package com.raksit.example.loyalty.writer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.user.entity.User;
import com.raksit.example.loyalty.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class UserCompositeItemWriterTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CompositeItemWriter<User> userCompositeItemWriter;

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
  }

  @Test
  void shouldInsertActiveUser_whenCallWrite_givenExistingUserIsStillActive() throws Exception {
    // Given
    final User user = new User("John", "Doe", "john.doe@example.com", "+6678901234");
    user.setActivityPoints(100L);
    user.setIsActive(true);
    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();

    // When
    StepScopeTestUtils.doInStepScope(stepExecution, () -> {
      userCompositeItemWriter.write(List.of(user));
      return null;
    });

    // Then
    Iterable<User> updatedUser = userRepository.findAll();
    assertThat(updatedUser.iterator().hasNext(), equalTo(true));
    assertThat(updatedUser.iterator().next().getActivityPoints(), equalTo(100L));
  }

  @Test
  void shouldDeleteInactiveUser_whenCallWrite_givenExistingUserIsNoLongerActive() throws Exception {
    // Given
    final User user = new User("John", "Doe", "john.doe@example.com", "+6678901234");
    userRepository.save(user);
    user.setActivityPoints(100L);
    user.setIsActive(false);
    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();

    // When
    StepScopeTestUtils.doInStepScope(stepExecution, () -> {
      userCompositeItemWriter.write(List.of(user));
      return null;
    });

    // Then
    Optional<User> updatedUser = userRepository.findById(user.getId());
    assertThat(updatedUser.isPresent(), equalTo(false));
  }
}
