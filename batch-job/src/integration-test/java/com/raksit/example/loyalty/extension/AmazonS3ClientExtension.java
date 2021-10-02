package com.raksit.example.loyalty.extension;

import org.junit.ClassRule;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;

public class AmazonS3ClientExtension implements BeforeAllCallback, AfterAllCallback {

  @ClassRule
  public static LocalStackContainer localStackContainer = new LocalStackContainer(
      DockerImageName.parse("localstack/localstack"))
      .withServices(Service.S3);

  @Override
  public void beforeAll(ExtensionContext context) {
    localStackContainer.start();
  }

  @Override
  public void afterAll(ExtensionContext context) {
    localStackContainer.stop();
  }
}
