package com.raksit.example.loyalty.user.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.raksit.example.loyalty.user.dto.UserDTO;
import com.raksit.example.loyalty.user.entity.User;
import com.raksit.example.loyalty.user.repository.UserRepository;
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
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  void shouldReturnUserDTO_whenFindUserById_givenUserWithIdExists() {
    // Given
    final String userId = UUID.randomUUID().toString();
    final User user = new User(RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10));
    user.setPoints(100L);
    when(userRepository.findById(UUID.fromString(userId)))
        .thenReturn(Optional.of(user));

    // When
    final UserDTO actual = userService.findUserById(userId);

    // Then
    assertThat(actual.getId(), equalTo(userId));
    assertThat(actual.getFirstName(), equalTo(user.getFirstName()));
    assertThat(actual.getLastName(), equalTo(user.getLastName()));
    assertThat(actual.getEmail(), equalTo(user.getEmail()));
    assertThat(actual.getPhone(), equalTo(user.getPhone()));
    assertThat(actual.getPoints(), equalTo(user.getPoints()));
  }

  @Test
  void shouldThrowNoSuchElementException_whenFindUserById_givenUserWithIdNotExist() {
    // Given
    final String userId = UUID.randomUUID().toString();
    when(userRepository.findById(UUID.fromString(userId)))
        .thenReturn(Optional.empty());

    // When
    // Then
    assertThrows(NoSuchElementException.class, () -> userService.findUserById(userId));
  }
}