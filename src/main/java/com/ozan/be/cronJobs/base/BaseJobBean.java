package com.ozan.be.cronJobs.base;

import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.slf4j.MDC;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public abstract class BaseJobBean extends QuartzJobBean implements IJobRunner {

  // Generate unique ID for each job execution
  private void generateAndSetJobId() {
    String jobId = UUID.randomUUID().toString();
    MDC.put("jobId", jobId);
  }

  @Override
  protected void executeInternal(JobExecutionContext context) {
    boolean isError = false;

    generateAndSetJobId();
    log.info("Starting Job: {}", context.getJobDetail().getKey().getName());
    try {
      executeJob();
    } catch (Exception e) {
      isError = true;
      log.error(
          "Error in Job: {}. Error: {}", context.getJobDetail().getKey().getName(), e.getMessage());
    } finally {
      if (isError) {
        log.info("Job {} interrupted and terminated.", context.getJobDetail().getKey().getName());
      } else {
        log.info("Job {} completed.", context.getJobDetail().getKey().getName());
      }
      MDC.clear();
    }
  }

  @Override
  public void run(Map<String, Object> args) {
    try {
      generateAndSetJobId();
      log.info(
          "Running job manually: {} with Job ID: {}", getClass().getSimpleName(), MDC.get("jobId"));
      executeJob();
    } finally {
      MDC.clear();
    }
  }

  protected abstract void executeJob();
}
