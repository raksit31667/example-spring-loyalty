#!/usr/bin/env bash

set -euo pipefail

current_dir="$(dirname "$0")"

service_name="$1"

docker login --username $DOCKER_USERNAME --password $DOCKER_PASSWORD

docker build -t "example-spring-loyalty-$service_name" "./$service_name" --build-arg SEMANTIC_VERSION=${SEMANTIC_VERSION}

docker tag "example-spring-loyalty-$service_name" "raksit31667/example-spring-loyalty-$service_name:${SEMANTIC_VERSION}"

docker push "raksit31667/example-spring-loyalty-$service_name:${SEMANTIC_VERSION}"