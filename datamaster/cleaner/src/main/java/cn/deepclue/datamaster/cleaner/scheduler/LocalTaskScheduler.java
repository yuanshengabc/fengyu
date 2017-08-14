package cn.deepclue.datamaster.cleaner.scheduler;

import cn.deepclue.datamaster.cleaner.dao.cleaning.TaskDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.impl.TaskSchedulerListener;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.streamer.job.Job;
import cn.deepclue.datamaster.scheduler.JobService;
import cn.deepclue.datamaster.scheduler.impl.QStreamJobBuilder;
import cn.deepclue.datamaster.scheduler.impl.QuartzJobScheduler;
import cn.deepclue.datamaster.scheduler.impl.QuartzJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by xuzb on 19/03/2017.
 */
@Component("loaclScheduler")
public class LocalTaskScheduler implements TaskScheduler {

    @Autowired
    private JobFactory jobFactory;

    @Autowired
    private TaskDao taskDao;

    private JobService jobService;

    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }

    public void setJobFactory(JobFactory jobFactory) {
        this.jobFactory = jobFactory;
    }

    @PostConstruct
    public void init() {
        // FIXME: Use autowired to refactor this.
        QuartzJobScheduler quartzJobScheduler = new QuartzJobScheduler();
        quartzJobScheduler.setqJobBuilder(new QStreamJobBuilder());

        TaskSchedulerListener listener = new TaskSchedulerListener();
        listener.setTaskDao(taskDao);

        quartzJobScheduler.addListener(listener);

        QuartzJobService service = new QuartzJobService();
        service.setJobScheduler(quartzJobScheduler);
        jobService = service;
    }

    @Override
    public boolean schedule(Task task) {
        Job job = jobFactory.createJob(task);
        return jobService.schedule(job);
    }

    @Override public boolean clear() {
        return jobService.clear();
    }

    @Override public boolean stopTaskById(int tid) {
        return false;
    }

    @Override public boolean stopTask(Task task) {
        Job job = jobFactory.createJob(task);
        return jobService.stopJob(job);
    }

    @Override public boolean shutdown() {
        return false;
    }
}
