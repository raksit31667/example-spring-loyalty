package com.raksit.example.loyalty.feature;

import static com.raksit.example.loyalty.feature.FeatureName.EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import org.junit.jupiter.api.Test;

class DefaultFeatureToggleServiceTest {

  @Test
  void shouldReturnTrue_whenIsEnabled_givenPropertiesReturnsTrue() {
    // Given
    DefaultFeatureFlagProperties properties = new DefaultFeatureFlagProperties();
    properties.setFlags(new HashMap<>() {
      {
        put("EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE", true);
      }
    });
    DefaultFeatureToggleService defaultFeatureToggleService = new DefaultFeatureToggleService(properties);

    // When
    // Then
    assertThat(defaultFeatureToggleService.isEnabled(EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE), equalTo(true));
  }

  @Test
  void shouldReturnFalse_whenIsEnabled_givenPropertiesReturnsFalse() {
    // Given
    DefaultFeatureFlagProperties properties = new DefaultFeatureFlagProperties();
    properties.setFlags(new HashMap<>() {
      {
        put("EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE", false);
      }
    });
    DefaultFeatureToggleService defaultFeatureToggleService = new DefaultFeatureToggleService(properties);

    // When
    // Then
    assertThat(defaultFeatureToggleService.isEnabled(EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE), equalTo(false));
  }

  @Test
  void shouldReturnFalse_whenIsEnabled_givenPropertiesNotFound() {
    // Given
    DefaultFeatureFlagProperties properties = new DefaultFeatureFlagProperties();
    properties.setFlags(new HashMap<>() {
      {
        put("EXAMPLE_SPRING_LOYALTY_BLAH_BLAH", true);
      }
    });
    DefaultFeatureToggleService defaultFeatureToggleService = new DefaultFeatureToggleService(properties);

    // When
    // Then
    assertThat(defaultFeatureToggleService.isEnabled(EXAMPLE_SPRING_LOYALTY_EXAMPLE_FEATURE), equalTo(false));
  }
}