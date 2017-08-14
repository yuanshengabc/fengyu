package cn.deepclue.datamaster.scheduler.impl;

import cn.deepclue.datamaster.scheduler.job.QCronJob;
import cn.deepclue.datamaster.streamer.job.Job;
import com.google.gson.Gson;
import org.quartz.*;

/**
 * Created by magneto on 17-3-27.
 */
public class QCronJobBuilder extends QStreamJobBuilder {

    public QCronJobBuilder() {
    }

    @Override
    public JobDetail buildJobDetail(Job job) {
        String jobData = job.serialize();

        return JobBuilder.newJob(QCronJob.class)
                .withIdentity(String.valueOf(job.getjId()), job.getGroupName())
                .usingJobData(JOB_CLASS_KEY, job.getClass().getName())
                .usingJobData(JOB_DATA_KEY, jobData)
                .build();
    }

    @Override
    public Trigger buildTrigger(Job job) {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withRepeatCount(100).withIntervalInSeconds(2);

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();

        return triggerBuilder.withIdentity(String.valueOf(job.getjId()), job.getGroupName())
                .withSchedule(simpleScheduleBuilder)
                .startNow().build();
    }
}
