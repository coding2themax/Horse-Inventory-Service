# syntax=docker/dockerfile:1

# ---- Build stage: compile the app (generates the OpenAPI client) ----
FROM eclipse-temurin:27-jdk AS build
WORKDIR /workspace

# Resolve dependencies first so they are cached until pom.xml changes.
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -ntp dependency:go-offline

COPY src src
# Tests and the JaCoCo gate run in CI before the image is built.
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -ntp package -DskipTests \
    && cp target/horse-inventory-service-*.jar app.jar \
    && java -Djarmode=tools -jar app.jar extract --layers --launcher --destination extracted

# ---- Runtime stage ----
FROM eclipse-temurin:27-jre
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app --no-create-home app

# Copy the layers from least to most frequently changing for better caching.
COPY --from=build /workspace/extracted/dependencies/ ./
COPY --from=build /workspace/extracted/spring-boot-loader/ ./
COPY --from=build /workspace/extracted/snapshot-dependencies/ ./
COPY --from=build /workspace/extracted/application/ ./

USER app
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
