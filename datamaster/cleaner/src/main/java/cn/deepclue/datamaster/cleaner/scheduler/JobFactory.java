package cn.deepclue.datamaster.cleaner.scheduler;

import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.streamer.job.Job;

/**
 * Created by xuzb on 08/04/2017.
 */
public interface JobFactory {
    Job createJob(Task task);
}
