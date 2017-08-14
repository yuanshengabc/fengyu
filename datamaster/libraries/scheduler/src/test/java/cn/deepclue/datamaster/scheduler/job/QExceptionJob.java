package cn.deepclue.datamaster.scheduler.job;

import cn.deepclue.datamaster.scheduler.qjob.QJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by xuzb on 17/05/2017.
 */
public class QExceptionJob implements QJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int jId = Integer.valueOf(context.getJobDetail().getKey().getName());
        new ExceptionJob(jId).run();
    }
}
