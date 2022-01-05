package com.raksit.example.loyalty.writer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.job.writer.InactiveUserRepositoryItemWriter;
import com.raksit.example.loyalty.user.entity.User;
import com.raksit.example.loyalty.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class InactiveUserRepositoryItemWriterTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private InactiveUserRepositoryItemWriter inactiveUserRepositoryItemWriter;

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
  }

  @Test
  void shouldDeleteInactiveUser_whenCallWrite_givenExistingUserIsNoLongerActive() throws Exception {
    // Given
    final User user = new User("John", "Doe", "john.doe@example.com", "+6678901234");
    userRepository.save(user);
    user.setPoints(100L);
    user.setIsActive(false);
    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();

    // When
    StepScopeTestUtils.doInStepScope(stepExecution, () -> {
      inactiveUserRepositoryItemWriter.write(List.of(user));
      return null;
    });

    // Then
    Optional<User> updatedUser = userRepository.findById(user.getId());
    assertThat(updatedUser.isPresent(), equalTo(false));
  }

  @Test
  void shouldDoNothing_whenCallWrite_givenNonExistingUserIsNoLongerActive() throws Exception {
    // Given
    final User user = new User("John", "Doe", "john.doe@example.com", "+6678901234");
    user.setPoints(100L);
    user.setIsActive(false);
    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();

    // When
    StepScopeTestUtils.doInStepScope(stepExecution, () -> {
      inactiveUserRepositoryItemWriter.write(List.of(user));
      return null;
    });

    // Then
    Iterable<User> updatedUser = userRepository.findAll();
    assertThat(updatedUser.iterator().hasNext(), equalTo(false));
  }

  @Test
  void shouldDoNothing_whenCallWrite_givenExistingUserIsStillActive() throws Exception {
    // Given
    final User user = new User("John", "Doe", "john.doe@example.com", "+6678901234");
    userRepository.save(user);
    user.setPoints(100L);
    user.setIsActive(true);
    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();

    // When
    StepScopeTestUtils.doInStepScope(stepExecution, () -> {
      inactiveUserRepositoryItemWriter.write(List.of(user));
      return null;
    });

    // Then
    Optional<User> updatedUser = userRepository.findById(user.getId());
    assertThat(updatedUser.isPresent(), equalTo(true));
  }
}
