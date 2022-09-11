package com.raksit.example.loyalty.job.subscriptionimport.step;

import com.raksit.example.loyalty.job.subscriptionimport.subscription.repository.UserSubscriptionCountRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("subscription-import")
public class UserSubscriptionCountRefreshStepConfiguration {

  @Bean
  public Step refreshUserSubscriptionCountStep(
      StepBuilderFactory stepBuilderFactory,
      UserSubscriptionCountRepository userSubscriptionCountRepository) {
    return stepBuilderFactory.get("refreshUserSubscriptionCountStep")
        .tasklet((contribution, chunkContext) -> {
          userSubscriptionCountRepository.refresh();
          return RepeatStatus.FINISHED;
        })
        .allowStartIfComplete(true)
        .build();
  }
}
