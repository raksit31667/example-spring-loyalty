# example-spring-loyalty

| Pipeline               | Status                                                                                                                                                                   |
|------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Continuous integration | [![Build status](https://badge.buildkite.com/889411af93ce4fb117084d8cd0a0c6dd38603bc151279d5b1a.svg)](https://buildkite.com/raksit31667/example-spring-loyalty)          |
| Daily security checks  | [![Build status](https://badge.buildkite.com/c60bd219502a8001bdff738372fc205f3fc38ec895471be532.svg)](https://buildkite.com/raksit31667/example-spring-loyalty-security) |

Example application loyalty program powered by Spring Boot

## Directory structure

Here is the basic skeleton for your app repo that each of the starter templates conforms to:

```bash
├── .buildkite
├── api-service
│   ├── src
│       ├── integration-test
│       ├── main
│       ├── test
│   ├── build.gradle # Define app dependencies here
│   ├── Dockerfile
│   ├── helm
├── batch-job
│   ├── src
│       ├── integration-test
│       ├── main
│       ├── test
│   ├── build.gradle # Define app dependencies here
│   ├── Dockerfile
│   ├── helm
├── config
│   ├── checkstyle
│   ├── codestyle
├── gradle
│   ├── wrapper
├── performance-test
├── docker-compose.yml
├── docker-compose-buildkite-agent.yml
├── docker-compose-local.yml
├── .envrc.template
├── build.gradle # Define global dependencies here
```

- Your app's source code is nested beneath the `api-service` directory. It composes of submodule for
  main logic, unit testing, and integration testing. Note that for app-specific dependencies, it is
  recommended to configure in `build.gradle` within this directory.

- You can configure coding standards, dependency vulnerability checks in `config` directory. I
  already provided **checkStyle** with simple Java rules.

- For compatibility with Kubernetes, Simple Helm chart is located in `helm` directory. However,
  other Kubernetes dependencies (e.g. `spring-cloud-starter-kubernetes`) is not included within this
  template. Please kindly add it if necessary.

- Your app's performance testing scripts, written in Scala, is nested beneath the `perfomance-test`
  directory. Please follow the Gatling DSL guideline [here](https://gatling.io/docs/current/).

- It is also recommended updating `docker-compose-local.yml` if your app requires external
  components, such as database, and message queue to test locally.

- The `build.gradle` in root path is used for defining global configuration. In this case, I
  provided simple SonarQube and Docker build & publish plugins.

- The [Buildkite](https://buildkite.com/) CICD pipeline script can be found beneath the `.buildkite`
  directory. The build step specifications are based from `docker-compose.yml`.

## Development guidelines

### Starting local server

Install Homebrew formulae [direnv](https://direnv.net/) that can load and unload environment
variables depending on the current directory.

Then, clone `.envrc.template` with:

```shell
$ cp .envrc.template .envrc
```

Fill environment variables in `.envrc`, then load environment variables by running:

```shell
$ direnv allow
```

Finally, start local server on `local` active profile by running:

```shell
$ docker-compose -f docker-compose-local.yml up -d && ./gradlew clean api-service:bootRun
```

```shell
$ docker-compose -f docker-compose-local.yml up -d && ./gradlew clean batch-job:bootRun
```

For other active profiles, specify profile on System property as:

```shell
$ ./gradlew clean api-service:bootRun -Dspring.profile.active=<your-profile-name-comma-separated>
```

### Compile and Build

The default Spring active profile is set to `local`,
add `-Dspring.profiles.active=<your-custom-profile>`
to customise the active profile.

```
$ ./gradlew clean build
```

To specify module in order to run Gradle tasks:

```
$ ./gradlew api-service:clean api-service:build

$ ./gradlew batch-job:clean batch-job:build
```

### Run mutation testing

References: [PIT Mutation Testing](https://pitest.org/)

We use PIT to run our unit tests against automatically modified versions of the application code.
When the application code changes, it should produce different results and cause the unit tests to
fail. If a unit test does not fail in this situation, it may indicate an issue with the test suite.

```
$ ./gradlew pitest
```

### Style Guide

Since it is much easier to understand a large codebase when all the code in it is in a consistent
style, we use [IntelliJ Java Google Style](https://google.github.io/styleguide/) where you can
import to IntelliJ by navigating to **Preference > Editor > Code Style > Java**, then click
on [Kebab Menu](https://thenounproject.com/term/kebab-menu/198755/) and **Import Scheme**. Finally,
browse to the XML file underneath `config/codestyle`.

### Environment variables

We use [direnv](https://direnv.net/) that can load and unload environment variables depending on the
current directory, so that you don't have to export variables repetitively.

After finish installation, copy `.envrc` based from `.envrc.template` and fill all required
variables, then run this following command in the root directory to apply variables:

```
direnv allow
```

### Git hooks

We use [ghooks.gradle](https://github.com/gtramontina/ghooks.gradle) for Git hooks Gradle plugin.

To download Git hooks plugin, run:

```shell
./gradlew your-module:clean your-module:build
```

### API documentation

See available API endpoints in [Swagger UI](http://localhost:8080/swagger-ui/index.html#/).

### CICD

[Buildkite](https://buildkite.com/) is a platform for running fast, secure, and scalable continuous
integration pipelines on our own infrastructure, it even works with a local Docker engine. Although
buildkite agent doesn't have dedicated agent for Java / Gradle, yet it allows builds to use Docker (
for more details, read
this [documentation](https://buildkite.com/docs/agent/v3/docker#allowing-builds-to-use-docker).)

Please be careful that **you have to start `buildkite-agent` in local machine in order to use
Buildkite.**

Install `buildkite-agent` based on your own machine. Follow the
instructions [here](https://buildkite.com/docs/agent/v3/installation). The snippet below is the
example for macOS using Homebrew:

```
brew install buildkite/buildkite/buildkite-agent
```

Then, you have to set Buildkite agent token for this pipeline, you can either run this command below
or use `direnv` by creating `.envrc` based from `.envrc.template`:

```
export BUILDKITE_AGENT_TOKEN=<your-buildkite-agent-token-here>
```

Finally, run `buildkite-agent` via `docker-compose` with the following command:

```
docker compose -f docker-compose-buildkite-agent.yml up
```

### Deployment

Each module contains Helm chart located in `helm` directory. To install/upgrade Helm chart, run:

```
helm upgrade --install example-spring-loyalty-<module-name> <module-name>/helm
```