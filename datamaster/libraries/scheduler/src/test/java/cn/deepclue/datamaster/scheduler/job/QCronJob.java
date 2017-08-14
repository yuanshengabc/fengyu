package cn.deepclue.datamaster.scheduler.job;

import cn.deepclue.datamaster.scheduler.qjob.QJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by xuzb on 17/05/2017.
 */
public class QCronJob implements QJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int jId = Integer.valueOf(context.getJobDetail().getKey().getName());
        new CronJob(jId).run();
    }
}
