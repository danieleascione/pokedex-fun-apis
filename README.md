# Pokédex Coding Challenge

## Prerequisites

- Java 21+ ([installation guide](INSTALL.md))

## Build

```bash
./gradlew build
```

## Run

```bash
./gradlew run
```

## Docker

**Build and run:**
```bash
docker build -t pokedex-fun-apis .
docker run -p 8080:8080 pokedex-fun-apis
```

The Dockerfile builds the jar and then starts the container where the application runs, meaning that it doesn't require 
a Java installation on the host machine.

**Test health endpoint:**
```bash
curl http://localhost:8080/health
```

**Or use the script** (builds, tests, and cleans up):
```bash
./build-start-and-test-in-docker.sh
```

## Things to explain

As you may have noticed from the agent setup with Claude, I follow an ATDD-inspired workflow:
•	Start with a failing acceptance test
•	Write the minimum amount of code needed to make the test pass
•	Refactor

I’ve adopted a simplified version of [Dave Farley’s 4-layer test design](https://continuous-delivery.co.uk/downloads/ATDD%20Guide%2026-03-21.pdf.
Tests can run against multiple targets, with the target selected via an environment variable.

The ATDD cycle begins at the outer layer—an end-to-end run against a deployed environment (simulated here by running the application in Docker).
From there, it moves inward to the next layers, such as starting the application in memory using Ktor, and so on.

Alongside this flow, I would typically add inner-layer tests as unit tests.
    