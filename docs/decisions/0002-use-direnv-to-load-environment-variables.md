# 2. Use direnv to load environment variables

Date: 2021-12-15

## Status

Accepted

## Context

We have tech difficulties to maintain environment variables for both local and deployment
environment. For example, if we would like to configure database credentials instead of hard-coded,
we would go with following snippets:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://\${DB_HOSTNAME}:5432/\${DB_NAME}
    username: \${DB_USERNAME}
    password: \${DB_PASSWORD}
```

Therefore, we need to manually export `DB_HOSTNAME`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`
environment variables. We are looking for a tool that help us load environment variables
automatically.

## Decision

We use [direnv](https://direnv.net/) that can load and unload environment variables depending on the
current directory.

## Consequences

We don't have to export variables repetitively.