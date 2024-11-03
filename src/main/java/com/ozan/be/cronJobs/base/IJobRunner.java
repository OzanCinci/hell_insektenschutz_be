package com.ozan.be.cronJobs.base;

import java.util.Map;

public interface IJobRunner {
  void run(Map<String, Object> args);
}
