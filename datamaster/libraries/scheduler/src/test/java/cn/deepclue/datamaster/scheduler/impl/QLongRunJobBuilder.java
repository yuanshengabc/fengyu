package cn.deepclue.datamaster.scheduler.impl;

import cn.deepclue.datamaster.scheduler.job.QLongRunJob;
import cn.deepclue.datamaster.streamer.job.Job;
import com.google.gson.Gson;
import org.quartz.*;

import java.util.Date;

import static org.quartz.DateBuilder.futureDate;

/**
 * Created by magneto on 17-3-24.
 */
public class QLongRunJobBuilder extends QStreamJobBuilder {
    public QLongRunJobBuilder() {
    }

    @Override
    public JobDetail buildJobDetail(Job job) {
        String jobData = job.serialize();

        return JobBuilder.newJob(QLongRunJob.class)
                .withIdentity(String.valueOf(job.getjId()), job.getGroupName())
                .usingJobData(JOB_CLASS_KEY, job.getClass().getName())
                .usingJobData(JOB_DATA_KEY, jobData)
                .build();
    }

    @Override
    public Trigger buildTrigger(Job job) {
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();

        Date future = futureDate(2, DateBuilder.IntervalUnit.SECOND);
        return triggerBuilder.withIdentity(String.valueOf(job.getjId()), job.getGroupName()).startAt(future).build();
    }
}
