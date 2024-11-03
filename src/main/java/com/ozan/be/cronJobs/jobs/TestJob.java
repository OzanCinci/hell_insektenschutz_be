package com.ozan.be.cronJobs.jobs;

import com.ozan.be.cronJobs.base.BaseJobBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestJob extends BaseJobBean {

  @Override
  protected void executeJob() {
    log.info("Executing TestingJob. This job runs every minute.");
  }
}
