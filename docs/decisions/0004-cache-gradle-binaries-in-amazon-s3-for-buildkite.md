# 4. Cache Gradle binaries in Amazon S3 for Buildkite

Date: 2021-12-19

## Status

Accepted

## Context

The Buildkite CI build takes ~10 minutes per step because Java Docker image running the build needs
to download non-existing Gradle binaries.

## Decision

We store Gradle binaries in Amazon S3 and use them as build cache by downloading binaries before
step starts and upload binaries back after step is done.

## Consequences

- Shortened feedback loop due to reduced build duration.
- Additional Amazon S3 infrastructure cost.