package com.raksit.example.loyalty.job.launcher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @PostMapping("/execute")
  public String execute(@RequestParam(required = false) String migrationDate) throws Exception {
    jobLauncher.run(migrateLegacyLoyalty,
        new JobParametersBuilder().addString("migrationDate", migrationDate, false)
            .toJobParameters());
    return "Done";
  }
}
