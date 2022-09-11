package com.raksit.example.loyalty.job.subscriptionimport;

import com.raksit.example.loyalty.job.migratelegacy.listener.MigrateLegacyLoyaltyJobLoggerListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableBatchProcessing
@Profile("subscription-import")
public class UserSubscriptionImportJobConfiguration {

  @Bean
  public Job userSubscriptionImport(
      Step invoiceToUserSubscriptionConversionStep,
      Step archiveExpiredUserSubscriptionStep,
      Step cleanupUserSubscriptionStep,
      Step refreshUserSubscriptionCountStep,
      JobBuilderFactory jobBuilderFactory) {
    return jobBuilderFactory
        .get("userSubscriptionImport")
        .start(invoiceToUserSubscriptionConversionStep)
        .next(archiveExpiredUserSubscriptionStep)
        .next(cleanupUserSubscriptionStep)
        .next(refreshUserSubscriptionCountStep)
        .listener(new MigrateLegacyLoyaltyJobLoggerListener())
        .build();
  }
}
