package com.raksit.example.loyalty.extension;

import java.io.FileNotFoundException;
import java.time.Duration;
import org.junit.ClassRule;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.util.ResourceUtils;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class ApiServiceExtension implements BeforeAllCallback, AfterAllCallback {

  @ClassRule
  public static DockerComposeContainer<?> dockerComposeContainer;

  // TODO: add .withEnv("SPLIT_API_KEY", "<credentials-from-secure-place>")
  static {
    try {
      dockerComposeContainer = new DockerComposeContainer<>(
          ResourceUtils.getFile("classpath:docker-compose-functional-test.yml"))
          .withExposedService("app_1", 8080,
              Wait.forListeningPort().withStartupTimeout(Duration.ofMinutes(5)));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void beforeAll(ExtensionContext context) {
    dockerComposeContainer.start();
  }

  @Override
  public void afterAll(ExtensionContext context) {
    dockerComposeContainer.stop();
  }

}
