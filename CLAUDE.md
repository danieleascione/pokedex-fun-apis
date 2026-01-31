# Pokédex Fun APIs project

## Build & Test

```bash
java25 && ./gradlew build      # Build and test
java25 && ./gradlew test       # Run tests only
```

## Project Structure

- `src/main/kotlin/` - Source code
- `src/test/kotlin/` - Tests
- `gradle/libs.versions.toml` - Dependency versions

## Principles

- **Minimal dependencies**: Do not add dependencies unless strictly required
- **Latest versions**: Always use the latest available version when adding dependencies

## Development Workflow

- Read the active plan (file starting with `active-plan`) to understand which feature we're developing. If empty, work with the agents to create user stories and plans.
- Follow [TDD Workflow](.claude/tdd-workflow.md)
- Use [Plan Template](.claude/plan-template.md) for new features
