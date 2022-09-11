package com.raksit.example.loyalty.job.subscriptionimport.step;

import com.raksit.example.loyalty.job.subscriptionimport.subscription.repository.UserSubscriptionRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Configuration
@Profile("subscription-import")
public class UserSubscriptionCleanupStepConfiguration {

  @Bean
  public Step cleanupUserSubscriptionStep(
      StepBuilderFactory stepBuilderFactory,
      UserSubscriptionRepository userSubscriptionRepository) {
    return stepBuilderFactory.get("cleanupUserSubscriptionStep")
        .tasklet((contribution, chunkContext) -> {
          userSubscriptionRepository.deleteAllByActiveUntilNot(
              Instant.now().truncatedTo(ChronoUnit.DAYS));
          return RepeatStatus.FINISHED;
        })
        .allowStartIfComplete(true)
        .build();
  }
}
