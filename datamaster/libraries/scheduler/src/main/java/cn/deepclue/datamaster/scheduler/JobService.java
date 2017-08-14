package cn.deepclue.datamaster.scheduler;

import cn.deepclue.datamaster.streamer.job.Job;

import java.util.List;

/**
 * Created by xuzb on 19/03/2017.
 */
public interface JobService {
    boolean shutdown();
    boolean schedule(Job job);
    boolean clear();
    boolean stopJobByTaskId(int tid);
    boolean stopJob(Job job);
    List<Job> getJobs(int page, int pageSize);
}
