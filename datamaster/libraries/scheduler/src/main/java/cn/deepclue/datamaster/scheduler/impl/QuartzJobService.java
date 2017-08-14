package cn.deepclue.datamaster.scheduler.impl;

import cn.deepclue.datamaster.streamer.job.Job;
import cn.deepclue.datamaster.scheduler.JobScheduler;
import cn.deepclue.datamaster.scheduler.JobService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 19/03/2017.
 */
public class QuartzJobService implements JobService {
    private JobScheduler jobScheduler;

    public void setJobScheduler(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
        jobScheduler.start();
    }

    @Override public boolean shutdown() {
        return false;
    }

    @Override
    public boolean schedule(Job job) {
        return jobScheduler.schedule(job);
    }

    @Override public boolean clear() {
        return jobScheduler.clear();
    }

    @Override
    public boolean stopJobByTaskId(int tid) {
        return false;
    }

    @Override public boolean stopJob(Job job) {
        return jobScheduler.unschedule(job);
    }

    @Override
    public List<Job> getJobs(int page, int pageSize) {
        return new ArrayList<>();
    }
}
