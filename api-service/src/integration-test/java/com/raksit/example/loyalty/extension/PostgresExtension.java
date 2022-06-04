package com.raksit.example.loyalty.extension;

import org.junit.ClassRule;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class PostgresExtension implements BeforeAllCallback, AfterAllCallback {

  @ClassRule
  public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
      DockerImageName.parse("postgres:alpine"))
      .waitingFor(
          Wait.forLogMessage(".*database system is ready to accept connections.*", 1)
      );

  @Override
  public void beforeAll(ExtensionContext context) {
    postgresContainer.start();
    System.setProperty("DB_URL", postgresContainer.getJdbcUrl());
    System.setProperty("DB_DRIVER_CLASS_NAME", postgresContainer.getDriverClassName());
    System.setProperty("DB_USERNAME", postgresContainer.getUsername());
    System.setProperty("DB_PASSWORD", postgresContainer.getPassword());
  }

  @Override
  public void afterAll(ExtensionContext context) {
    postgresContainer.stop();
  }
}
