package com.raksit.example.loyalty.writer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class UserRepositoryItemWriterTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RepositoryItemWriter<User> userRepositoryItemWriter;

  @Test
  void shouldUpdateUser_whenCallWrite_givenUserWithLoyaltyPoints() throws Exception {
    // Given
    final User user = new User("John", "Doe", "john.doe@example.com", "+6678901234");
    User savedUser = userRepository.save(user);
    user.setPoints(100L);
    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();

    // When
    StepScopeTestUtils.doInStepScope(stepExecution, () -> {
      userRepositoryItemWriter.write(List.of(user));
      return null;
    });

    // Then
    Optional<User> updatedUser = userRepository.findById(user.getId());
    assertThat(updatedUser.isPresent(), equalTo(true));
    assertThat(updatedUser.get().getPoints(), equalTo(100L));
  }
}
