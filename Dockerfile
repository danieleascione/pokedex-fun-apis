FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /app
COPY gradle gradle
COPY gradlew build.gradle.kts settings.gradle.kts gradle.properties ./
COPY src src

RUN ./gradlew installDist --no-daemon

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app
COPY --from=build /app/build/install/pokedex-fun-apis ./

EXPOSE 8080

CMD ["./bin/pokedex-fun-apis"]