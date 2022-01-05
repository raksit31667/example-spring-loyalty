package com.raksit.example.loyalty.activity.consumer;

import com.raksit.example.loyalty.activity.config.ActivityConfigurationProperties;
import com.raksit.example.loyalty.activity.event.ActivityPerformed;
import com.raksit.example.loyalty.metric.LoyaltyPointEarnedMetrics;
import com.raksit.example.loyalty.transaction.entity.Transaction;
import com.raksit.example.loyalty.transaction.repository.TransactionRepository;
import com.raksit.example.loyalty.user.entity.User;
import com.raksit.example.loyalty.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Component("activity-performed-consumer")
public class ActivityPerformedConsumer implements Consumer<ActivityPerformed> {

  private final UserRepository userRepository;

  private final TransactionRepository transactionRepository;

  private final ActivityConfigurationProperties activityConfigurationProperties;

  private final LoyaltyPointEarnedMetrics loyaltyPointEarnedMetrics;

  public ActivityPerformedConsumer(UserRepository userRepository,
      TransactionRepository transactionRepository,
      ActivityConfigurationProperties activityConfigurationProperties,
      LoyaltyPointEarnedMetrics loyaltyPointEarnedMetrics) {
    this.userRepository = userRepository;
    this.transactionRepository = transactionRepository;
    this.activityConfigurationProperties = activityConfigurationProperties;
    this.loyaltyPointEarnedMetrics = loyaltyPointEarnedMetrics;
  }

  @Override
  public void accept(ActivityPerformed activityPerformed) {
    log.info("Activity occurred event for user {} received.", activityPerformed.getActivity().getUserId());

    final Optional<User> user = userRepository.findById(
        UUID.fromString(activityPerformed.getActivity().getUserId()));

    if (user.isEmpty()) {
      log.info("Process skipped: user {} is not in loyalty program", activityPerformed.getActivity().getUserId());
      return;
    }

    loyaltyPointEarnedMetrics.initializeMetric(user.get().getId().toString(), activityPerformed.getActivity().getId());

    final Optional<Long> points = activityConfigurationProperties.getPoints(
        activityPerformed.getActivity().getId());

    if (points.isEmpty()) {
      log.info("Process skipped: user {} completed activity {} which is not in loyalty program",
          activityPerformed.getActivity().getUserId(), activityPerformed.getActivity().getId());
      return;
    }

    transactionRepository.save(new Transaction(user.get(), points.get(),
        Instant.ofEpochSecond(activityPerformed.getPerformedOn())));

    log.info("Process completed: user {} earned {} points", user.get().getId(), points.get());
    loyaltyPointEarnedMetrics.incrementMetric(user.get().getId().toString(),
        activityPerformed.getActivity().getId(), points.get());
  }
}
