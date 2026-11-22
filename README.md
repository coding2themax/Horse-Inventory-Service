# Horse Inventory Service

[![Maven Tests](https://github.com/coding2themax/Horse-Inventory-Service/actions/workflows/maven-tests.yml/badge.svg)](https://github.com/coding2themax/Horse-Inventory-Service/actions/workflows/maven-tests.yml)
[![codecov](https://codecov.io/gh/coding2themax/Horse-Inventory-Service/graph/badge.svg?token=140O794JPA)](https://codecov.io/gh/coding2themax/Horse-Inventory-Service)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)

Spring Boot WebFlux service for horse inventory and horse goods. Horse data is
read from a separate horse data service through a client generated from
[`horse-data-spec.yaml`](src/main/resources/horse-data-spec.yaml).

## Build and test

Requires JDK 27.

```bash
./mvnw verify   # generate the client, run tests, enforce 90% JaCoCo coverage
```

## Docker image

```bash
docker build -t horse-inventory-service .
docker run -p 8080:8080 -e HORSE_REPO_SERVICE_URL=http://host.docker.internal:13000 horse-inventory-service
```

Publishing a GitHub release runs the **Publish Image** workflow, which pushes the
image to Amazon ECR tagged with the release tag, plus `latest` for the repository's
latest release. The release commit must be on `main` and have passed **Maven Tests**.
Drafts and prereleases do not publish an image. The workflow authenticates through
GitHub OIDC and reads these repository variables: `AWS_REGION`, `AWS_ROLE_ARN` and
`ECR_REPOSITORY`.

## Configuration

| Property | Env var | Default |
|---|---|---|
| `horse.repo-service.base-url` | `HORSE_REPO_SERVICE_URL` | `http://localhost:13000` |
