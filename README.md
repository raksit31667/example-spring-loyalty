# example-spring-loyalty

[![Build status](https://badge.buildkite.com/889411af93ce4fb117084d8cd0a0c6dd38603bc151279d5b1a.svg)](https://buildkite.com/raksit31667/example-spring-loyalty)

Example RESTful API for loyalty program powered by Spring Boot

## Directory structure

Here is the basic skeleton for your app repo that each of the starter templates conforms to:

```bash
├── .buildkite
├── api-service
│   ├── src
│   │   ├── integration-test
│       ├── main
│       ├── test
│       ├── build.gradle # Define app dependencies here
├── config
│   ├── checkstyle
│   ├── codestyle
├── gradle
│   ├── wrapper
├── helm
├── performance-test
├── docker-compose.yml
├── Dockerfile
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

- It is also recommended updating `docker-compose.yml` if your app requires external components,
  such as database, and message queue to test locally.

- The `build.gradle` in root path is used for defining global configuration. In this case, I
  provided simple SonarQube and Docker build & publish plugins.

- The [Buildkite](https://buildkite.com/) CICD pipeline script can be found beneath the `.buildkite`
  directory.

## Style Guide

Since it is much easier to understand a large codebase when all the code in it is in a consistent
style, we use [IntelliJ Java Google Style](https://google.github.io/styleguide/) where you can
import to IntelliJ by navigating to **Preference > Editor > Code Style > Java**, then click
on [Kebab Menu](https://thenounproject.com/term/kebab-menu/198755/) and **Import Scheme**. Finally,
browse to the XML file underneath `config/codestyle`.