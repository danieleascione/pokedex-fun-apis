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

**Test health endpoint:**
```bash
curl http://localhost:8080/health
```

**Or use the script** (builds, tests, and cleans up):
```bash
./build-start-and-test-in-docker.sh
```