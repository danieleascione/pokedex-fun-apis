# Claude Code Agents

This directory contains specialized Claude Code agents for agentic workflow development.

## Attribution

Most of the agents are sourced from [andlaf-ak/claude-code-agents](https://github.com/andlaf-ak/claude-code-agents) by **Andrea Laforgia**, some are adapter to accomodate my personal preferences.

## Available Agents

| Agent | Purpose | Color |
|-------|---------|-------|
| `problem-analyst` | Deep problem understanding and analysis before implementation | Orange |
| `user-story-writer` | Breaking features into granular, INVEST-compliant user stories | Green |
| `atdd-developer` | Implementing user stories using Acceptance Test Driven Development | Cyan |
| `code-smell-detector` | Analyzing code for quality issues and design problems | Yellow |
| `refactoring-expert` | Generating comprehensive refactoring recommendations | Blue |
| `test-design-reviewer` | Evaluating test quality using Dave Farley's principles | Orange |

## Workflow Pipelines

### Feature Development Pipeline
1. **problem-analyst** - Analyze and understand the problem domain
2. **user-story-writer** - Decompose into implementable user stories
3. **atdd-developer** - Implement stories using Red-Green-Refactor cycle

### Code Quality Pipeline
1. **code-smell-detector** - Identify code smells and design issues
2. **refactoring-expert** - Generate refactoring recommendations
3. **test-design-reviewer** - Evaluate test quality with Farley Score

### Legacy Code Pipeline
1. **legacy-code-expert** - Analyze and safely modify legacy code
2. **code-smell-detector** - Identify issues to address
3. **refactoring-expert** - Plan refactoring approach

## Integration

These agents complement the existing [TDD workflow](../tdd-workflow.md) by adding structured problem analysis, story decomposition, code quality analysis, and test review phases.