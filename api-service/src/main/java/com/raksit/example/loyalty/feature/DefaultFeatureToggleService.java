package com.raksit.example.loyalty.feature;

import org.springframework.stereotype.Component;

@Component
public class DefaultFeatureToggleService implements FeatureToggleService {

  private final DefaultFeatureFlagProperties featureFlagProperties;

  public DefaultFeatureToggleService(DefaultFeatureFlagProperties featureFlagProperties) {
    this.featureFlagProperties = featureFlagProperties;
  }

  @Override
  public boolean isEnabled(FeatureName featureName) {
    return featureFlagProperties.getFlags().getOrDefault(featureName.name(), false);
  }
}
