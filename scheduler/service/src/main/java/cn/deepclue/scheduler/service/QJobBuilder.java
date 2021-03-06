package cn.deepclue.scheduler.service;

import cn.deepclue.scheduler.job.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 * Created by magneto on 17-3-22.
 */
public interface QJobBuilder {
    Trigger buildTrigger(Job job);
    JobDetail buildJobDetail(Job job);
}
