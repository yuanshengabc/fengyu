package cn.deepclue.scheduler.job;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by magneto on 17-3-27.
 */
public class CronJob extends Job {
    public CronJob(int jId) {
        super(jId);
    }

    @Override
    public boolean run() {
        System.out.println("Hello quzrtz " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));

        return true;
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
