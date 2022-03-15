package com.raksit.example.loyalty.feature;

public interface FeatureToggleService {

  boolean isEnabled(FeatureName featureName);
}
