package com.raksit.example.loyalty.activity.consumer;

import com.raksit.example.loyalty.activity.config.ActivityConfigurationProperties;
import com.raksit.example.loyalty.activity.event.ActivityPerformed;
import com.raksit.example.loyalty.transaction.entity.Transaction;
import com.raksit.example.loyalty.transaction.repository.TransactionRepository;
import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
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

  public ActivityPerformedConsumer(UserRepository userRepository,
      TransactionRepository transactionRepository,
      ActivityConfigurationProperties activityConfigurationProperties) {
    this.userRepository = userRepository;
    this.transactionRepository = transactionRepository;
    this.activityConfigurationProperties = activityConfigurationProperties;
  }

  @Override
  public void accept(ActivityPerformed activityPerformed) {
    log.info("Activity occurred event for user {} received.", activityPerformed.getActivity().getUserId());

    final Optional<User> user = userRepository.findById(
        UUID.fromString(activityPerformed.getActivity().getUserId()));

    if (user.isEmpty()) {
      return;
    }

    final Optional<Long> points = activityConfigurationProperties.getPoints(
        activityPerformed.getActivity().getId());

    if (points.isEmpty()) {
      return;
    }

    transactionRepository.save(new Transaction(user.get(), points.get(),
        Instant.ofEpochSecond(activityPerformed.getPerformedOn())));
  }
}
