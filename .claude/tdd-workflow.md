# TDD Workflow

Follow Kent Beck's Test-Driven Development and Tidy First principles.

## Plan Files

- **Active plans**: `.claude/plan-<ticket-id>-<feature-name>.md`
- **Archived plans**: `.claude/archive/plan-*.md`
- **Template**: `.claude/plan-template.md`

When the user says "go" or "let's go", find the next unmarked test in the active plan, implement the test, then implement only enough code to make that test pass.

## The TDD Cycle

Execute this cycle for every test case:

### 1. RED Phase
- Write a failing test FIRST
- Run the test to verify it FAILS
- **CRITICAL**: Never skip verifying the RED phase
- If the test passes unexpectedly, investigate why

### 2. GREEN Phase
- Implement the MINIMUM code to make the test pass
- Run tests to confirm GREEN
- Do not add extra functionality or "improvements"

### 3. REFACTOR Phase
- **STOP and ask**: "Is there anything to refactor?" before proceeding
- Look for:
    - Duplicate code that can be extracted
    - Code clarity improvements
    - Compile-time safety improvements
- Run tests after EACH refactoring step
- If tests fail after refactoring, REVERT and try a different approach

### 4. UPDATE and COMMIT
- Update the active plan file to mark the completed test
- Commit with proper message format
- Only then proceed to the next test

## Commit Discipline

### When to Commit
- After GREEN phase (test passes)
- After successful refactoring
- Only commit when ALL tests pass

### Commit Rules
- Separate behavioral and structural commits
- **NEVER** add Co-Authored-By lines

### Message Format
```
<type>: <short description>

<detailed description of what and why>
```

Types:
- `feat:` - Behavioral change (new test + implementation)
- `refactor:` - Structural change (no behavior change)
- `fix:` - Bug fix
- `test:` - Test-only changes
- `docs:` - Documentation
- `chore:` - Maintenance

## Workflow Checklist

For each test case in the active plan:

- [ ] Write the failing test
- [ ] Run test - verify RED (must see failure)
- [ ] Implement minimum code
- [ ] Run test - verify GREEN
- [ ] Commit the behavioral change
- [ ] Ask: "Any refactoring needed?"
- [ ] If yes: refactor, run tests, commit refactoring separately
- [ ] Update the plan file
- [ ] Proceed to next test

## Communication Protocol

### Before Refactoring
Always ask before refactoring or suggest specific improvements and wait for approval.

### When Uncertain
- If a test approach is unclear, discuss options before implementing
- If requirements seem incomplete, update the plan and verify with user

## Anti-Patterns to Avoid

- Implementing code before seeing RED
- Skipping the refactoring question
- Mixing behavioral and structural changes in one commit
- Moving to next test without updating the plan