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

Released images are published to Amazon ECR. See [Releasing](#releasing).

## Releasing

Each release publishes an image to Amazon ECR tagged with the release version. The
newest release also receives the `latest` tag.

Before you start:

- Merge the changes into `main`.
- Confirm that **Maven Tests** passed for the commit you want to release.
- Choose a version in `MAJOR.MINOR.PATCH` format. Increase `MAJOR` for breaking
  changes, `MINOR` for new backward-compatible features, and `PATCH` for fixes.

### Option 1: Run the Create Release workflow (recommended)

1. In GitHub, open the **Actions** tab and select **Create Release**.
2. Select **Run workflow** and keep `main` as the branch.
3. Enter the version without a `v`, for example `1.2.0`.
4. Optionally enter a commit SHA from `main`. Leave it blank to release the head of `main`.
5. Select **Run workflow**.

You can start the same workflow from the GitHub CLI:

```bash
gh workflow run release.yml -f version=1.2.0
gh run watch
```

The workflow checks the version, commit and test result, creates the `v1.2.0` tag and
GitHub release with generated notes, and publishes the image.

### Option 2: Create the release in GitHub

1. Open the repository's **Releases** page and select **Draft a new release**.
2. Under **Choose a tag**, type the new tag, for example `v1.2.0`, and select the
   option to create it on publish. The `v` is required here.
3. Set **Target** to `main`.
4. Enter a title and select **Generate release notes**.
5. Leave **Set as a pre-release** unchecked. Drafts and prereleases do not publish an image.
6. Select **Publish release**. The **Publish Image** workflow then starts.

### After releasing

Check that the workflow succeeded in the **Actions** tab, then confirm the image exists:

```bash
aws ecr describe-images --repository-name horse-inventory-service \
  --image-ids imageTag=v1.2.0
```

- **If publishing fails:** open the failed run and select **Re-run failed jobs**. The
  GitHub release already exists, so do not create it again.
- **If a release has a bug:** fix it on `main` and release a new patch version. A
  version that is already in ECR is never rebuilt or overwritten.

### Access

Publishing authenticates through GitHub OIDC and reads the `AWS_REGION`, `AWS_ROLE_ARN`
and `ECR_REPOSITORY` repository variables. The IAM role's trust policy must allow
`refs/heads/main` for Option 1 and `refs/tags/v*` for Option 2.

## Configuration

| Property | Env var | Default |
|---|---|---|
| `horse.repo-service.base-url` | `HORSE_REPO_SERVICE_URL` | `http://localhost:13000` |
