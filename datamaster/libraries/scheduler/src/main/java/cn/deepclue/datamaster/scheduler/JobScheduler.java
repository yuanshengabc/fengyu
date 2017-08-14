package cn.deepclue.datamaster.scheduler;

import cn.deepclue.datamaster.streamer.job.Job;

/**
 * Created by xuzb on 19/03/2017.
 */
public interface JobScheduler {
    boolean schedule(Job job);
    boolean unschedule(Job job);
    boolean clear();
    boolean start();
    boolean shutdown();
}
