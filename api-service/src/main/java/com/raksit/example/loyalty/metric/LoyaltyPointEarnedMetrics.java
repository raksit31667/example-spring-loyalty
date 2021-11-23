package com.raksit.example.loyalty.metric;

import io.micrometer.core.instrument.Metrics;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyPointEarnedMetrics {

  private static final String METRIC_NAME = "loyalty_points_earned";
  private static final String USER_ID_TAG = "user_id";
  private static final String ACTIVITY_ID_TAG = "activity_id";

  public void initializeMetric(String userId, String activityId) {
    Metrics.counter(METRIC_NAME, USER_ID_TAG, userId, ACTIVITY_ID_TAG, activityId);
  }

  public void incrementMetric(String userId, String activityId, long earnedPoints) {
    Metrics.counter(METRIC_NAME, USER_ID_TAG, userId, ACTIVITY_ID_TAG, activityId).increment(earnedPoints);
  }
}
