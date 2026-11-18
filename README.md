# Horse Inventory Service

[![Maven Tests](https://github.com/coding2themax/Horse-Inventory-Service/actions/workflows/maven-tests.yml/badge.svg)](https://github.com/coding2themax/Horse-Inventory-Service/actions/workflows/maven-tests.yml)
[![codecov](https://codecov.io/gh/coding2themax/Horse-Inventory-Service/graph/badge.svg?token=140O794JPA)](https://codecov.io/gh/coding2themax/Horse-Inventory-Service)

Spring Boot WebFlux service for horse inventory and horse goods. Horse data is
read from a separate horse data service through a client generated from
[`horse-data-spec.yaml`](src/main/resources/horse-data-spec.yaml).

## Build and test

Requires JDK 27.

```bash
./mvnw verify   # generate the client, run tests, enforce 90% JaCoCo coverage
```

## Configuration

| Property | Env var | Default |
|---|---|---|
| `horse.repo-service.base-url` | `HORSE_REPO_SERVICE_URL` | `http://localhost:13000` |
