package com.raksit.example.loyalty.activity.consumer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.raksit.example.loyalty.activity.config.ActivityConfigurationProperties;
import com.raksit.example.loyalty.activity.event.Activity;
import com.raksit.example.loyalty.activity.event.ActivityPerformed;
import com.raksit.example.loyalty.transaction.entity.Transaction;
import com.raksit.example.loyalty.transaction.repository.TransactionRepository;
import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActivityPerformedConsumerTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private ActivityConfigurationProperties activityConfigurationProperties;

  @Captor
  private ArgumentCaptor<Transaction> transactionArgumentCaptor;

  @InjectMocks
  private ActivityPerformedConsumer activityPerformedConsumer;

  @Test
  void shouldSaveNewTransaction_whenAccept_givenActivityPerformedEventMatchesActivityPointsMapping() {
    // Given
    final String userId = UUID.randomUUID().toString();
    final long nowEpochSeconds = System.currentTimeMillis();
    final String activityId = RandomStringUtils.random(10);
    final ActivityPerformed activityPerformed = ActivityPerformed.builder()
        .activity(Activity.builder()
            .id(activityId)
            .name(RandomStringUtils.random(10))
            .userId(userId)
            .build())
        .performedOn(nowEpochSeconds)
        .build();
    final User user = new User("firstName", "lastName", "email", "phone");
    user.setId(UUID.fromString(userId));
    when(userRepository.findById(UUID.fromString(userId)))
        .thenReturn(Optional.of(user));
    when(activityConfigurationProperties.getPoints(activityId))
        .thenReturn(Optional.of(20L));

    // When
    activityPerformedConsumer.accept(activityPerformed);

    // Then
    verify(transactionRepository).save(transactionArgumentCaptor.capture());
    Transaction transaction = transactionArgumentCaptor.getValue();
    assertThat(transaction.getUser().getId(), equalTo(user.getId()));
    assertThat(transaction.getPoints(), equalTo(20L));
    assertThat(transaction.getPerformedOn(), equalTo(Instant.ofEpochSecond(nowEpochSeconds)));
  }

  @Test
  void shouldDoNothing_whenAccept_givenActivityPerformedEventUserIdDoesNotExist() {
    // Given
    final String userId = UUID.randomUUID().toString();
    final long nowEpochSeconds = System.currentTimeMillis();
    final String activityId = RandomStringUtils.random(10);
    final ActivityPerformed activityPerformed = ActivityPerformed.builder()
        .activity(Activity.builder()
            .id(activityId)
            .name(RandomStringUtils.random(10))
            .userId(userId)
            .build())
        .performedOn(nowEpochSeconds)
        .build();
    when(userRepository.findById(UUID.fromString(userId)))
        .thenReturn(Optional.empty());

    // When
    activityPerformedConsumer.accept(activityPerformed);

    // Then
    verify(transactionRepository, never()).save(any(Transaction.class));
  }

  @Test
  void shouldDoNothing_whenAccept_givenActivityPerformedEventDoesNotMatchActivityPoints() {
    // Given
    final String userId = UUID.randomUUID().toString();
    final long nowEpochSeconds = System.currentTimeMillis();
    final String activityId = RandomStringUtils.random(10);
    final ActivityPerformed activityPerformed = ActivityPerformed.builder()
        .activity(Activity.builder()
            .id(activityId)
            .name(RandomStringUtils.random(10))
            .userId(userId)
            .build())
        .performedOn(nowEpochSeconds)
        .build();
    final User user = new User("firstName", "lastName", "email", "phone");
    user.setId(UUID.fromString(userId));
    when(userRepository.findById(UUID.fromString(userId)))
        .thenReturn(Optional.of(user));
    when(activityConfigurationProperties.getPoints(activityId))
        .thenReturn(Optional.empty());

    // When
    activityPerformedConsumer.accept(activityPerformed);

    // Then
    verify(transactionRepository, never()).save(any(Transaction.class));
  }
}