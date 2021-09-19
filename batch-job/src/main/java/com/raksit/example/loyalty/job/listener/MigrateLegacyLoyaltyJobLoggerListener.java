package com.raksit.example.loyalty.job.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class MigrateLegacyLoyaltyJobLoggerListener implements JobExecutionListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    log.info("Before job - started at {}", jobExecution.getStartTime());
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    log.info("After job - ended at {} with status {}",
        jobExecution.getEndTime(), jobExecution.getExitStatus().getExitCode());
  }
}
