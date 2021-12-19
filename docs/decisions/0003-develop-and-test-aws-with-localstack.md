# 3. Develop and test AWS with localstack

Date: 2021-12-19

## Status

Accepted

## Context

Our system integrates AWS resources (i.e. Kinesis, DynamoDB, S3). How can we perform an integration
testing within local machine?

- Configure an application to connect to the real AWS resources? -> Infrastructure cost?
- Perform integration testing after deployment to deployment environment -> Feedback loop?

## Decision

We use [localstack](https://localstack.cloud/) to develop and test AWS infrastructure.

```yaml
version: '3'
services:
  localstack:
    image: localstack/localstack
    environment:
      - AWS_DEFAULT_REGION=ap-southeast-1
      - EDGE_PORT=4566
      - SERVICES=kinesis, dynamodb
    ports:
      - '4566:4566'
    volumes:
      - localstack:/tmp/localstack
      - './setup-localstack.sh:/docker-entrypoint-initaws.d/setup-localstack.sh'

volumes:
  localstack:
```

To run `localstack` via `docker-compose` CLI:
```shell
$ docker-compose -f docker-compose-local.yml up localstack
```

## Consequences

- Shortened feedback loop by testing locally in Docker.
- Prevent rolling out untested changes.
- Huge cost savings for development teams.
- Require `awslocal` installation.
