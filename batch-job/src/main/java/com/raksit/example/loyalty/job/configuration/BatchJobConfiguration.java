package com.raksit.example.loyalty.job.configuration;

import com.raksit.example.loyalty.job.listener.UserRepositoryItemReadLoggerListener;
import com.raksit.example.loyalty.job.processor.LoyaltyUserItemProcessor;
import com.raksit.example.loyalty.legacy.LegacyLoyaltyClient;
import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
@EnableBatchProcessing
public class BatchJobConfiguration {

  private final UserRepository userRepository;

  private final LegacyLoyaltyClient legacyLoyaltyClient;

  private final StepBuilderFactory stepBuilderFactory;

  private final JobBuilderFactory jobBuilderFactory;

  public BatchJobConfiguration(UserRepository userRepository,
      LegacyLoyaltyClient legacyLoyaltyClient,
      StepBuilderFactory stepBuilderFactory,
      JobBuilderFactory jobBuilderFactory) {
    this.userRepository = userRepository;
    this.legacyLoyaltyClient = legacyLoyaltyClient;
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

  @Bean
  public LoyaltyUserItemProcessor loyaltyUserItemProcessor() {
    return new LoyaltyUserItemProcessor(legacyLoyaltyClient);
  }

  @Bean
  public RepositoryItemWriter<User> userRepositoryItemWriter() {
    RepositoryItemWriter<User> repositoryItemWriter = new RepositoryItemWriter<>();
    repositoryItemWriter.setRepository(userRepository);
    repositoryItemWriter.setMethodName("save");
    return repositoryItemWriter;
  }

  @Bean
  public Step step(RepositoryItemReader<User> userRepositoryItemReader,
      LoyaltyUserItemProcessor loyaltyUserItemProcessor,
      RepositoryItemWriter<User> userRepositoryItemWriter) {
    return stepBuilderFactory
        .get("step1")
        .<User, User>chunk(3)
        .reader(userRepositoryItemReader)
        .processor(loyaltyUserItemProcessor)
        .writer(userRepositoryItemWriter)
        .listener(new UserRepositoryItemReadLoggerListener())
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
        .build();
  }
}
