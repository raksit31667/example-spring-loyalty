package com.raksit.example.loyalty.activity.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class ActivityConfigurationPropertiesTest {

  @Test
  void shouldReturnActivityPoints_whenGetPoints_givenActivityIdExistsInPointMap() {
    // Given
    final String activityId = RandomStringUtils.random(10);
    final ActivityConfigurationProperties properties = new ActivityConfigurationProperties();
    properties.setPointMap(new HashMap<>() {
      {
        put(activityId, 20L);
      }
    });

    // When
    final Optional<Long> points = properties.getPoints(activityId);

    // Then
    assertThat(points.isPresent(), equalTo(true));
  }

  @Test
  void shouldReturnEmpty_whenGetPoints_givenActivityIdDoesNotExistInPointMap() {
    // Given
    final String activityId = RandomStringUtils.random(10);
    final ActivityConfigurationProperties properties = new ActivityConfigurationProperties();
    properties.setPointMap(new HashMap<>() {
      {
        put("abcd", 20L);
      }
    });

    // When
    final Optional<Long> points = properties.getPoints(activityId);

    // Then
    assertThat(points.isEmpty(), equalTo(true));
  }
}