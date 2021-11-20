package com.raksit.example.loyalty.job;

import static com.raksit.example.loyalty.extension.AmazonS3ClientExtension.localStackContainer;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.extension.AmazonS3ClientExtension;
import com.raksit.example.loyalty.legacy.LegacyLoyaltyUser;
import com.raksit.example.loyalty.mock.LegacyLoyaltyMockServer;
import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ResourceUtils;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;

@ExtendWith(AmazonS3ClientExtension.class)
@SpringBatchTest
@IntegrationTest
public class MigrateLegacyLoyaltyJobTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private JobRepositoryTestUtils jobRepositoryTestUtils;

  @Autowired
  private AmazonS3 amazonS3;

  private LegacyLoyaltyMockServer legacyLoyaltyMockServer;

  private static final String S3_BUCKET_NAME = "old-loyalty-transactions";

  @BeforeEach
  void setUp() {
    legacyLoyaltyMockServer = new LegacyLoyaltyMockServer();
    amazonS3.createBucket(S3_BUCKET_NAME);
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    jobRepositoryTestUtils.removeJobExecutions();
    legacyLoyaltyMockServer.stop();
    amazonS3.listObjectsV2(S3_BUCKET_NAME)
        .getObjectSummaries()
        .stream()
        .map(S3ObjectSummary::getKey)
        .forEach(key -> amazonS3.deleteObject(S3_BUCKET_NAME, key));
    amazonS3.deleteBucket(S3_BUCKET_NAME);
  }

  @Test
  void shouldCreateUsers_whenExecute_givenLoyaltyTransactionAndLegacyLoyaltyUserExist() throws Exception {
    // Given
    final String yesterday = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .format(LocalDateTime.now().minusDays(1));

    final File file = ResourceUtils.getFile("classpath:loyalty-transactions/Loyalty_Transactions_2.csv");
    final ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.length());

    amazonS3.putObject(S3_BUCKET_NAME, yesterday + "/Loyalty_Transactions_2.csv",
        new ByteArrayInputStream(FileUtils.readFileToByteArray(file)), objectMetadata);

    LegacyLoyaltyUser user = new LegacyLoyaltyUser("b724424a",
        "John", "Doe", "john.doe@example.com", "+6678901234", true);

    LegacyLoyaltyUser anotherUser = new LegacyLoyaltyUser("4bf594e4",
        "Raksit", "Man", "raksit.man@example.com", "+6678904321", true);

    legacyLoyaltyMockServer.addHappyPathExpectations(user);
    legacyLoyaltyMockServer.addHappyPathExpectations(anotherUser);

    // When
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters());
    Optional<User> updatedUser = userRepository.findByEmail("john.doe@example.com");
    Optional<User> updatedAnotherUser = userRepository.findByEmail("raksit.man@example.com");

    // Then
    assertThat(jobExecution.getJobInstance().getJobName(), equalTo("migrateLegacyLoyalty"));
    assertThat(jobExecution.getExitStatus(), equalTo(ExitStatus.COMPLETED));
    assertThat(updatedUser.isPresent(), equalTo(true));
    assertThat(updatedUser.get().getPoints(), equalTo(100L));
    assertThat(updatedAnotherUser.isPresent(), equalTo(true));
    assertThat(updatedAnotherUser.get().getPoints(), equalTo(200L));
  }

  @Test
  void shouldSkipFailedUser_whenExecute_givenSystemCannotUpdatePoints() throws Exception {
    // Given
    final String yesterday = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .format(LocalDateTime.now().minusDays(1));

    final File file = ResourceUtils.getFile("classpath:loyalty-transactions/Loyalty_Transactions_2.csv");
    final ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.length());

    amazonS3.putObject(S3_BUCKET_NAME, yesterday + "/Loyalty_Transactions_2.csv",
        new ByteArrayInputStream(FileUtils.readFileToByteArray(file)), objectMetadata);

    LegacyLoyaltyUser user = new LegacyLoyaltyUser("b724424a",
        "John", "Doe", "john.doe@example.com", "+6678901234", true);

    LegacyLoyaltyUser anotherUser = new LegacyLoyaltyUser("4bf594e4",
        "Raksit", "Man", "raksit.man@example.com", "+6678904321", true);

    legacyLoyaltyMockServer.addUnhappyPathExpectations(user.getId());
    legacyLoyaltyMockServer.addHappyPathExpectations(anotherUser);

    // When
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters());
    Optional<User> updatedUser = userRepository.findByEmail("john.doe@example.com");
    Optional<User> updatedAnotherUser = userRepository.findByEmail("raksit.man@example.com");

    // Then
    assertThat(jobExecution.getJobInstance().getJobName(), equalTo("migrateLegacyLoyalty"));
    assertThat(jobExecution.getExitStatus(), equalTo(ExitStatus.COMPLETED));
    assertThat(updatedUser.isPresent(), equalTo(false));
    assertThat(updatedAnotherUser.isPresent(), equalTo(true));
    assertThat(updatedAnotherUser.get().getPoints(), equalTo(200L));
  }

  @TestConfiguration
  static class AmazonS3ClientTestConfiguration {

    @Primary
    @Bean
    public AmazonS3 amazonS3() {
      return AmazonS3ClientBuilder.standard()
          .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(Service.S3))
          .withCredentials(localStackContainer.getDefaultCredentialsProvider())
          .build();
    }
  }
}
