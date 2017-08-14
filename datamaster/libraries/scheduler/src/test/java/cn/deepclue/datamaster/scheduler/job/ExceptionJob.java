package cn.deepclue.datamaster.scheduler.job;

import cn.deepclue.datamaster.streamer.job.Job;
import cn.deepclue.datamaster.scheduler.exception.QuartzException;

/**
 * Created by magneto on 17-3-24.
 */
public class ExceptionJob extends Job {
    public ExceptionJob(int jId) {
        super(jId);
    }

    @Override
    public boolean run() {
        throw new QuartzException("job run exception.", "运行异常的任务！");
    }

    @Override
    public String serialize() {
        return "";
    }

    @Override
    public void deserialize(String jsonMap) {
    }

    @Override
    public String getGroupName() {
        return getClass().getSimpleName();
    }
}
