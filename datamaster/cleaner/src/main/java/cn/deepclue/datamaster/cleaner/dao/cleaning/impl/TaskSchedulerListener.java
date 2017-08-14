package cn.deepclue.datamaster.cleaner.dao.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.TaskDao;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.task.TaskStatus;
import cn.deepclue.datamaster.scheduler.JobListener;
import cn.deepclue.datamaster.scheduler.QJobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by magneto on 17-3-27.
 */
public class TaskSchedulerListener implements JobListener {

    private static Logger logger = LoggerFactory.getLogger(TaskSchedulerListener.class);

    private TaskDao taskDao;

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override public void onStatusChanged(int taskId, QJobStatus status) {
        TaskStatus taskStatus = TaskStatus.valueOf(status.name());

        Task task = taskDao.getTask(taskId);
        // FIXME: why task can be null?
        if (task == null) {
            logger.error("Cannot find task " + taskId + ".");
            return;
        }

        task.setModifiedOn(new Date());
        task.setTaskStatus(taskStatus);

        taskDao.update(task);

        logger.info("Task " + taskId + " changed to " + status + ".");
    }

    @Override
    public void onSuccess(int taskId) {
        logger.info("Task " + taskId + " run successfully.");
    }

    @Override public void onFailure(int taskId, Throwable e) {
        logger.error("Task " + taskId + " failed {}", e);
    }

}
