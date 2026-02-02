# Pokédex API - Problem Analysis

## 1. Problem Statement and Context

### Core Problem
Build a REST API service that enriches Pokemon data from PokéAPI with optional fun translations (Shakespeare/Yoda) from FunTranslations API. The service acts as an aggregation layer that combines and transforms data from two external APIs.

### Business Context
This is a demonstration project to showcase API integration, error handling, and production-ready code practices. The service must be resilient to external API failures while maintaining good performance and user experience.

### Success Criteria
- Two working REST endpoints returning JSON
- Correct data transformation from PokéAPI format to response format
- Proper translation routing logic (cave/legendary → Yoda, others → Shakespeare)
- Graceful degradation when translation API fails
- Production-ready code quality (tests, documentation, error handling)

---

## 2. Stakeholder Analysis

### Primary Stakeholders
**End Users (API Consumers)**
- Need: Reliable, fast responses with Pokemon information
- Pain point: Want enriched data without dealing with multiple API calls themselves
- Expectation: Clear error messages when something goes wrong

**Developers (Maintainers)**
- Need: Clear, testable, maintainable code
- Pain point: Understanding design decisions and trade-offs
- Expectation: Good documentation, high-value tests, clear separation of concerns

### External Dependencies (Implicit Stakeholders)
**PokéAPI (https://pokeapi.co/)**
- Provides source data; we depend on their uptime and response format
- Rate limits unknown - need to investigate
- Schema changes could break our integration

**FunTranslations API**
- Optional enhancement layer; failures must not break core functionality
- Known to have rate limits (often strict on free tier)
- Response time variability could impact our SLA

---

## 3. User Needs and Pain Points

### User Journey 1: Basic Pokemon Information
**Goal**: Get Pokemon details quickly without dealing with complex PokéAPI responses

**Current Pain Points**:
- PokéAPI `pokemon-species` endpoint returns large, nested JSON
- Multiple `flavor_text` entries (different languages, different game versions)
- Need to filter for English descriptions
- Habitat and legendary status buried in response structure

**Desired Outcome**: Single, clean endpoint with exactly the fields needed

### User Journey 2: Translated Descriptions
**Goal**: Get Pokemon descriptions in fun fictional dialects

**Current Pain Points**:
- Would require two separate API calls (PokéAPI + FunTranslations)
- Need to implement translation routing logic themselves
- Translation API often has rate limits - users want fallback behavior
- Error handling complexity when chaining APIs

**Desired Outcome**: Single endpoint that handles everything with intelligent fallback

---

## 4. Functional Requirements

### FR-1: Basic Pokemon Information Endpoint
**Endpoint**: `GET /pokemon/{name}`

**Inputs**:
- Path parameter: Pokemon name (string, case-insensitive per PokéAPI behavior)

**Outputs** (JSON):
```json
{
  "name": "string",
  "description": "string",
  "habitat": "string | null",
  "isLegendary": "boolean"
}
```

**Processing Logic**:
1. Call PokéAPI `https://pokeapi.co/api/v2/pokemon-species/{name}`
2. Extract `name` from response
3. Extract first English `flavor_text` entry (may need to strip newlines/special chars)
4. Extract `habitat.name` (nullable field)
5. Extract `is_legendary` boolean
6. Transform to response format

**Validation**:
- Pokemon name must not be empty
- Must handle URL encoding (e.g., "mr-mime")

### FR-2: Translated Pokemon Information Endpoint
**Endpoint**: `GET /pokemon/translated/{name}`

**Inputs**:
- Path parameter: Pokemon name (string, case-insensitive)

**Outputs**: Same JSON structure as FR-1, but with translated description

**Processing Logic**:
1. Call PokéAPI `https://pokeapi.co/api/v2/pokemon-species/{name}` (same as FR-1)
2. Extract all fields as in FR-1
3. **Translation Routing**:
   - IF `habitat == "cave"` OR `is_legendary == true` → Use Yoda translation
   - ELSE → Use Shakespeare translation
4. Call appropriate FunTranslations endpoint:
   - Yoda: `https://api.funtranslations.com/translate/yoda.json?text={description}`
   - Shakespeare: `https://api.funtranslations.com/translate/shakespeare.json?text={description}`
5. **Fallback**: If translation fails (API error, rate limit, timeout), use original description
6. Return response with translated (or fallback) description

**Critical Business Rule**: Translation failure MUST NOT fail the entire request

---

## 5. Non-Functional Requirements

### NFR-1: Performance
- **Response Time**: Target < 2 seconds for 95th percentile (depends on external APIs)
- **Throughput**: Must handle concurrent requests efficiently
- **Resource Usage**: Minimize memory footprint for HTTP client connections

### NFR-2: Reliability
- **Availability**: Service should be available even when FunTranslations API is down
- **Graceful Degradation**: Translation failures → fallback to original description
- **Error Handling**: Clear error responses for invalid Pokemon names, network failures

### NFR-3: Maintainability
- **Code Quality**: "Concise, readable, correct" per requirements
- **Test Coverage**: High-value unit tests (not necessarily 100% coverage)
- **Documentation**: Design decisions must be documented
- **Separation of Concerns**: Clear boundaries between routing, business logic, external API calls

### NFR-4: Production Readiness
- **Logging**: Sufficient logs for debugging without exposing sensitive data
- **Configuration**: External API URLs should be configurable
- **Observability**: Consider metrics/health checks for external API dependencies
- **Docker Support**: Already provided - must ensure it works with final implementation

### NFR-5: API Design
- **RESTful**: Follow REST conventions for resource naming
- **Content Type**: JSON responses with proper `Content-Type` headers
- **Status Codes**:
  - 200 OK for successful responses
  - 404 Not Found for invalid Pokemon names
  - 500/503 for service errors
- **Error Response Format**: Consistent structure for error messages

---

## 6. Data and Information Needs

### 6.1 PokéAPI Integration

**Endpoint**: `GET https://pokeapi.co/api/v2/pokemon-species/{name}`

**Required Fields from Response**:
```
{
  "name": string,
  "flavor_text_entries": [
    {
      "flavor_text": string,
      "language": { "name": string }
    }
  ],
  "habitat": { "name": string } | null,
  "is_legendary": boolean
}
```

**Data Extraction Challenges**:
- `flavor_text_entries` is an array - need to find first English entry
- `flavor_text` may contain newlines (`\n`) and form feeds (`\f`) - need cleanup
- `habitat` can be `null` for some Pokemon
- Pokemon names in API use lowercase with hyphens (e.g., "mr-mime")

**Unknown Assumptions**:
- What if no English flavor text exists?
- What if `flavor_text_entries` array is empty?
- Are there Pokemon without descriptions?

### 6.2 FunTranslations API Integration

**Yoda Endpoint**: `POST/GET https://api.funtranslations.com/translate/yoda.json`
**Shakespeare Endpoint**: `POST/GET https://api.funtranslations.com/translate/shakespeare.json`

**Request Format**:
- Query parameter: `text={description}`
- OR POST with JSON body (need to verify which method works)

**Expected Response Format**:
```json
{
  "success": { "total": 1 },
  "contents": {
    "translated": "string",
    "text": "string",
    "translation": "yoda" | "shakespeare"
  }
}
```

**Known Constraints**:
- **Rate Limiting**: Free tier typically 5 requests/hour (CRITICAL CONSTRAINT)
- **Response Time**: Can be slow (1-3 seconds)
- **Availability**: Not guaranteed uptime
- **Text Length Limits**: May have max character limits

**Unknown Assumptions**:
- What HTTP status codes indicate rate limiting? (429? 503?)
- What's the exact error response format?
- Are there text length limits?
- Does special character encoding matter?

### 6.3 Data Transformation Requirements

**Text Cleanup**:
- Remove newline characters (`\n`)
- Remove form feed characters (`\f`)
- Preserve punctuation and capitalization
- Handle potential Unicode characters

**Name Normalization**:
- Input names should be case-insensitive
- Convert to lowercase for PokéAPI calls
- Handle names with spaces, hyphens, special characters

---

## 7. Business Rules and Constraints

### BR-1: Translation Routing Logic
```
IF (habitat == "cave" OR is_legendary == true)
  THEN use Yoda translation
  ELSE use Shakespeare translation
```

**Edge Cases**:
- What if habitat is `null`? → Not "cave", so use Shakespeare (unless legendary)
- Legendary Pokemon with cave habitat? → Use Yoda (OR condition met)

### BR-2: Fallback Behavior
- Translation API failure → Use original (untranslated) description
- No English flavor text → What should happen? (UNKNOWN - need decision)
- Empty description → What should happen? (UNKNOWN - need decision)

### BR-3: External API Rate Limits
- **PokéAPI**: Generally permissive, no known strict limits
- **FunTranslations**: ~5 requests/hour on free tier
- **Implication**: Testing against real APIs will quickly hit limits
- **Solution Needed**: Mock external APIs for testing, or implement caching

### BR-4: Error Handling Philosophy
- **Fail Fast**: Invalid Pokemon name → 404 immediately
- **Fail Safe**: Translation failure → Fallback to original description
- **Transparency**: Log external API failures for observability

---

## 8. Integration and External System Requirements

### Integration Points

**1. PokéAPI (Required, Synchronous)**
- Protocol: HTTP/HTTPS
- Method: GET
- Authentication: None
- Timeout: Recommend 5-10 seconds
- Retry Strategy: Consider retries for transient failures (5xx errors)
- Circuit Breaker: Consider if API frequently unavailable

**2. FunTranslations API (Optional, Synchronous)**
- Protocol: HTTP/HTTPS
- Method: GET (or POST - needs verification)
- Authentication: None (free tier) or API key (paid tier)
- Timeout: Recommend 10-15 seconds (can be slow)
- Retry Strategy: Do NOT retry rate limit errors (429)
- Fallback: Always fallback on failure

### HTTP Client Requirements
- Connection pooling for performance
- Configurable timeouts
- Proper error handling for network failures
- Support for JSON deserialization
- Logging of requests/responses for debugging

### Ktor HTTP Client
- Already have Ktor framework - natural choice to use Ktor client
- Need to add dependencies: `ktor-client-core`, `ktor-client-cio` (or other engine)
- Supports everything needed: timeouts, connection pooling, JSON serialization

---

## 9. Edge Cases and Error Scenarios

### Edge Case Matrix

| Scenario | Expected Behavior | Priority |
|----------|-------------------|----------|
| Invalid Pokemon name (e.g., "doesnotexist") | 404 Not Found with error message | HIGH |
| PokéAPI returns 404 | Propagate 404 to client | HIGH |
| PokéAPI returns 5xx | 503 Service Unavailable | HIGH |
| PokéAPI timeout | 504 Gateway Timeout | MEDIUM |
| No English flavor text | Decision needed: error? empty string? | HIGH |
| Empty flavor text array | Decision needed: error? default message? | HIGH |
| Habitat is null | Valid case - use Shakespeare unless legendary | HIGH |
| FunTranslations rate limit (429) | Fallback to original description, log event | HIGH |
| FunTranslations timeout | Fallback to original description | HIGH |
| FunTranslations 5xx error | Fallback to original description | HIGH |
| FunTranslations returns invalid JSON | Fallback to original description | MEDIUM |
| Pokemon name with special chars (é, ♂, ♀) | Handle URL encoding correctly | MEDIUM |
| Very long flavor text (>1000 chars) | May hit translation API limits - investigate | LOW |
| Concurrent requests for same Pokemon | Should work independently (stateless) | MEDIUM |
| Legendary cave-dwelling Pokemon | Use Yoda (OR condition) | LOW |

### Critical Unknowns to Investigate

**Priority 1 (Must Investigate Before Implementation)**:
1. What does PokéAPI return for Pokemon with no English flavor text?
2. What HTTP status codes does FunTranslations API use for rate limiting?
3. What's the exact request format for FunTranslations (GET vs POST)?
4. Are there character length limits for FunTranslations?

**Priority 2 (Nice to Know)**:
5. What's the typical response time for each API?
6. Does PokéAPI support HTTP/2 or compression?
7. Are there any batch endpoints to reduce API calls?

---

## 10. Key Architectural Decisions Needed

### Decision 1: HTTP Client Implementation
**Options**:
- Ktor Client (consistent with Ktor server)
- Java 11+ HttpClient (standard library)
- Third-party library (OkHttp, Apache HttpClient)

**Recommendation**: Ktor Client
- Already using Ktor framework
- Excellent Kotlin DSL
- Built-in JSON serialization support
- Connection pooling and timeout support

### Decision 2: Error Handling Strategy
**Options**:
- Exceptions for all errors (idiomatic Kotlin)
- Result/Either types for error handling
- Nullable types with null representing errors

**Recommendation**: Exceptions for truly exceptional cases (network failures), Result types for expected failures (Pokemon not found)

### Decision 3: External API Abstraction
**Options**:
- Direct HTTP calls in route handlers (simple but not testable)
- Repository/Service layer (classic separation)
- Interface-based clients (best for testing)

**Recommendation**: Interface-based clients (PokemonApiClient, TranslationApiClient) for testability

### Decision 4: Testing Strategy
**Approach**:
- Unit tests with mocked HTTP clients
- Integration tests with real APIs (sparingly - rate limits)
- Contract tests to verify external API assumptions
- Test fixture data (save real API responses for testing)

### Decision 5: Configuration Management
**Needs**:
- External API base URLs (for testing/environment switching)
- Timeout values
- Port number

**Options**:
- Hardcoded (simple but inflexible)
- Environment variables (12-factor app compliant)
- Configuration file (HOCON, YAML)

**Recommendation**: Environment variables with sensible defaults

### Decision 6: Response Caching
**Consideration**: Pokemon data rarely changes - could cache responses

**Options**:
- No caching (simplest)
- In-memory cache (improves performance, reduces API load)
- HTTP cache headers (let clients cache)

**Recommendation**: No caching for MVP - add if needed for performance

### Decision 7: Logging Strategy
**What to Log**:
- Incoming requests (Pokemon name, endpoint)
- External API calls (URL, status code, response time)
- Translation fallback events (critical for observability)
- Errors and stack traces

**What NOT to Log**:
- Full external API responses (could be large)
- Sensitive data (none in this app, but good practice)

---

## 11. Risks and Unknowns

### High Risk

**R-1: FunTranslations API Rate Limits**
- **Risk**: Development/testing will quickly exhaust free tier (5 req/hour)
- **Impact**: Cannot test translated endpoint against real API
- **Mitigation**: Mock FunTranslations API for testing; use recorded responses
- **Residual Risk**: Real API behavior may differ from assumptions

**R-2: PokéAPI Response Format Changes**
- **Risk**: PokéAPI could change JSON schema without notice
- **Impact**: Parsing failures, null pointer exceptions
- **Mitigation**: Defensive parsing; comprehensive error handling; monitoring
- **Residual Risk**: Schema changes could be subtle and not trigger errors

**R-3: External API Availability**
- **Risk**: APIs may be down when demonstrating the project
- **Impact**: Bad first impression, appears broken
- **Mitigation**: Graceful fallback for translations; health check endpoint that doesn't depend on external APIs
- **Residual Risk**: Basic endpoint still depends on PokéAPI

### Medium Risk

**R-4: Performance Bottlenecks**
- **Risk**: Sequential API calls (PokéAPI → FunTranslations) could be slow
- **Impact**: Poor user experience, timeouts
- **Mitigation**: Reasonable timeout values; consider caching if needed
- **Residual Risk**: Limited control over external API performance

**R-5: Character Encoding Issues**
- **Risk**: Special Pokemon characters (♂, ♀, é) may cause encoding problems
- **Impact**: Request failures, garbled text
- **Mitigation**: Proper UTF-8 handling, URL encoding
- **Residual Risk**: Edge cases may still exist

### Low Risk

**R-6: Translation Quality**
- **Risk**: Translations might not make sense or be amusing
- **Impact**: Feature seems pointless
- **Mitigation**: Accept this as a "fun" feature; documentation sets expectations
- **Residual Risk**: N/A - this is inherent to the fun translations concept

---

## 12. Assumptions Requiring Validation

### Critical Assumptions (MUST VALIDATE)

**A-1: PokéAPI English Flavor Text**
- **Assumption**: Every Pokemon has at least one English flavor text entry
- **Validation Method**: Query several Pokemon including edge cases (MissingNo, recent generations)
- **If False**: Need error handling or default description logic

**A-2: FunTranslations API Request Format**
- **Assumption**: GET request with query parameter works
- **Validation Method**: Test against real API (before rate limit)
- **If False**: Implement POST request with JSON body

**A-3: FunTranslations Rate Limit Status Code**
- **Assumption**: Returns 429 (Too Many Requests) for rate limiting
- **Validation Method**: Intentionally trigger rate limit during testing
- **If False**: Update error detection logic

**A-4: Habitat Null Handling**
- **Assumption**: Some Pokemon have null habitat (e.g., legendary/mythical)
- **Validation Method**: Query known Pokemon (Mewtwo, Mew, Arceus)
- **If False**: Adjust null handling logic

### Non-Critical Assumptions (VALIDATE IF TIME)

**A-5: PokéAPI Case Sensitivity**
- **Assumption**: API accepts mixed case Pokemon names
- **Validation Method**: Test with "Pikachu", "PIKACHU", "pikachu"
- **If False**: Need case normalization before API call

**A-6: Translation Preserves Meaning**
- **Assumption**: Translated text is still comprehensible
- **Validation Method**: Manual review of several translations
- **If False**: Document as known limitation

---

## 13. Domain Model

### Core Entities

**Pokemon (Domain Model)**
```kotlin
data class Pokemon(
    val name: String,
    val description: String,
    val habitat: String?,
    val isLegendary: Boolean
)
```

**PokemonSpecies (PokéAPI Response Model)**
```kotlin
data class PokemonSpeciesResponse(
    val name: String,
    val flavor_text_entries: List<FlavorTextEntry>,
    val habitat: Habitat?,
    val is_legendary: Boolean
)

data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language
)

data class Language(
    val name: String
)

data class Habitat(
    val name: String
)
```

**TranslationResponse (FunTranslations API Response Model)**
```kotlin
data class TranslationResponse(
    val success: Success,
    val contents: Contents
)

data class Success(
    val total: Int
)

data class Contents(
    val translated: String,
    val text: String,
    val translation: String
)
```

### Domain Services

**PokemonService**
- Orchestrates fetching and translating Pokemon data
- Implements translation routing logic
- Handles fallback scenarios

**PokemonApiClient (Interface)**
- Abstracts PokéAPI calls
- Returns domain model or throws exceptions

**TranslationApiClient (Interface)**
- Abstracts FunTranslations API calls
- Returns translated text or null (on failure)

---

## 14. Success Metrics

### Functional Completeness
✅ `/pokemon/{name}` endpoint returns correct data
✅ `/pokemon/translated/{name}` endpoint applies correct translation logic
✅ Translation fallback works when API fails
✅ Appropriate HTTP status codes for errors

### Code Quality
✅ Clear separation of concerns (routing, business logic, API clients)
✅ High-value unit tests (not brittle, test behavior not implementation)
✅ Design decisions documented
✅ Code is concise and readable

### Production Readiness
✅ Dockerfile works and builds correctly
✅ Health check endpoint functions
✅ Proper error handling and logging
✅ Configuration externalized (or documented)

### Bonus Points
✅ Meaningful git commit history
✅ README with setup and usage instructions
✅ Contract tests or API response fixtures
✅ Observability considerations (metrics, structured logging)

---

## 15. Out of Scope

Explicitly NOT required for this challenge:

- Authentication/Authorization
- Rate limiting on our API
- Database persistence
- Caching layer
- Pagination or batch operations
- GraphQL or other API paradigms
- Frontend/UI
- CI/CD pipeline configuration
- Kubernetes/advanced deployment
- API versioning
- Internationalization beyond English
- Admin endpoints or management APIs

---

## 16. Next Steps (Not Implementation Suggestions)

To proceed with this challenge, the following information is needed:

### Immediate Validation Tasks
1. **Test PokéAPI Response Format**: Query 3-5 Pokemon to verify actual response structure
2. **Test FunTranslations API**: Make sample requests to understand rate limits, response format, status codes
3. **Document Edge Cases**: Create test fixtures with real API responses for edge cases

### Open Questions to Resolve
1. What should happen if no English flavor text exists?
2. Should we normalize Pokemon names (case-insensitive)?
3. What's the desired behavior for empty or missing descriptions?
4. Should we implement any caching to reduce external API calls?

### Documentation Needs
1. Decision log for architectural choices
2. API contract documentation (our endpoints)
3. External API dependency documentation
4. Testing strategy documentation

---

## Summary

This Pokédex API challenge is fundamentally an **integration and transformation problem**. The core technical challenges are:

1. **External API Integration**: Reliably calling and parsing two external APIs with different characteristics
2. **Error Resilience**: Gracefully handling failures in optional features (translations)
3. **Data Transformation**: Cleaning and mapping external API formats to our simplified response model
4. **Business Logic**: Implementing translation routing rules correctly

The main risks center around **external API reliability and rate limits**, particularly for FunTranslations API. The solution must prioritize **testability** (mocking external APIs) and **observability** (logging translation fallbacks).

Success requires **clear architectural boundaries** between routing, business logic, and external API clients, combined with **high-value tests** that verify behavior without being brittle.