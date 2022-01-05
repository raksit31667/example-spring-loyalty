package com.raksit.example.loyalty.feature;

import io.split.client.SplitClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

@Component
public class FeatureToggle {

  @Value("${split.io.api.key}")
  private String apiToken;

  private final SplitClient splitClient;

  private final Environment environment;

  public FeatureToggle(SplitClient splitClient, Environment environment) {
    this.splitClient = splitClient;
    this.environment = environment;
  }

  public boolean isToggledOn(String featureName) {
    if (environment.acceptsProfiles(Profiles.of("default"))) {
      return true;
    }
    final String featureFlag = splitClient.getTreatment(apiToken, featureName);
    if (featureFlag.equals("on")) {
      return true;
    } else if (featureFlag.equals("off")) {
      return false;
    } else {
      throw new RuntimeException("Error retrieving feature flag from Split.io");
    }
  }
}
