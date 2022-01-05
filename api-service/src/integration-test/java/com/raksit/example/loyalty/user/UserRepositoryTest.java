package com.raksit.example.loyalty.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.user.entity.User;
import com.raksit.example.loyalty.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void shouldSaveUser_whenSave_givenNewUser() {
    // Given
    final User savedUser = userRepository.save(new User("firstName", "lastName", "email", "phone"));

    // When
    Optional<User> actual = userRepository.findById(savedUser.getId());

    // Then
    assertTrue(actual.isPresent());
    assertEquals(savedUser.getId(), actual.get().getId());
  }
}
