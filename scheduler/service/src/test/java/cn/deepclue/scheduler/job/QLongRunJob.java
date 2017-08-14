package cn.deepclue.scheduler.job;

import cn.deepclue.scheduler.service.impl.builder.QJobBuilderImpl;
import cn.deepclue.scheduler.service.impl.qjob.QJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by xuzb on 17/05/2017.
 */
public class QLongRunJob implements QJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int jId = Integer.valueOf(context.getJobDetail().getKey().getName());

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        long time = jobDataMap.getLongFromString(QJobBuilderImpl.JOB_DATA_KEY);

        new LongRunJob(jId, time).run();
    }
}
