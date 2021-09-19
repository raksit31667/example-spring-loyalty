package com.raksit.example.loyalty.job.listener;

import com.raksit.example.loyalty.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;

@Slf4j
public class LoyaltyUserItemProcessLoggerListener implements ItemProcessListener<User, User> {

  @Override
  public void beforeProcess(User user) {
    log.info("Fetching user {} old loyalty points from legacy system", user.getId());
  }

  @Override
  public void afterProcess(User user, User result) {
    log.info("Fetched successfully: user {} has {} old loyalty points", user.getId(), result.getPoints());
  }

  @Override
  public void onProcessError(User user, Exception exception) {
    log.error("Fetched failed: unexpected error occurred while fetching user {}", user.getId(), exception);
  }
}
