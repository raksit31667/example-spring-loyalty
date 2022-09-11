package com.raksit.example.loyalty.job.subscriptionimport.step;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.raksit.example.loyalty.job.configuration.AmazonS3ConfigurationProperties;
import com.raksit.example.loyalty.job.subscriptionimport.invoice.Invoice;
import com.raksit.example.loyalty.job.subscriptionimport.processor.InvoiceToUserSubscriptionProcessor;
import com.raksit.example.loyalty.job.subscriptionimport.subscription.entity.UserSubscription;
import com.raksit.example.loyalty.job.subscriptionimport.subscription.repository.UserSubscriptionRepository;
import io.awspring.cloud.core.io.s3.PathMatchingSimpleStorageResourcePatternResolver;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

@Configuration
@Profile("subscription-import")
public class InvoiceToUserSubscriptionConversionStepConfiguration {

  private static final int CHUNK_SIZE = 1000;

  @StepScope
  @Bean
  public MultiResourceItemReader<Invoice> invoiceMultiResourceItemReader(
      AmazonS3 amazonS3ForUserSubscriptionImportJob,
      ApplicationContext applicationContext,
      AmazonS3ConfigurationProperties amazonS3ConfigurationProperties) {

    Resource[] resources = amazonS3ForUserSubscriptionImportJob
        .listObjectsV2(amazonS3ConfigurationProperties.getBucketName())
        .getObjectSummaries().stream()
        .map(s3ObjectSummary -> s3ResourcePatternResolver(amazonS3ForUserSubscriptionImportJob, applicationContext)
            .getResource(getS3ResourceUrl(s3ObjectSummary, amazonS3ConfigurationProperties)))
        .toArray(Resource[]::new);
    return new MultiResourceItemReaderBuilder<Invoice>()
        .name("invoiceMultiResourceItemReader")
        .resources(resources)
        .delegate(invoiceFlatFileItemReader())
        .build();
  }

  private FlatFileItemReader<Invoice> invoiceFlatFileItemReader() {
    return new FlatFileItemReaderBuilder<Invoice>()
        .name("invoiceFlatFileItemReader")
        .linesToSkip(1)
        .lineMapper(new DefaultLineMapper<>() {
          {
            setLineTokenizer(new DelimitedLineTokenizer() {
              {
                setNames("userId", "productId", "productName", "issuedDate");
              }
            });

            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
              {
                setTargetType(Invoice.class);
              }
            });
          }
        })
        .build();
  }

  private ResourcePatternResolver s3ResourcePatternResolver(
      AmazonS3 amazonS3ForUserSubscriptionImportJob,
      ApplicationContext applicationContext) {

    return new PathMatchingSimpleStorageResourcePatternResolver(
        amazonS3ForUserSubscriptionImportJob, applicationContext);
  }

  private String getS3ResourceUrl(
      S3ObjectSummary s3ObjectSummary,
      AmazonS3ConfigurationProperties amazonS3ConfigurationProperties) {

    return String.format("s3://%s/%s",
        amazonS3ConfigurationProperties.getBucketName(), s3ObjectSummary.getKey());
  }

  @Bean
  public RepositoryItemWriter<UserSubscription> userSubscriptionRepositoryItemWriter(
      UserSubscriptionRepository userSubscriptionRepository) {

    return new RepositoryItemWriterBuilder<UserSubscription>()
        .repository(userSubscriptionRepository)
        .build();
  }

  @Bean
  public Step invoiceToUserSubscriptionConversionStep(
      AmazonS3 amazonS3ForLegacyLoyaltyJob,
      ApplicationContext applicationContext,
      AmazonS3ConfigurationProperties amazonS3ConfigurationProperties,
      UserSubscriptionRepository userSubscriptionRepository,
      StepBuilderFactory stepBuilderFactory) {

    return stepBuilderFactory
        .get("invoiceToUserSubscriptionConversionStep")
        .<Invoice, UserSubscription>chunk(CHUNK_SIZE)
        .reader(invoiceMultiResourceItemReader(
            amazonS3ForLegacyLoyaltyJob,
            applicationContext,
            amazonS3ConfigurationProperties))
        .processor(new InvoiceToUserSubscriptionProcessor())
        .writer(userSubscriptionRepositoryItemWriter(userSubscriptionRepository))
        .faultTolerant()
        .skipPolicy(new AlwaysSkipItemSkipPolicy())
        .allowStartIfComplete(true)
        .build();
  }
}
