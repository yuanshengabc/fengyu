package cn.deepclue.scheduler;

import cn.deepclue.scheduler.domain.Callback;
import cn.deepclue.scheduler.job.SimpleScheduleMode;
import cn.deepclue.scheduler.job.CallbackJob;
import cn.deepclue.scheduler.service.JobService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

/**
 * Created by ggchangan on 17-7-11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobServiceTest {
    @Autowired
    private JobService jobService;

    @Test
    public void scheduleTest() {
        SimpleScheduleMode simpleScheduleMode = new SimpleScheduleMode();
        simpleScheduleMode.setRepeatCount(1);

        Callback callback = new Callback();
        callback.setUrl("www.baidu.com");
        callback.setRequestBody("{}");

        CallbackJob job = new CallbackJob();
        job.setAppId(1);
        job.setScheduleMode(simpleScheduleMode);

        assertTrue(jobService.schedule(job));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
