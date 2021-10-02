package com.raksit.example.loyalty.reader;

import static com.raksit.example.loyalty.extension.AmazonS3ClientExtension.localStackContainer;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.extension.AmazonS3ClientExtension;
import com.raksit.example.loyalty.legacy.LoyaltyTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ResourceUtils;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;

@ExtendWith(AmazonS3ClientExtension.class)
@IntegrationTest
public class LoyaltyTransactionResourceItemReaderTest {

  @Autowired
  private AmazonS3 amazonS3;

  @Autowired
  private MultiResourceItemReader<LoyaltyTransaction> loyaltyTransactionMultiResourceItemReader;

  private static final String S3_BUCKET_NAME = "old-loyalty-transactions";

  @BeforeEach
  void setUp() {
    amazonS3.createBucket(S3_BUCKET_NAME);
  }

  @AfterEach
  void tearDown() {
    amazonS3.listObjectsV2(S3_BUCKET_NAME)
        .getObjectSummaries()
        .stream()
        .map(S3ObjectSummary::getKey)
        .forEach(key -> amazonS3.deleteObject(S3_BUCKET_NAME, key));
    amazonS3.deleteBucket(S3_BUCKET_NAME);
  }

  @Test
  void shouldReturnAllLoyaltyTransactions_whenCallReader_givenLoyaltyTransactionCsvFilesExistInS3() throws Exception {
    // Given
    amazonS3.putObject(S3_BUCKET_NAME, "Loyalty_Transactions_1.csv",
        ResourceUtils.getFile("classpath:loyalty-transactions/Loyalty_Transactions_1.csv"));
    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();

    // When
    StepScopeTestUtils.doInStepScope(stepExecution, () -> {
      LoyaltyTransaction loyaltyTransaction;
      loyaltyTransactionMultiResourceItemReader.open(stepExecution.getExecutionContext());
      while ((loyaltyTransaction = loyaltyTransactionMultiResourceItemReader.read()) != null) {
        // Then
        assertThat(loyaltyTransaction.getMemberId(), equalTo("b724424a"));
        assertThat(loyaltyTransaction.getPoints(), equalTo(100L));
      }
      loyaltyTransactionMultiResourceItemReader.close();
      return null;
    });
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
