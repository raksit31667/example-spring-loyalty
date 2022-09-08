package com.raksit.example.loyalty.job.migratelegacy.listener;

import com.raksit.example.loyalty.job.migratelegacy.legacy.LoyaltyTransaction;
import com.raksit.example.loyalty.job.migratelegacy.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;

@Slf4j
public class LoyaltyUserItemProcessLoggerListener implements ItemProcessListener<LoyaltyTransaction, User> {

  @Override
  public void beforeProcess(LoyaltyTransaction loyaltyTransaction) {
    log.info("Fetching user {} from legacy system",
        loyaltyTransaction.getMemberId());
  }

  @Override
  public void afterProcess(LoyaltyTransaction loyaltyTransaction, User result) {
    log.info("Fetched successfully: user {} has {} old loyalty points",
        result.getId(), loyaltyTransaction.getPoints());
  }

  @Override
  public void onProcessError(LoyaltyTransaction loyaltyTransaction, Exception exception) {
    log.error("Fetched failed: unexpected error occurred while fetching user {}",
        loyaltyTransaction.getMemberId(), exception);
  }
}
