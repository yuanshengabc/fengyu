package cn.deepclue.scheduler.job;

import cn.deepclue.scheduler.domain.Task;

public interface JobFactory {
    Job createJob(Task task);
}
