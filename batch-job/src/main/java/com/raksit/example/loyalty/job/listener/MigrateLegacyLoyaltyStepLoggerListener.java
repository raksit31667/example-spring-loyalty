package com.raksit.example.loyalty.job.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

@Slf4j
public class MigrateLegacyLoyaltyStepLoggerListener implements StepExecutionListener {

  @Override
  public void beforeStep(StepExecution stepExecution) {
    log.info("Before step - there are {} users", stepExecution.getReadCount());
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    log.info("After step - there are {} updated users", stepExecution.getWriteCount());
    return stepExecution.getExitStatus();
  }
}
