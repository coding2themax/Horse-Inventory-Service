# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Scope note

The parent `../CLAUDE.md` describes other projects (willow-api, willow-service, PetCore). Its rules do **not** apply here:
- This repo uses annotation-based `@RestController`s.
- It targets **Java 27**, not 25.
- It runs on Spring Boot **4.1.x**.

## Commands

```bash
./mvnw verify                      # full build: generate client, compile, test, JaCoCo gate (what CI runs)
./mvnw generate-sources            # regenerate the OpenAPI client only
./mvnw test -Dtest=RemoteHorseServiceTest                    # one test class
./mvnw test -Dtest=RemoteHorseServiceTest#createGetUpdateAndDeleteMapResults  # one method
./mvnw spring-boot:run             # run locally
```

- Run single tests with `test`, not `verify`. The JaCoCo `check` goal runs in `verify` with `haltOnFailure=true` and a **90% instruction-coverage minimum per package**, so a partial test run fails it.
- No linter or formatter is configured.
- The project name is spelled "Iventory" in some places, for example `HorseIventoryServiceApplication` and the artifact `name`. Keep that in mind when searching.

## Architecture

There are two independent feature areas under `com.coding2themax.horse.horseinventoryservice`.

**Horses: spec-first, backed by a remote service**
- The flow is `RemoteHorseService` (implements `HorseService`) → `HorseRepository` / `RemoteHorseRepository` → the generated client `org.openapitools.client.api.HorseReactiveRepoServiceApi` → the external **horse data service**. That service is not built yet, and its URL comes from `horse.repo-service.base-url` / `HORSE_REPO_SERVICE_URL`, default `http://localhost:13000`.
- The contract lives in `src/main/resources/horse-data-spec.yaml`. `openapi-generator-maven-plugin` generates the client into `target/generated-sources/openapi` on every build, using the `webclient` library with Spring Boot 4 and Jackson 3. **Never edit the generated code; change the spec.** The generated code is excluded from JaCoCo.
- The domain model (`model.horse.Horse` with the `HorseColor`, `HorseCoatPattern` and `HorseTraining` enums) and the generated model (`org.openapitools.client.model.Horse`, `HorseColor`, …) share class names. Mapping code therefore uses fully qualified names for one side.
- Enums are mapped **by constant name**, so the domain enums and the spec's enum schemas must list identical constants. `RemoteHorseServiceTest.everyEnumConstantRoundTrips` fails if they drift.
- Domain ids are `String`, while remote ids are `int32` and marked `readOnly` in the spec, so they are never sent on create or update. `RemoteHorseService.parseId` converts them. An invalid id is returned as an `IllegalArgumentException` error signal, never thrown during assembly.
- Repository conventions:
  - A remote 404 maps to an empty `Mono`, and to `false` for `deleteHorse`.
  - Delete success is read from the HTTP status through `deleteHorseWithHttpInfo`, because the spec returns 204 with no body.
  - Filtering is pushed to the server via query parameters on `getAllHorses`.
- There is no HTTP controller for horses yet.

**Horse goods: in-memory CRUD**
- The flow is `HorseGoodsController` (`/horsegoods`) → `HorseGoodService` / `InMemoryHorseGoodService`, which is backed by a `ConcurrentHashMap`. Its data is lost on restart.
- The model `HorseGoods` is mutable, so the service copies values into and out of the store.
- Creating a good with an existing `goodsId` raises `HorseGoodAlreadyExistsException`, which the controller maps to 409. A missing good maps to 404 via `ResponseStatusException`.
- `HorseFeed` and `HorseProduct` in `model.store` are plain models and are not wired into any endpoint.

All service and repository methods are reactive (`Mono` or `Flux`). Do not call `.block()` in main code.

## Spring Boot 4 specifics

- Boot's `WebClient.Builder` bean comes from `spring-boot-starter-webclient`, not from the webflux starter. `HorseRepoClientConfig` builds the `ApiClient` from that builder, so it inherits Boot's codecs and observability.
- Test dependencies come from `spring-boot-starter-webflux-test`. `@WebFluxTest` lives in `org.springframework.boot.webflux.test.autoconfigure`, and mocks use `@MockitoBean`.

## Testing conventions

- Mockito can mock interfaces such as `HorseRepository` and `HorseGoodService`, but **cannot mock concrete classes on JDK 27**, because the bundled Byte Buddy doesn't support it. For the generated `HorseReactiveRepoServiceApi`, use the hand-written `StubApi` subclass in `RemoteHorseRepositoryTest` and override the methods you need.
- Reactive assertions use `StepVerifier`. Controller tests use `@WebFluxTest` with `WebTestClient`.
- `HorseIventoryServiceApplicationTest` is a full `@SpringBootTest` context-load check, so new beans must be wireable without the remote service running.

## CI

`.github/workflows/maven-tests.yml` runs `./mvnw -B -ntp verify` on JDK 27 (Temurin) for pushes to `main` and for PRs. It then uploads `target/site/jacoco/jacoco.xml` to Codecov, using the `CODECOV_TOKEN` repo secret. That upload never fails the job. Surefire and JaCoCo reports are also stored as build artifacts.
