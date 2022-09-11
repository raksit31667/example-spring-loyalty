package com.raksit.example.loyalty.job.subscriptionimport.processor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.raksit.example.loyalty.job.subscriptionimport.subscription.entity.ExpiredUserSubscription;
import com.raksit.example.loyalty.job.subscriptionimport.subscription.entity.UserSubscription;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

class UserSubscriptionToExpiredUserSubscriptionProcessorTest {

  @Test
  void shouldReturnExpiredUserSubscription_whenProcess_givenUserSubscription() {
    // Given
    UserSubscription userSubscription = new UserSubscription(
        UUID.randomUUID(),
        RandomStringUtils.random(5),
        RandomStringUtils.random(5),
        Instant.now(),
        Instant.now().plus(1, ChronoUnit.DAYS)
    );
    UserSubscriptionToExpiredUserSubscriptionProcessor processor =
        new UserSubscriptionToExpiredUserSubscriptionProcessor();

    // When
    ExpiredUserSubscription expiredUserSubscription = processor.process(userSubscription);

    // Then
    assertThat(expiredUserSubscription.getUserId(), equalTo(userSubscription.getUserId()));
    assertThat(expiredUserSubscription.getProductName(), equalTo(userSubscription.getProductName()));
    assertThat(expiredUserSubscription.getProductId(), equalTo(userSubscription.getProductId()));
    assertThat(expiredUserSubscription.getSubscribedOn(), equalTo(userSubscription.getSubscribedOn()));
    assertThat(expiredUserSubscription.getActiveUntil(), equalTo(userSubscription.getActiveUntil()));
  }
}