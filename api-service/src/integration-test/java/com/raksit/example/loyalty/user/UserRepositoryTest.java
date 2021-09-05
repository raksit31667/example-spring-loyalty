package com.raksit.example.loyalty.user;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.util.Optional;
import java.util.UUID;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@FlywayTest
@AutoConfigureEmbeddedDatabase(provider = DOCKER)
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void shouldSaveUser_whenSave_givenNewUser() {
    // Given
    UUID id = UUID.randomUUID();
    userRepository.save(new User(id, "firstName", "lastName", "email", "phone"));

    // When
    Optional<User> actual = userRepository.findById(id);

    // Then
    assertTrue(actual.isPresent());
    assertEquals(id, actual.get().getId());
  }
}
