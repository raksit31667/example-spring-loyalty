package com.raksit.example.loyalty.job.launcher;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

@ExtendWith(MockitoExtension.class)
class JobLauncherControllerTest {

  @Mock
  private JobLauncher jobLauncher;

  @Mock
  private Job migrateLegacyLoyalty;

  @InjectMocks
  private JobLauncherController jobLauncherController;

  @Test
  void shouldLaunchJobWithMigrationDate_whenExecute_givenMigrationDate() throws Exception {
    // Given
    // When
    jobLauncherController.execute("2021-11-11");

    // Then
    verify(jobLauncher).run(migrateLegacyLoyalty,
        new JobParametersBuilder().addString("migrationDate", "2021-11-11", false)
            .toJobParameters());
  }
}