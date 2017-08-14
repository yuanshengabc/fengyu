package cn.deepclue.datamaster.cleaner.scheduler;

import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;

/**
 * Created by xuzb on 16/03/2017.
 */
public interface TaskScheduler {
    boolean schedule(Task task);
    boolean clear();
    boolean stopTaskById(int tid);
    boolean stopTask(Task task);
    boolean shutdown();
}
