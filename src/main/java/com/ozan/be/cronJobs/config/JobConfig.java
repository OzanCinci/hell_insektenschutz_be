package com.ozan.be.cronJobs.config;

import java.util.List;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JobConfig {
  public record JobSchedule(String className, String cronExpression, Boolean enabled) {}

  private List<JobSchedule> schedules =
      List.of(
          new JobSchedule("TestJob", "0 * * * * ?", false), // every minute
          new JobSchedule("SqsPollingJob", "0 */2 * * * ?", false)); // every two minute

  public List<JobSchedule> getActiveSchedules() {
    return schedules.stream().filter(JobSchedule::enabled).toList();
  }
}
