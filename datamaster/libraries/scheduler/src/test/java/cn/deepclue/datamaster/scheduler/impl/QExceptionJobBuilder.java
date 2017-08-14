package cn.deepclue.datamaster.scheduler.impl;

import cn.deepclue.datamaster.scheduler.job.QExceptionJob;
import cn.deepclue.datamaster.streamer.job.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;

/**
 * Created by xuzb on 17/05/2017.
 */
public class QExceptionJobBuilder extends QStreamJobBuilder {

    @Override
    public JobDetail buildJobDetail(Job job) {
        String jobData = job.serialize();

        return JobBuilder.newJob(QExceptionJob.class)
                .withIdentity(String.valueOf(job.getjId()), job.getGroupName())
                .usingJobData(JOB_CLASS_KEY, job.getClass().getName())
                .usingJobData(JOB_DATA_KEY, jobData)
                .build();
    }
}
