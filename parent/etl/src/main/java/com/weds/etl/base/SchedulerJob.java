package com.weds.etl.base;

import com.weds.core.base.BaseJob;
import org.quartz.Job;

// @DisallowConcurrentExecution
// @PersistJobDataAfterExecution
public abstract class SchedulerJob extends BaseJob implements Job {
    // void execute(JobExecutionContext context) throws JobExecutionException;.


}
