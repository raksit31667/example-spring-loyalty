package com.raksit.example.loyalty.job.listener;

import com.raksit.example.loyalty.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class UserRepositoryItemWriteLoggerListener implements ItemWriteListener<User> {

  @Override
  public void beforeWrite(List<? extends User> users) {
    log.info("Updating loyalty points of {} users into a database", getUserIds(users));
  }

  private List<UUID> getUserIds(List<? extends User> users) {
    return users.stream().map(User::getId).collect(Collectors.toList());
  }

  @Override
  public void afterWrite(List<? extends User> users) {
    log.info("Updated successfully: loyalty points of {} users updated", getUserIds(users));
  }

  @Override
  public void onWriteError(Exception exception, List<? extends User> users) {
    log.error("Updated failed: unexpected error occurred while reading {} users", getUserIds(users), exception);
  }
}
