package com.raksit.example.loyalty.activity.consumer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.raksit.example.loyalty.activity.event.Activity;
import com.raksit.example.loyalty.activity.event.ActivityPerformed;
import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.transaction.entity.Transaction;
import com.raksit.example.loyalty.transaction.repository.TransactionRepository;
import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
import java.time.Instant;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class ActivityPerformedConsumerIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private ActivityPerformedConsumer activityPerformedConsumer;

  @AfterEach
  void tearDown() {
    transactionRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void shouldSaveNewTransaction_whenAccept_givenActivityPerformedEventMatchesActivityPointsMapping() {
    // Given
    final User user = userRepository.save(new User("firstName", "lastName", "email", "phone"));
    final long nowEpochSeconds = System.currentTimeMillis();
    final ActivityPerformed activityPerformed = ActivityPerformed.builder()
        .activity(Activity.builder()
            .id("abcd")
            .name(RandomStringUtils.random(10))
            .userId(user.getId().toString())
            .build())
        .performedOn(nowEpochSeconds)
        .build();

    // When
    activityPerformedConsumer.accept(activityPerformed);

    // Then
    Transaction transaction = transactionRepository.findAll().iterator().next();
    assertThat(transaction.getId(), notNullValue());
    assertThat(transaction.getUser().getId(), equalTo(user.getId()));
    assertThat(transaction.getPoints(), equalTo(20L));
    assertThat(transaction.getPerformedOn(), equalTo(Instant.ofEpochSecond(nowEpochSeconds)));
  }

  @Test
  void shouldDoNothing_whenAccept_givenActivityPerformedEventUserIdDoesNotExist() {
    // Given
    final long nowEpochSeconds = System.currentTimeMillis();
    final ActivityPerformed activityPerformed = ActivityPerformed.builder()
        .activity(Activity.builder()
            .id("abcd")
            .name(RandomStringUtils.random(10))
            .userId(UUID.randomUUID().toString())
            .build())
        .performedOn(nowEpochSeconds)
        .build();

    // When
    activityPerformedConsumer.accept(activityPerformed);

    // Then
    assertThat(transactionRepository.findAll().iterator().hasNext(), equalTo(false));
  }

  @Test
  void shouldDoNothing_whenAccept_givenActivityPerformedEventDoesNotMatchActivityPoints() {
    // Given
    final User user = userRepository.save(new User("firstName", "lastName", "email", "phone"));
    final long nowEpochSeconds = System.currentTimeMillis();
    final ActivityPerformed activityPerformed = ActivityPerformed.builder()
        .activity(Activity.builder()
            .id("xyz")
            .name(RandomStringUtils.random(10))
            .userId(user.getId().toString())
            .build())
        .performedOn(nowEpochSeconds)
        .build();

    // When
    activityPerformedConsumer.accept(activityPerformed);

    // Then
    assertThat(transactionRepository.findAll().iterator().hasNext(), equalTo(false));
  }
}
