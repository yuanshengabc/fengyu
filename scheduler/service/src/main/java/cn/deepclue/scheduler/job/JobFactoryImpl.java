package cn.deepclue.scheduler.job;

import cn.deepclue.scheduler.domain.ScheduleInfo;
import cn.deepclue.scheduler.domain.Task;
import org.springframework.stereotype.Component;

@Component
public class JobFactoryImpl implements JobFactory {
    @Override
    public Job createJob(Task task) {
        CallbackJob callbackJob = new CallbackJob();
        callbackJob.setjId(task.getTaskId());
        callbackJob.setAppId(task.getAppId());
        callbackJob.setCallback(task.getCallback());

        ScheduleInfo scheduleInfo = task.getScheduleInfo();
        if (scheduleInfo.getSimpleScheduleInfo() != null) {
            callbackJob.setScheduleMode(new SimpleScheduleMode().from(scheduleInfo));
        } else {
            callbackJob.setScheduleMode(new CronScheduleMode().from(scheduleInfo));
        }

        return callbackJob;
    }
}
