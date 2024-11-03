package com.ozan.be.cronJobs.config;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DynamicJobSchedulerService {

  @Autowired private JobConfig jobConfig;
  @Autowired private Scheduler scheduler;
  private static final String PATH_RESOLVER = "com.ozan.be.cronJobs.jobs.";

  @PostConstruct
  public void scheduleAllJobs() throws SchedulerException, ClassNotFoundException {
    for (JobConfig.JobSchedule schedule : jobConfig.getActiveSchedules()) {
      String clazz = PATH_RESOLVER + schedule.className();
      Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(clazz);
      scheduleJob(jobClass, schedule.cronExpression());
    }
  }

  public void scheduleJob(Class<? extends Job> jobClass, String cronExpression)
      throws SchedulerException {
    JobDetail jobDetail =
        JobBuilder.newJob(jobClass).withIdentity(jobClass.getSimpleName()).storeDurably().build();

    Trigger trigger =
        TriggerBuilder.newTrigger()
            .withIdentity(jobClass.getSimpleName() + "Trigger")
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
            .build();

    scheduler.scheduleJob(jobDetail, trigger);
  }

  public void triggerJob(Class<? extends Job> jobClass, Map<String, Object> args)
      throws SchedulerException {
    JobDetail jobDetail =
        JobBuilder.newJob(jobClass)
            .withIdentity(jobClass.getSimpleName())
            .usingJobData(new JobDataMap(args))
            .build();

    scheduler.triggerJob(jobDetail.getKey(), jobDetail.getJobDataMap());
  }
}
