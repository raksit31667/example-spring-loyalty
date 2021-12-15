# 1. Use Buildkite as a CICD pipeline

Date: 2021-12-15

## Status

Accepted

## Context

Buildkite is a platform for running fast, secure, and scalable continuous integration pipelines on
our own infrastructure. According to [Buildkite documentation](https://buildkite.com/features), The
open-source Elastic CI Stack for AWS gives us an easy-to-maintain, elastically scaling CI stack in
our own AWS account.

## Decision

We use Buildkite as a CICD pipeline

## Consequences

By using Buildkite, we will benefit from:

- **Unlimited Language Support**: Build, test, and deploy Docker-based projects with the agent’s
  built in Docker Compose support, or our own build scripts for maximum control.
- **Agent Plugins**: Use agent plugins for common tools and workflows, such as Docker and Docker
  Compose.

On the other hand, we will have additional cost ($15 for monthly subscription and $30+ for AWS
infrastructure) for maintaining Buildkite agent on Elastic CI Stack