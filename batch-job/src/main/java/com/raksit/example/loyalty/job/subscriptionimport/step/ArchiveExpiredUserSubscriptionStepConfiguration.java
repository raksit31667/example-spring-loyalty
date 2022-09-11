package com.raksit.example.loyalty.job.subscriptionimport.step;

import com.raksit.example.loyalty.job.subscriptionimport.processor.UserSubscriptionToExpiredUserSubscriptionProcessor;
import com.raksit.example.loyalty.job.subscriptionimport.subscription.entity.ExpiredUserSubscription;
import com.raksit.example.loyalty.job.subscriptionimport.subscription.entity.UserSubscription;
import com.raksit.example.loyalty.job.subscriptionimport.subscription.repository.ExpiredUserSubscriptionRepository;
import com.raksit.example.loyalty.job.subscriptionimport.subscription.repository.UserSubscriptionRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableBatchProcessing
@Profile("subscription-import")
public class ArchiveExpiredUserSubscriptionStepConfiguration {

  private static final int PAGE_SIZE = 1000;
  private static final int CHUNK_SIZE = 1000;

  @Bean
  public RepositoryItemReader<UserSubscription> userSubscriptionRepositoryItemReader(
      UserSubscriptionRepository userSubscriptionRepository) {
    return new RepositoryItemReaderBuilder<UserSubscription>()
        .name("userSubscriptionRepositoryItemReader")
        .repository(userSubscriptionRepository)
        .pageSize(PAGE_SIZE)
        .sorts(Collections.emptyMap())
        .methodName("findAllByActiveUntilLessThan")
        .arguments(List.of(Instant.now().truncatedTo(ChronoUnit.DAYS)))
        .build();
  }

  @Bean
  public RepositoryItemWriter<ExpiredUserSubscription> expiredUserSubscriptionRepositoryItemWriter(
      ExpiredUserSubscriptionRepository expiredUserSubscriptionRepository) {
    return new RepositoryItemWriterBuilder<ExpiredUserSubscription>()
        .repository(expiredUserSubscriptionRepository)
        .build();
  }

  @Bean
  public Step archiveExpiredUserSubscriptionStep(
      StepBuilderFactory stepBuilderFactory,
      UserSubscriptionRepository userSubscriptionRepository,
      ExpiredUserSubscriptionRepository expiredUserSubscriptionRepository) {

    return stepBuilderFactory
        .get("archiveExpiredUserSubscriptionStep")
        .<UserSubscription, ExpiredUserSubscription>chunk(CHUNK_SIZE)
        .reader(userSubscriptionRepositoryItemReader(userSubscriptionRepository))
        .processor(new UserSubscriptionToExpiredUserSubscriptionProcessor())
        .writer(expiredUserSubscriptionRepositoryItemWriter(expiredUserSubscriptionRepository))
        .faultTolerant()
        .skipPolicy(new AlwaysSkipItemSkipPolicy())
        .allowStartIfComplete(true)
        .build();
  }
}
