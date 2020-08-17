package com.weds.etl.service;

import com.weds.core.utils.DateUtils;
import com.weds.etl.base.SchedulerJob;
import com.weds.etl.constant.SchedulerConstants;
import com.weds.etl.entity.JobEntity;
import com.weds.etl.entity.SchedulerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.impl.matchers.GroupMatcher;

import javax.annotation.Resource;
import java.util.*;

public class SchedulerService {

    @Resource
    private Scheduler scheduler;

    private Logger log = LogManager.getLogger();

    public void addSchedulerListener(JobListener jobListener, TriggerListener triggerListener,
                                     SchedulerListener schedulerListener) throws SchedulerException {
        ListenerManager listenerManager = scheduler.getListenerManager();
        if (jobListener != null) {
            listenerManager.addJobListener(jobListener, EverythingMatcher.allJobs());
        }
        if (triggerListener != null) {
            listenerManager.addTriggerListener(triggerListener, EverythingMatcher.allTriggers());
        }
        if (schedulerListener != null) {
            listenerManager.addSchedulerListener(schedulerListener);
        }
    }

    /**
     * 启动调度器
     *
     * @throws SchedulerException
     */
    public void start() throws SchedulerException {
        if (scheduler.isStarted()) {
            return;
        }
        scheduler.start();
    }

    /**
     * 关闭调度器
     *
     * @throws SchedulerException
     */
    public void stop() throws SchedulerException {
        if (scheduler.isShutdown()) {
            return;
        }
        scheduler.shutdown(true);
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    public void pauseAll() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    public void resumeAll() throws SchedulerException {
        scheduler.resumeAll();
    }


    /**
     * 添加定时任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @throws Exception
     */
    public void addCronJob(String jobClassName, String jobGroupName, String cronExpression, JobDataMap jobDataMap) throws Exception {
        // 表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
                .withSchedule(scheduleBuilder).build();
        this.addJob(jobClassName, jobGroupName, cronTrigger, jobDataMap);
    }

    public void addCronJob(String jobClassName, String cronExpression, JobDataMap jobDataMap) throws Exception {
        this.addCronJob(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME, cronExpression, jobDataMap);
    }

    public void addCronJob(String jobClassName, String cronExpression) throws Exception {
        this.addCronJob(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME, cronExpression, null);
    }

    /**
     * @param job
     * @param jobGroupName
     * @param cronExpression
     * @param jobDataMap
     * @throws Exception
     */
    public void addCronJob(Job job, String jobGroupName, String cronExpression, JobDataMap jobDataMap) throws Exception {
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new RuntimeException("Cron表达式错误");
        }
        // 表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(job.getClass().getName(), jobGroupName)
                .withSchedule(scheduleBuilder).build();
        this.addJob(job, jobGroupName, cronTrigger, jobDataMap);
    }

    public void addCronJob(Job job, String cronExpression, JobDataMap jobDataMap) throws Exception {
        this.addCronJob(job, SchedulerConstants.DEFAULT_GROUP_NAME, cronExpression, jobDataMap);
    }

    public void addCronJob(Job job, String cronExpression) throws Exception {
        this.addCronJob(job, SchedulerConstants.DEFAULT_GROUP_NAME, cronExpression, null);
    }

    /**
     * 添加间隔任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @param seconds
     * @throws Exception
     */
    public void addDelayJob(String jobClassName, String jobGroupName, int seconds, int delay, JobDataMap jobDataMap) throws Exception {
        // 表达式调度构建器(即任务执行的时间)
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForever(seconds);
        // 按新的cronExpression表达式构建一个新的trigger
        SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
                .withSchedule(scheduleBuilder).startAt(DateUtils.addSeconds(new Date(), delay)).build();
        addJob(jobClassName, jobGroupName, simpleTrigger, jobDataMap);
    }

    public void addDelayJob(String jobClassName, int seconds, JobDataMap jobDataMap) throws Exception {
        this.addDelayJob(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME, seconds, 0, jobDataMap);
    }

    public void addDelayJob(String jobClassName, int seconds) throws Exception {
        this.addDelayJob(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME, seconds, 0, null);
    }

    public void addDelayJob(String jobClassName, int seconds, int delay, JobDataMap jobDataMap) throws Exception {
        this.addDelayJob(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME, seconds, delay, jobDataMap);
    }

    public void addDelayJob(String jobClassName, int seconds, int delay) throws Exception {
        this.addDelayJob(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME, seconds, delay, null);
    }

    /**
     * @param job
     * @param jobGroupName
     * @param seconds
     * @param jobDataMap
     * @throws Exception
     */
    public void addDelayJob(Job job, String jobGroupName, int seconds, int delay, JobDataMap jobDataMap) throws Exception {
        // 表达式调度构建器(即任务执行的时间)
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForever(seconds);
        // 按新的cronExpression表达式构建一个新的trigger
        SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity(job.getClass().getName(), jobGroupName)
                .withSchedule(scheduleBuilder).startAt(DateUtils.addSeconds(new Date(), delay)).build();
        addJob(job, jobGroupName, simpleTrigger, jobDataMap);
    }

    public void addDelayJob(Job job, int seconds, JobDataMap jobDataMap) throws Exception {
        this.addDelayJob(job, SchedulerConstants.DEFAULT_GROUP_NAME, seconds, 0, jobDataMap);
    }

