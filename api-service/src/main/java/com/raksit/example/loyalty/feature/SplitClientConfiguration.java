package com.raksit.example.loyalty.feature;

import io.split.client.SplitClient;
import io.split.client.SplitClientConfig;
import io.split.client.SplitFactory;
import io.split.client.SplitFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;

@Profile({"dev", "functional-test", "prod"})
@Configuration
public class SplitClientConfiguration {

  @Value("${split.io.api.key}")
  private String splitApiKey;

  private final Environment environment;

  public SplitClientConfiguration(Environment environment) {
    this.environment = environment;
  }

  @Bean
  public SplitClient splitClient()
      throws IOException, URISyntaxException, InterruptedException, TimeoutException {
    final SplitClientConfig clientConfig = SplitClientConfig.builder()
        .setBlockUntilReadyTimeout(10000)
        .enableDebug()
        .build();

    final SplitFactory factory = SplitFactoryBuilder.build(splitApiKey, clientConfig);
    final SplitClient client = factory.client();

    if (environment.acceptsProfiles(Profiles.of("!default"))) {
      client.blockUntilReady();
    }

    return client;
  }
}
