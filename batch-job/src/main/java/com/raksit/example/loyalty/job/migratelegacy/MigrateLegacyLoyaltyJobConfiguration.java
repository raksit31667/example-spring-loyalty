package com.raksit.example.loyalty.job.migratelegacy;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.raksit.example.loyalty.job.configuration.AmazonS3ConfigurationProperties;
import com.raksit.example.loyalty.job.migratelegacy.legacy.LegacyLoyaltyClient;
import com.raksit.example.loyalty.job.migratelegacy.legacy.LoyaltyTransaction;
import com.raksit.example.loyalty.job.migratelegacy.listener.LoyaltyUserItemProcessLoggerListener;
import com.raksit.example.loyalty.job.migratelegacy.listener.MigrateLegacyLoyaltyJobLoggerListener;
import com.raksit.example.loyalty.job.migratelegacy.listener.MigrateLegacyLoyaltyStepLoggerListener;
import com.raksit.example.loyalty.job.migratelegacy.listener.UserRepositoryItemReadLoggerListener;
import com.raksit.example.loyalty.job.migratelegacy.listener.UserRepositoryItemWriteLoggerListener;
import com.raksit.example.loyalty.job.migratelegacy.processor.LoyaltyUserItemProcessor;
import com.raksit.example.loyalty.job.migratelegacy.user.entity.User;
import com.raksit.example.loyalty.job.migratelegacy.user.repository.UserRepository;
import com.raksit.example.loyalty.job.migratelegacy.writer.ActiveUserRepositoryItemWriter;
import com.raksit.example.loyalty.job.migratelegacy.writer.InactiveUserRepositoryItemWriter;
import io.awspring.cloud.core.io.s3.PathMatchingSimpleStorageResourcePatternResolver;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Configuration
@EnableBatchProcessing
public class MigrateLegacyLoyaltyJobConfiguration {

  @StepScope
  @Bean
  public MultiResourceItemReader<LoyaltyTransaction> loyaltyTransactionMultiResourceItemReader(
      AmazonS3 amazonS3ForLegacyLoyaltyJob,
      ApplicationContext applicationContext,
      AmazonS3ConfigurationProperties amazonS3ConfigurationProperties,
      @Value("#{jobParameters['migrationDate']}") String migrationDate) {

    Resource[] resources = amazonS3ForLegacyLoyaltyJob.listObjectsV2(amazonS3ConfigurationProperties.getBucketName(),
            Optional.ofNullable(migrationDate).orElseGet(this::getYesterdayFormattedDate))
        .getObjectSummaries().stream()
        .map(s3ObjectSummary -> s3ResourcePatternResolver(amazonS3ForLegacyLoyaltyJob, applicationContext)
            .getResource(getS3ResourceUrl(s3ObjectSummary, amazonS3ConfigurationProperties)))
        .toArray(Resource[]::new);
    return new MultiResourceItemReaderBuilder<LoyaltyTransaction>()
        .name("loyaltyTransactionMultiResourceItemReader")
        .resources(resources)
        .delegate(loyaltyTransactionFlatFileItemReader())
        .build();
  }

  private FlatFileItemReader<LoyaltyTransaction> loyaltyTransactionFlatFileItemReader() {
    return new FlatFileItemReaderBuilder<LoyaltyTransaction>()
        .name("loyaltyTransactionFlatFileItemReader")
        .linesToSkip(1)
        .lineMapper(new DefaultLineMapper<>() {
          {
            setLineTokenizer(new DelimitedLineTokenizer() {
              {
                setNames("memberId", "points");
              }
            });

            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
              {
                setTargetType(LoyaltyTransaction.class);
              }
            });
          }
        })
        .build();
  }

  private ResourcePatternResolver s3ResourcePatternResolver(
      AmazonS3 amazonS3ForLegacyLoyaltyJob,
      ApplicationContext applicationContext) {

    return new PathMatchingSimpleStorageResourcePatternResolver(amazonS3ForLegacyLoyaltyJob, applicationContext);
  }

  private String getYesterdayFormattedDate() {
    return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now().minusDays(1));
  }

  private String getS3ResourceUrl(
      S3ObjectSummary s3ObjectSummary,
      AmazonS3ConfigurationProperties amazonS3ConfigurationProperties) {

    return String.format("s3://%s/%s",
        amazonS3ConfigurationProperties.getBucketName(), s3ObjectSummary.getKey());
  }

  @Bean
  public LoyaltyUserItemProcessor loyaltyUserItemProcessor(LegacyLoyaltyClient legacyLoyaltyClient) {
    return new LoyaltyUserItemProcessor(legacyLoyaltyClient);
  }

  @Bean
  public CompositeItemWriter<User> userCompositeItemWriter(UserRepository userRepository) {
    return new CompositeItemWriterBuilder<User>()
        .delegates(new InactiveUserRepositoryItemWriter(userRepository),
            new ActiveUserRepositoryItemWriter(userRepository))
        .build();
  }

  @Bean
  public Step step(
      UserRepository userRepository,
      LegacyLoyaltyClient legacyLoyaltyClient,
      AmazonS3 amazonS3ForLegacyLoyaltyJob,
      ApplicationContext applicationContext,
      AmazonS3ConfigurationProperties amazonS3ConfigurationProperties,
      StepBuilderFactory stepBuilderFactory) {

    return stepBuilderFactory
        .get("step")
        .<LoyaltyTransaction, User>chunk(3)
        .reader(loyaltyTransactionMultiResourceItemReader(
            amazonS3ForLegacyLoyaltyJob,
            applicationContext,
            amazonS3ConfigurationProperties,
            null
        ))
        .processor(loyaltyUserItemProcessor(legacyLoyaltyClient))
        .writer(userCompositeItemWriter(userRepository))
        .listener(new MigrateLegacyLoyaltyStepLoggerListener())
        .listener(new UserRepositoryItemReadLoggerListener())
        .listener(new LoyaltyUserItemProcessLoggerListener())
        .listener(new UserRepositoryItemWriteLoggerListener())
        .faultTolerant()
        .skipPolicy(new AlwaysSkipItemSkipPolicy())
        .allowStartIfComplete(true)
        .build();
  }

  @Bean
  public Job migrateLegacyLoyalty(Step step, JobBuilderFactory jobBuilderFactory) {
    return jobBuilderFactory
        .get("migrateLegacyLoyalty")
        .flow(step)
        .end()
        .listener(new MigrateLegacyLoyaltyJobLoggerListener())
        .build();
  }
}
