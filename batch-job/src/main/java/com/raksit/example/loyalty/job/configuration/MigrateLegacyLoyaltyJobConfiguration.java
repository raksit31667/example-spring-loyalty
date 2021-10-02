package com.raksit.example.loyalty.job.configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.raksit.example.loyalty.job.listener.LoyaltyUserItemProcessLoggerListener;
import com.raksit.example.loyalty.job.listener.MigrateLegacyLoyaltyJobLoggerListener;
import com.raksit.example.loyalty.job.listener.MigrateLegacyLoyaltyStepLoggerListener;
import com.raksit.example.loyalty.job.listener.UserRepositoryItemReadLoggerListener;
import com.raksit.example.loyalty.job.listener.UserRepositoryItemWriteLoggerListener;
import com.raksit.example.loyalty.job.processor.LoyaltyUserItemProcessor;
import com.raksit.example.loyalty.legacy.LegacyLoyaltyClient;
import com.raksit.example.loyalty.legacy.LoyaltyTransaction;
import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
import io.awspring.cloud.core.io.s3.PathMatchingSimpleStorageResourcePatternResolver;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.HashMap;

@Configuration
@EnableBatchProcessing
public class MigrateLegacyLoyaltyJobConfiguration {

  private final UserRepository userRepository;

  private final LegacyLoyaltyClient legacyLoyaltyClient;

  private final AmazonS3 amazonS3;

  private final ApplicationContext applicationContext;

  private final AmazonS3ConfigurationProperties amazonS3ConfigurationProperties;

  private final StepBuilderFactory stepBuilderFactory;

  private final JobBuilderFactory jobBuilderFactory;

  public MigrateLegacyLoyaltyJobConfiguration(UserRepository userRepository,
      LegacyLoyaltyClient legacyLoyaltyClient,
      AmazonS3 amazonS3,
      ApplicationContext applicationContext,
      AmazonS3ConfigurationProperties amazonS3ConfigurationProperties,
      StepBuilderFactory stepBuilderFactory,
      JobBuilderFactory jobBuilderFactory) {
    this.userRepository = userRepository;
    this.legacyLoyaltyClient = legacyLoyaltyClient;
    this.amazonS3 = amazonS3;
    this.applicationContext = applicationContext;
    this.amazonS3ConfigurationProperties = amazonS3ConfigurationProperties;
    this.stepBuilderFactory = stepBuilderFactory;
    this.jobBuilderFactory = jobBuilderFactory;
  }

  @Bean
  public RepositoryItemReader<User> userRepositoryItemReader() {
    RepositoryItemReader<User> repositoryItemReader = new RepositoryItemReader<>();
    repositoryItemReader.setRepository(userRepository);
    repositoryItemReader.setMethodName("findAll");
    repositoryItemReader.setPageSize(1);
    repositoryItemReader.setSort(new HashMap<>());
    return repositoryItemReader;
  }

  @StepScope
  @Bean
  public MultiResourceItemReader<LoyaltyTransaction> loyaltyTransactionMultiResourceItemReader() {
    Resource[] resources = amazonS3.listObjectsV2(amazonS3ConfigurationProperties.getBucketName())
        .getObjectSummaries().stream()
        .map(s3ObjectSummary -> s3ResourcePatternResolver().getResource(getS3ResourceUrl(s3ObjectSummary)))
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

  private ResourcePatternResolver s3ResourcePatternResolver() {
    return new PathMatchingSimpleStorageResourcePatternResolver(amazonS3, applicationContext);
  }

  private String getS3ResourceUrl(S3ObjectSummary s3ObjectSummary) {
    return String.format("s3://%s/%s", amazonS3ConfigurationProperties.getBucketName(), s3ObjectSummary.getKey());
  }

  @Bean
  public LoyaltyUserItemProcessor loyaltyUserItemProcessor() {
    return new LoyaltyUserItemProcessor(legacyLoyaltyClient);
  }

  @Bean
  public RepositoryItemWriter<User> userRepositoryItemWriter() {
    RepositoryItemWriter<User> repositoryItemWriter = new RepositoryItemWriter<>();
    repositoryItemWriter.setRepository(userRepository);
    return repositoryItemWriter;
  }

  @Bean
  public Step step() {
    return stepBuilderFactory
        .get("step")
        .<User, User>chunk(3)
        .reader(userRepositoryItemReader())
        .processor(loyaltyUserItemProcessor())
        .writer(userRepositoryItemWriter())
        .listener(new MigrateLegacyLoyaltyStepLoggerListener())
        .listener(new UserRepositoryItemReadLoggerListener())
        .listener(new LoyaltyUserItemProcessLoggerListener())
        .listener(new UserRepositoryItemWriteLoggerListener())
        .faultTolerant()
        .skipPolicy(new AlwaysSkipItemSkipPolicy())
        .build();
  }

  @Bean
  public Job migrateLegacyLoyalty(Step step) {
    return jobBuilderFactory
        .get("migrateLegacyLoyalty")
        .flow(step)
        .end()
        .listener(new MigrateLegacyLoyaltyJobLoggerListener())
        .build();
  }
}
