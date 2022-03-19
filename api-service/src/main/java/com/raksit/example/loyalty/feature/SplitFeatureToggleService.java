package com.raksit.example.loyalty.feature;

import io.split.client.SplitClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Profile({"dev", "functional-test", "prod"})
@Component
public class SplitFeatureToggleService implements FeatureToggleService {

  public static final String SPLIT_TREATMENT_ON = "on";
  public static final String SPLIT_TREATMENT_OFF = "off";

  private final SplitClient splitClient;

  public SplitFeatureToggleService(SplitClient splitClient) {
    this.splitClient = splitClient;
  }

  @Override
  public boolean isEnabled(FeatureName featureName) {
    final String featureFlag = splitClient.getTreatment("", featureName.name());
    if (SPLIT_TREATMENT_ON.equals(featureFlag)) {
      return true;
    } else {
      if (!SPLIT_TREATMENT_OFF.equals(featureFlag)) {
        log.error("Error retrieving feature flag from Split.io");
      }
      return false;
    }
  }
}
