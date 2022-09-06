package com.raksit.example.loyalty.user.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import com.raksit.example.loyalty.user.entity.User;
import com.raksit.example.loyalty.user.entity.UserSubscriptionCount;
import com.raksit.example.loyalty.user.repository.UserRepository;
import com.raksit.example.loyalty.user.repository.UserSubscriptionCountRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserDomainServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserSubscriptionCountRepository userSubscriptionCountRepository;

  @InjectMocks
  private UserDomainService userDomainService;

  @Test
  void shouldReturnUser_findUserById_givenUserExistsWithNumberOfSubscriptions() {
    // Given
    final UUID userId = UUID.randomUUID();
    final User user = new User(RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10));
    user.setActivityPoints(100L);
    long numberOfSubscriptions = RandomUtils.nextLong();
    when(userRepository.findById(userId))
        .thenReturn(Optional.of(user));
    when(userSubscriptionCountRepository.findByUserId(userId))
        .thenReturn(Optional.of(new UserSubscriptionCount(userId, numberOfSubscriptions)));

    // When
    Optional<User> actual = userDomainService.findUserById(userId);

    // Then
    assertThat(actual.isPresent(), equalTo(true));
    assertThat(actual.get().getId(), equalTo(userId));
    assertThat(actual.get().getFirstName(), equalTo(user.getFirstName()));
    assertThat(actual.get().getLastName(), equalTo(user.getLastName()));
    assertThat(actual.get().getEmail(), equalTo(user.getEmail()));
    assertThat(actual.get().getPhone(), equalTo(user.getPhone()));
    assertThat(actual.get().getNumberOfSubscriptions(), equalTo(numberOfSubscriptions));
    assertThat(actual.get().getTotalPoints(), equalTo(user.getActivityPoints() + numberOfSubscriptions));
  }

  @Test
  void shouldReturnUser_findUserById_givenUserExistsWithoutNumberOfSubscriptions() {
    // Given
    final UUID userId = UUID.randomUUID();
    final User user = new User(RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10));
    user.setActivityPoints(100L);
    when(userRepository.findById(userId))
        .thenReturn(Optional.of(user));
    when(userSubscriptionCountRepository.findByUserId(userId))
        .thenReturn(Optional.empty());

    // When
    Optional<User> actual = userDomainService.findUserById(userId);

    // Then
    assertThat(actual.isPresent(), equalTo(true));
    assertThat(actual.get().getId(), equalTo(userId));
    assertThat(actual.get().getFirstName(), equalTo(user.getFirstName()));
    assertThat(actual.get().getLastName(), equalTo(user.getLastName()));
    assertThat(actual.get().getEmail(), equalTo(user.getEmail()));
    assertThat(actual.get().getPhone(), equalTo(user.getPhone()));
    assertThat(actual.get().getNumberOfSubscriptions(), equalTo(0L));
    assertThat(actual.get().getTotalPoints(), equalTo(user.getActivityPoints()));
  }

  @Test
  void shouldReturnEmpty_findUserById_givenUserNotExist() {
    // Given
    final UUID userId = UUID.randomUUID();
    final User user = new User(RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10));
    user.setActivityPoints(100L);
    when(userRepository.findById(userId))
        .thenReturn(Optional.empty());
    // When
    Optional<User> actual = userDomainService.findUserById(userId);

    // Then
    assertThat(actual.isPresent(), equalTo(false));
  }
}