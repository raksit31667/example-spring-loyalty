package com.raksit.example.loyalty.job;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBatchTest
@IntegrationTest
public class MigrateLegacyLoyaltyJobTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Test
  void shouldUpdateUserLoyaltyPoints_whenExecute_givenExistingUsers() throws Exception {
    // Given
    User user = userRepository.save(
        new User("John", "Doe", "john.doe@example.com", "+6678901234"));

    User anotherUser = userRepository.save(
        new User("Raksit", "Man", "raksit.man@example.com", "+6678904321"));

    // When
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters());
    Optional<User> updatedUser = userRepository.findById(user.getId());
    Optional<User> updatedAnotherUser = userRepository.findById(anotherUser.getId());

    // Then
    assertThat(jobExecution.getJobInstance().getJobName(), equalTo("migrateLegacyLoyalty"));
    assertThat(jobExecution.getExitStatus(), equalTo(ExitStatus.COMPLETED));
    assertThat(updatedUser.isPresent(), equalTo(true));
    assertThat(updatedUser.get().getPoints(), equalTo(100L));
    assertThat(updatedAnotherUser.isPresent(), equalTo(true));
    assertThat(updatedAnotherUser.get().getPoints(), equalTo(100L));
  }
}
