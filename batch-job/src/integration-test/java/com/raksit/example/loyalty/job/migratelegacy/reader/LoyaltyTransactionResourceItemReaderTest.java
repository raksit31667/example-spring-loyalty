package com.raksit.example.loyalty.job.migratelegacy.reader;

import static com.raksit.example.loyalty.extension.AmazonS3ClientExtension.localStackContainer;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.extension.AmazonS3ClientExtension;
import com.raksit.example.loyalty.job.migratelegacy.legacy.LoyaltyTransaction;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

@ExtendWith(AmazonS3ClientExtension.class)
@ActiveProfiles("migrate-legacy-loyalty")
@IntegrationTest
class LoyaltyTransactionResourceItemReaderTest {

  @Autowired
  private AmazonS3 amazonS3ForLegacyLoyaltyJob;

  @Autowired
  private MultiResourceItemReader<LoyaltyTransaction> loyaltyTransactionMultiResourceItemReader;

  private static final String S3_BUCKET_NAME = "old-loyalty-transactions";

  @BeforeEach
  void setUp() {
    amazonS3ForLegacyLoyaltyJob.createBucket(S3_BUCKET_NAME);
  }

  @AfterEach
  void tearDown() {
    amazonS3ForLegacyLoyaltyJob.listObjectsV2(S3_BUCKET_NAME)
        .getObjectSummaries()
        .stream()
        .map(S3ObjectSummary::getKey)
        .forEach(key -> amazonS3ForLegacyLoyaltyJob.deleteObject(S3_BUCKET_NAME, key));
    amazonS3ForLegacyLoyaltyJob.deleteBucket(S3_BUCKET_NAME);
  }

  @Test
  void shouldReturnAllLoyaltyTransactions_whenCallReader_givenLoyaltyTransactionCsvFilesExistInS3()
      throws Exception {
    // Given
    final String yesterday = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .format(LocalDateTime.now().minusDays(1));

    final File file = ResourceUtils.getFile(
        "classpath:loyalty-transactions/Loyalty_Transactions_1.csv");
    final ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.length());

    amazonS3ForLegacyLoyaltyJob.putObject(S3_BUCKET_NAME, yesterday + "/Loyalty_Transactions_1.csv",
        new ByteArrayInputStream(FileUtils.readFileToByteArray(file)), objectMetadata);
    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();

    // When
    StepScopeTestUtils.doInStepScope(stepExecution, () -> {
      loyaltyTransactionMultiResourceItemReader.open(stepExecution.getExecutionContext());
      // Then
      LoyaltyTransaction loyaltyTransaction = loyaltyTransactionMultiResourceItemReader.read();
      assertThat(loyaltyTransaction, notNullValue());
      assertThat(loyaltyTransaction.getMemberId(), equalTo("b724424a"));
      assertThat(loyaltyTransaction.getPoints(), equalTo(100L));
      loyaltyTransactionMultiResourceItemReader.close();
      return null;
    });
  }

  @Test
  void shouldReturnOneLoyaltyTransaction_whenCallReader_givenLoyaltyTransactionCsvFilesExistInS3InMigrationDate()
      throws Exception {
    // Given
    final String yesterday = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .format(LocalDateTime.now().minusDays(1));

    final File file = ResourceUtils.getFile(
        "classpath:loyalty-transactions/Loyalty_Transactions_1.csv");
    final ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.length());

    amazonS3ForLegacyLoyaltyJob.putObject(S3_BUCKET_NAME,  "1970-01-01/Loyalty_Transactions_1.csv",
        new ByteArrayInputStream(FileUtils.readFileToByteArray(file)), objectMetadata);
    amazonS3ForLegacyLoyaltyJob.putObject(S3_BUCKET_NAME, yesterday + "/Loyalty_Transactions_1.csv",
        new ByteArrayInputStream(FileUtils.readFileToByteArray(file)), objectMetadata);
    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(
        new JobParametersBuilder().addString("migrationDate", "1970-01-01", false)
            .toJobParameters());

    // When
    StepScopeTestUtils.doInStepScope(stepExecution, () -> {
      loyaltyTransactionMultiResourceItemReader.open(stepExecution.getExecutionContext());
      // Then
      LoyaltyTransaction loyaltyTransaction = loyaltyTransactionMultiResourceItemReader.read();
      assertThat(loyaltyTransaction, notNullValue());
      assertThat(loyaltyTransaction.getMemberId(), equalTo("b724424a"));
      assertThat(loyaltyTransaction.getPoints(), equalTo(100L));
      LoyaltyTransaction nullLoyaltyTransaction = loyaltyTransactionMultiResourceItemReader.read();
      assertThat(nullLoyaltyTransaction, nullValue());
      loyaltyTransactionMultiResourceItemReader.close();
      return null;
    });
  }

  @TestConfiguration
  static class AmazonS3ClientTestConfiguration {

    @Primary
    @Bean
    public AmazonS3 amazonS3ForLegacyLoyaltyJob() {
      return AmazonS3ClientBuilder.standard()
          .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(Service.S3))
          .withCredentials(localStackContainer.getDefaultCredentialsProvider())
          .build();
    }
  }
}
