package cn.deepclue.scheduler.service.impl.builder;


import cn.deepclue.scheduler.job.CronScheduleMode;
import cn.deepclue.scheduler.job.Job;
import cn.deepclue.scheduler.job.ScheduleMode;
import cn.deepclue.scheduler.job.SimpleScheduleMode;
import cn.deepclue.scheduler.service.QJobBuilder;
import cn.deepclue.scheduler.service.impl.qjob.QScheduleJob;
import org.quartz.*;
import org.springframework.stereotype.Service;

/**
 * Created by magneto on 17-3-24.
 */
@Service("qJobBuilder")
public class QJobBuilderImpl implements QJobBuilder {
    public static final String JOB_CLASS_KEY = "JOB_CLASS";
    public static final String JOB_DATA_KEY = "JOB_DATA";

    private static final int FUTURE_TIME = 1;

    @Override
    public Trigger buildTrigger(Job job) {
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        ScheduleMode scheduleMode = job.getScheduleMode();

        if (scheduleMode instanceof SimpleScheduleMode) {
            SimpleScheduleMode simpleScheduleMode = (SimpleScheduleMode) scheduleMode;

            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
            if (simpleScheduleMode.getRepeatCount() < 0) {
                simpleScheduleBuilder.repeatForever();
            } else {
                simpleScheduleBuilder.withRepeatCount(simpleScheduleMode.getRepeatCount());
            }
            simpleScheduleBuilder.withIntervalInMilliseconds(simpleScheduleMode.getRepeatInterval());

            triggerBuilder.withSchedule(simpleScheduleBuilder);
        } else if (scheduleMode instanceof CronScheduleMode) {
            CronScheduleMode cronScheduleMode = (CronScheduleMode) scheduleMode;

            CronScheduleBuilder cronScheduleBuilder =
                    CronScheduleBuilder.cronSchedule(cronScheduleMode.getCronExpression());

            triggerBuilder.withSchedule(cronScheduleBuilder);
        } else {
            throw new IllegalStateException("unknown scheduleMode");
        }

        if (scheduleMode.getStartTime() != null) {
            triggerBuilder.startAt(scheduleMode.getStartTime());
        } else {
            triggerBuilder.startAt(DateBuilder.futureDate(FUTURE_TIME, DateBuilder.IntervalUnit.SECOND));
        }
        if (scheduleMode.getEndTime() != null) {
            triggerBuilder.endAt(scheduleMode.getEndTime());
        }

        triggerBuilder.withDescription(scheduleMode.getTriggerDescription());
        triggerBuilder.withPriority(scheduleMode.getTriggerPriority());


        return triggerBuilder.withIdentity(String.valueOf(job.getjId()), job.getGroupName()).build();
    }

    @Override
    public JobDetail buildJobDetail(Job job) {
        String jobData = job.serialize();

        return JobBuilder.newJob(QScheduleJob.class)
                .withIdentity(String.valueOf(job.getjId()), job.getGroupName())
                .usingJobData(JOB_CLASS_KEY, job.getClass().getName())
                .usingJobData(JOB_DATA_KEY, jobData)
                .build();
    }
}
