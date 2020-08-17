package com.weds.etl.base;

import com.weds.core.base.BaseJob;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

// @DisallowConcurrentExecution
// @PersistJobDataAfterExecution
public abstract class BaseSchedulerJob extends BaseJob implements Job {
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();
    private Logger log = LogManager.getLogger();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            before(context);
            doExecute(context);
            after(context, null);
        } catch (Exception e) {
            log.error("任务执行异常：", e);
            after(context, e);
        }
    }

    protected void before(JobExecutionContext context) {
        String keyName = context.getJobDetail().getKey().getName();
        log.info("Job [" + keyName + "] start");
        threadLocal.set(new Date());
    }

    protected void after(JobExecutionContext context, Exception e) {
        String keyName = context.getJobDetail().getKey().getName();
        log.info("Job [" + keyName + "] finished");
        Date startTime = threadLocal.get();
        threadLocal.remove();
        Date endTime = new Date();
        long runMs = startTime.getTime() - endTime.getTime();
        log.info("Use time [" + runMs + "], Job result [" + (e == null ? "Success" : "Error") + "]");
    }

    protected abstract void doExecute(JobExecutionContext context) throws Exception;
}
