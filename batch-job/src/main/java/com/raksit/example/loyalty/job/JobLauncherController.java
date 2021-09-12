package com.raksit.example.loyalty.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobLauncherController {

  private final JobLauncher jobLauncher;

  private final Job migrateLegacyLoyalty;

  public JobLauncherController(JobLauncher jobLauncher,
      Job migrateLegacyLoyalty) {
    this.jobLauncher = jobLauncher;
    this.migrateLegacyLoyalty = migrateLegacyLoyalty;
  }

  @GetMapping("/execute")
  public String execute() throws Exception {
    jobLauncher.run(migrateLegacyLoyalty, new JobParameters());
    return "Done";
  }
}
