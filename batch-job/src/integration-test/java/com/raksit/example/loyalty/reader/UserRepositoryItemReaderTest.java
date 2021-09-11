package com.raksit.example.loyalty.reader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class UserRepositoryItemReaderTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RepositoryItemReader<User> userRepositoryItemReader;

  @Test
  void shouldReturnAllUsers_whenCallReader_givenUsersExistInRepository() throws Exception {
    // Given
    User savedUser = userRepository.save(
        new User("John", "Doe", "john.doe@example.com", "+6678901234")
    );
    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();

    // When
    StepScopeTestUtils.doInStepScope(stepExecution, () -> {
      User user;
      userRepositoryItemReader.open(stepExecution.getExecutionContext());
      while ((user = userRepositoryItemReader.read()) != null) {
        // Then
        assertThat(user.getId(), equalTo(savedUser.getId()));
        assertThat(user.getFirstName(), equalTo("John"));
        assertThat(user.getLastName(), equalTo("Doe"));
        assertThat(user.getEmail(), equalTo("john.doe@example.com"));
        assertThat(user.getPhone(), equalTo("+6678901234"));
      }
      userRepositoryItemReader.close();
      return null;
    });
  }
}
