package com.raksit.example.loyalty.job.listener;

import com.raksit.example.loyalty.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;

@Slf4j
public class UserRepositoryItemReadLoggerListener implements ItemReadListener<User> {

  @Override
  public void beforeRead() {
    log.info("Reading all users from a database");
  }

  @Override
  public void afterRead(User user) {
    log.info("Read successfully: user {} will be processed", user.getId());
  }

  @Override
  public void onReadError(Exception ex) {
    log.error("Read failed: unexpected error occurred while reading all users", ex);
  }
}
