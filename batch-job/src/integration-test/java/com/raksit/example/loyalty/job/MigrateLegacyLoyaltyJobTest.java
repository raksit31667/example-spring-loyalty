package com.raksit.example.loyalty.job;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.mock.LegacyLoyaltyMockServer;
import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBatchTest
@IntegrationTest
public class MigrateLegacyLoyaltyJobTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private JobRepositoryTestUtils jobRepositoryTestUtils;

  private LegacyLoyaltyMockServer legacyLoyaltyMockServer;

  @BeforeEach
  void setUp() throws JsonProcessingException {
    legacyLoyaltyMockServer = new LegacyLoyaltyMockServer();
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    jobRepositoryTestUtils.removeJobExecutions();
    legacyLoyaltyMockServer.stop();
  }

  @Test
  void shouldUpdateUserLoyaltyPoints_whenExecute_givenExistingUsers() throws Exception {
    // Given
    User user = userRepository.save(
        new User("John", "Doe", "john.doe@example.com", "+6678901234"));

    User anotherUser = userRepository.save(
        new User("Raksit", "Man", "raksit.man@example.com", "+6678904321"));

    legacyLoyaltyMockServer.addHappyPathExpectations(user.getId());
    legacyLoyaltyMockServer.addHappyPathExpectations(anotherUser.getId());

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

  @Test
  void shouldSkipFailedUser_whenExecute_givenSystemCannotUpdatePoints() throws Exception {
    // Given
    User user = userRepository.save(
        new User("John", "Doe", "john.doe@example.com", "+6678901234"));

    User anotherUser = userRepository.save(
        new User("Raksit", "Man", "raksit.man@example.com", "+6678904321"));

    legacyLoyaltyMockServer.addUnhappyPathExpectations(user.getId());
    legacyLoyaltyMockServer.addHappyPathExpectations(anotherUser.getId());

    // When
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters());
    Optional<User> updatedUser = userRepository.findById(user.getId());
    Optional<User> updatedAnotherUser = userRepository.findById(anotherUser.getId());

    // Then
    assertThat(jobExecution.getJobInstance().getJobName(), equalTo("migrateLegacyLoyalty"));
    assertThat(jobExecution.getExitStatus(), equalTo(ExitStatus.COMPLETED));
    assertThat(updatedUser.isPresent(), equalTo(true));
    assertThat(updatedUser.get().getPoints(), equalTo(0L));
    assertThat(updatedAnotherUser.isPresent(), equalTo(true));
    assertThat(updatedAnotherUser.get().getPoints(), equalTo(100L));
  }
}
