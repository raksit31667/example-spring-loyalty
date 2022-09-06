package com.raksit.example.loyalty.user.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.raksit.example.loyalty.user.dto.UserDTO;
import com.raksit.example.loyalty.user.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserDomainService userDomainService;

  @Mock
  private UserBannerService userBannerService;

  @InjectMocks
  private UserService userService;

  @Test
  void shouldReturnUserDTO_whenFindUserById_givenUserWithIdExists() {
    // Given
    final UUID userId = UUID.randomUUID();
    final User user = new User(RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10));
    user.setActivityPoints(100L);
    user.setNumberOfSubscriptions(10L);
    when(userDomainService.findUserById(userId))
        .thenReturn(Optional.of(user));
    when(userBannerService.getBase64BannerImage(userId.toString()))
        .thenReturn("data:image/png;base64,R5ay8lR8GNIaUi60FgrEjQ==");

    // When
    final UserDTO actual = userService.findUserById(userId.toString());

    // Then
    assertThat(actual.getId(), equalTo(userId.toString()));
    assertThat(actual.getFirstName(), equalTo(user.getFirstName()));
    assertThat(actual.getLastName(), equalTo(user.getLastName()));
    assertThat(actual.getEmail(), equalTo(user.getEmail()));
    assertThat(actual.getPhone(), equalTo(user.getPhone()));
    assertThat(actual.getPoints(), equalTo(user.getTotalPoints()));
    assertThat(actual.getNumberOfSubscriptions(), equalTo(user.getNumberOfSubscriptions()));
    assertThat(actual.getBannerImage(), equalTo("data:image/png;base64,R5ay8lR8GNIaUi60FgrEjQ=="));
  }

  @Test
  void shouldThrowNoSuchElementException_whenFindUserById_givenUserWithIdNotExist() {
    // Given
    final UUID userId = UUID.randomUUID();
    when(userDomainService.findUserById(userId))
        .thenReturn(Optional.empty());

    // When
    // Then
    assertThrows(NoSuchElementException.class, () -> userService.findUserById(userId.toString()));
  }
}