    public void addDelayJob(Job job, int seconds) throws Exception {
        this.addDelayJob(job, SchedulerConstants.DEFAULT_GROUP_NAME, seconds, 0, null);
    }

    public void addDelayJob(Job job, int seconds, int delay, JobDataMap jobDataMap) throws Exception {
        this.addDelayJob(job, SchedulerConstants.DEFAULT_GROUP_NAME, seconds, delay, jobDataMap);
    }

    public void addDelayJob(Job job, int seconds, int delay) throws Exception {
        this.addDelayJob(job, SchedulerConstants.DEFAULT_GROUP_NAME, seconds, delay, null);
    }

    /**
     * 添加任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @param trigger
     * @throws Exception
     */
    public void addJob(String jobClassName, String jobGroupName, Trigger trigger, JobDataMap jobDataMap) throws Exception {
        JobBuilder jobBuilder = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName);
        if (jobDataMap != null) {
            jobBuilder = jobBuilder.usingJobData(jobDataMap);
        }
        // 构建job信息
        JobDetail jobDetail = jobBuilder.build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void addJob(Job job, String jobGroupName, Trigger trigger, JobDataMap jobDataMap) throws Exception {
        JobBuilder jobBuilder = JobBuilder.newJob(job.getClass()).withIdentity(job.getClass().getName(), jobGroupName);
        if (jobDataMap != null) {
            jobBuilder = jobBuilder.usingJobData(jobDataMap);
        }
        // 构建job信息
        JobDetail jobDetail = jobBuilder.build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 更新任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @throws Exception
     */
    public void updateCronJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new RuntimeException("Cron表达式错误");
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        // 按新的cronExpression表达式重新构建trigger
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        // 按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    public void updateCronJob(String jobClassName, String cronExpression) throws Exception {
        updateCronJob(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME, cronExpression);
    }

    /**
     * 更新任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @param seconds
     * @throws Exception
     */
    public void updateDelayJob(String jobClassName, String jobGroupName, int seconds) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
        SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
        // 表达式调度构建器
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForever(seconds);
        // 按新的cronExpression表达式重新构建trigger
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        // 按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    public void updateDelayJob(String jobClassName, int seconds) throws Exception {
        updateDelayJob(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME, seconds);
    }

    /**
     * 暂停任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    public void pauseJob(String jobClassName, String jobGroupName) throws Exception {
        scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    public void pauseJob(String jobClassName) throws Exception {
        this.pauseJob(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME);
    }

    /**
     * 恢复任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    public void resumeJob(String jobClassName, String jobGroupName) throws Exception {
        scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
    }


    public void resumeJob(String jobClassName) throws Exception {
        this.resumeJob(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME);
    }

    /**
     * 删除任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    public void deleteJob(String jobClassName, String jobGroupName) throws Exception {
        // 停止触发器
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
        // 移除触发器
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
        // 删除任务
        scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    public void deleteJob(String jobClassName) throws Exception {
        this.deleteJob(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME);
    }

    public void runJob(String jobClassName, String jobGroupName) throws SchedulerException {
        scheduler.triggerJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    public void runJob(String jobClassName) throws SchedulerException {
        scheduler.triggerJob(JobKey.jobKey(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME));
    }

    /**
     * 获取任务状态
     *
     * @param jobClassName
     * @param jobGroupName
     * @return
     * @throws Exception
     */
    public Trigger.TriggerState getState(String jobClassName, String jobGroupName) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
        Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
        return triggerState;
    }

    public Trigger.TriggerState getState(String jobClassName) throws Exception {
        return this.getState(jobClassName, SchedulerConstants.DEFAULT_GROUP_NAME);
    }

    /**
     * 获取所有定时任务
     *
     * @return
     * @throws SchedulerException
     */
    public List<SchedulerEntity> getAllState() throws SchedulerException {
        List<SchedulerEntity> list = new ArrayList<>();
        List<String> jobGroupNames = scheduler.getJobGroupNames();
        for (String jobGroupName : jobGroupNames) {
            SchedulerEntity schedulerEntity = new SchedulerEntity();
            schedulerEntity.setJobGroupName(jobGroupName);
            List<JobEntity> jobList = new ArrayList<>();
            Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.groupEquals(jobGroupName));
            for (TriggerKey triggerKey : triggerKeys) {
                JobEntity jobEntity = new JobEntity();
                Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
                Trigger trigger = scheduler.getTrigger(triggerKey);
                jobEntity.setNextFireTime(trigger.getNextFireTime());
                jobEntity.setPreviousFireTime(trigger.getPreviousFireTime());
                jobEntity.setStartTime(trigger.getStartTime());
                jobEntity.setEndTime(trigger.getEndTime());
                jobEntity.setFinalFireTime(trigger.getFinalFireTime());

                JobKey jobKey = trigger.getJobKey();
                jobEntity.setJobKey(jobKey);
                jobEntity.setTriggerState(triggerState);
                jobList.add(jobEntity);
                // Date nextFireTime = trigger.getNextFireTime();
                // String jobName = jobKey.getName();
                // String jobGroup = jobKey.getGroup();
            }
            schedulerEntity.setList(jobList);
            list.add(schedulerEntity);
        }
        return list;
    }

    private static SchedulerJob getClass(String classname) throws Exception {
        Class<?> clazz = Class.forName(classname);
        return (SchedulerJob) clazz.newInstance();
    }
}
