package com.raksit.example.loyalty.job.migratelegacy.listener;

import com.raksit.example.loyalty.job.migratelegacy.legacy.LoyaltyTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;

@Slf4j
public class UserRepositoryItemReadLoggerListener implements ItemReadListener<LoyaltyTransaction> {

  @Override
  public void beforeRead() {
    log.info("Reading all users from a database");
  }

  @Override
  public void afterRead(LoyaltyTransaction loyaltyTransaction) {
    log.info("Read successfully: user {} will be processed", loyaltyTransaction.getMemberId());
  }

  @Override
  public void onReadError(Exception ex) {
    log.error("Read failed: unexpected error occurred while reading all users", ex);
  }
}
