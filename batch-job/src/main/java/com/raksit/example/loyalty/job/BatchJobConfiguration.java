package com.raksit.example.loyalty.job;

import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
@EnableBatchProcessing
public class BatchJobConfiguration {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

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
  public Step step1(RepositoryItemReader<User> userRepositoryItemReader) {
    return stepBuilderFactory
        .get("step1")
        .chunk(2)
        .reader(userRepositoryItemReader)
        .writer(new ListItemWriter<>())
        .build();
  }

  @Bean
  public Job migrateLegacyLoyalty(Step step1) {
    return jobBuilderFactory
        .get("migrateLegacyLoyalty")
        .flow(step1)
        .end()
        .build();
  }
}
