package cn.deepclue.datamaster.scheduler.impl;

import cn.deepclue.datamaster.cleaning.job.AnalysisJob;
import cn.deepclue.datamaster.scheduler.job.CronJob;
import cn.deepclue.datamaster.scheduler.job.ExceptionJob;
import cn.deepclue.datamaster.scheduler.job.LongRunJob;
import cn.deepclue.datamaster.streamer.job.Job;
import cn.deepclue.datamaster.scheduler.exception.QuartzException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.quartz.*;
import org.quartz.listeners.JobListenerSupport;
import org.quartz.listeners.SchedulerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.*;
import static org.quartz.impl.matchers.EverythingMatcher.allJobs;

/**
 * Created by magneto on 17-3-22.
 */
public class QuartzJobSchedulerTest {
    private static Logger logger = LoggerFactory.getLogger(QuartzJobSchedulerTest.class);

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private QuartzJobScheduler quartzJobScheduler;
    @Before public void setUp() {
        quartzJobScheduler = new QuartzJobScheduler();
        quartzJobScheduler.setqJobBuilder(new QStreamJobBuilder());
    }

    @Test public void schedule() throws SchedulerException {
        expectedException.expect(QuartzException.class);
        Job job = new AnalysisJob(1, null, null);
        boolean ret = quartzJobScheduler.schedule(job);
        assertFalse(ret);

        Scheduler scheduler = quartzJobScheduler.getScheduler();
        assertNull(scheduler);

        boolean isStart = quartzJobScheduler.start();
        scheduler = quartzJobScheduler.getScheduler();
        assertNotNull(scheduler);
        assertTrue(isStart);
        assertTrue(scheduler.isStarted());

        expectedException.expect(QuartzException.class);
        boolean isScheduler = quartzJobScheduler.schedule(job);
        assertTrue(isScheduler);
        assertFalse(scheduler.checkExists(new JobKey("1")));
        assertTrue(scheduler.checkExists(new JobKey("1", job.getGroupName())));
    }

    @Test public void start() {
        //多次启动使用的是同一个对象，并且保留了原Scheduler的状态
        boolean isStart = quartzJobScheduler.start();
        Scheduler scheduler1 = quartzJobScheduler.getScheduler();
        assertTrue(isStart);
        isStart = quartzJobScheduler.start();
        Scheduler scheduler2 = quartzJobScheduler.getScheduler();
        assertTrue(isStart);
        assertEquals(scheduler1, scheduler2);
    }

    @Test public void shutdown() {
        boolean isShutdown = quartzJobScheduler.shutdown();
        assertFalse(isShutdown);
        quartzJobScheduler.start();
        isShutdown = quartzJobScheduler.shutdown();
        assertTrue(isShutdown);
    }

    @Test public void state() throws SchedulerException, InterruptedException {
        quartzJobScheduler.start();
        quartzJobScheduler.clear();
        Scheduler scheduler = quartzJobScheduler.getScheduler();
        CustomJobListener customJobListener = new CustomJobListener();
        scheduler.getListenerManager().addJobListener(customJobListener, allJobs());
        quartzJobScheduler.setqJobBuilder(new QLongRunJobBuilder());
        quartzJobScheduler.schedule(new LongRunJob(1, 1));
        quartzJobScheduler.schedule(new LongRunJob(2, 3));
        quartzJobScheduler.schedule(new LongRunJob(3, 5));

        //异常任务测试
        quartzJobScheduler.setqJobBuilder(new QExceptionJobBuilder());
        quartzJobScheduler.schedule(new ExceptionJob(4));

        Thread.sleep(3000);
        //所有任务启动之后，shutdown(true)可以保证已经启动的任务执行完之后才会关闭scheduler
        scheduler.shutdown(true);
    }

    @Test public void deleteJob() throws SchedulerException, InterruptedException {
        quartzJobScheduler.start();
        quartzJobScheduler.clear();
        quartzJobScheduler.setqJobBuilder(new QLongRunJobBuilder());

        LongRunJob job = new LongRunJob(1, 1);
        String groupName = job.getGroupName();
        quartzJobScheduler.schedule(job);
        quartzJobScheduler.schedule(new LongRunJob(2, 3));
        quartzJobScheduler.schedule(new LongRunJob(3, 5));

        assertTrue(quartzJobScheduler.getScheduler().checkExists(new JobKey("1", groupName)));
        assertTrue(quartzJobScheduler.getScheduler().checkExists(new JobKey("2", groupName)));
        assertTrue(quartzJobScheduler.getScheduler().checkExists(new JobKey("3", groupName)));
        //验证尚未运行的任务，可直接删除，尚未运行的任务2可直接被删除
        //Boolean isDel = quartzJobScheduler.deleteJob(new JobKey("2", GROUP));
        //删除必须给GROUP
        Boolean isDel = quartzJobScheduler.deleteJob(2, groupName);
        assertTrue(isDel);

        assertTrue(quartzJobScheduler.getScheduler().checkExists(new JobKey("1", groupName)));
        assertFalse(quartzJobScheduler.getScheduler().checkExists(new JobKey("2", groupName)));
        assertTrue(quartzJobScheduler.getScheduler().checkExists(new JobKey("3", groupName)));

        //正在运行的任务不可以被取消
        Thread.sleep(4*1000);

        for (int i=1; i<=3; i++) {
            String state = quartzJobScheduler.getScheduler().getTriggerState(
                new TriggerKey(String.valueOf(i), groupName)).name();
            if (i == 1) assertEquals("NONE", state);
            if (i == 2) assertEquals("NONE", state);
            //if (i == 3) assertEquals("COMPLETE", state);
        }


        //任务1，已经运行完毕，删除返回false
        isDel = quartzJobScheduler.deleteJob(1, groupName);
        assertFalse(isDel);

        List<JobExecutionContext> currentlyExecutingJobs = quartzJobScheduler.getScheduler().getCurrentlyExecutingJobs();
        assertEquals(1, currentlyExecutingJobs.size());
        assertEquals(LongRunJob.class.getSimpleName() + ".3", currentlyExecutingJobs.get(0).getJobDetail().getKey().toString());
        assertTrue(quartzJobScheduler.getScheduler().checkExists(new JobKey("3", groupName)));

        //正在运行的任务不可以被取消，运行完之后相应的任务被取消
        isDel = quartzJobScheduler.deleteJob(new JobKey("3", groupName));
        assertTrue(isDel);
        assertFalse(quartzJobScheduler.getScheduler().checkExists(new JobKey("3", groupName)));

        quartzJobScheduler.getScheduler().shutdown(true);

    }

    /**
     *获取任务的具体返回结果,如任务确实被执行完毕
     * 获取任务的具体状态
     */
    static class CustomJobListener extends JobListenerSupport {

        @Override public String getName() {
            return "任务运行过程监听";
        }

        public void jobToBeExecuted(JobExecutionContext context) {
            //TODO add log
            logger.info(context.getJobDetail().getKey().toString());
        }

        //任务结束后的处理函数
        public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
            //所有执行的任务抛出JobExecutionException异常，根据异常是否为空，可判定任务运行过程中，是否产生了异常
            if (jobException == null) {
                logger.info(String.format("任务%s运行总时间：%d",
                    context.getJobDetail().getKey().toString(), context.getJobRunTime()));
                logger.info(String.format("任务%s运行结果：%s",
                    context.getJobDetail().getKey().toString(), context.getResult()));
                assertTrue((Boolean) context.getResult());
            } else {
                logger.warn(jobException.getMessage());
                jobException.printStackTrace();
            }

        }
    }

    /**
     * 当scheduler失败时，获取Cron任务执行状态，当scheduler被resume之后，验证状态
     */
    static class CronJobSchedulerListener extends SchedulerListenerSupport {
        @Override
        public void schedulerStarted() {
            logger.info(String.format("scheduler已经启动!"));
        }

        @Override
        public void schedulerShutdown() {
            logger.info(String.format("scheduler已经关闭!"));
        }

        @Override
        public void jobScheduled(Trigger trigger) {
            logger.info(String.format("%s被scheduler", trigger.getKey().toString()));
        }

        @Override
        public void triggerPaused(TriggerKey triggerKey) {
            logger.info(String.format("%s被暂停", triggerKey.toString()));
        }

        @Override
        public void triggerResumed(TriggerKey triggerKey) {
            logger.info(String.format("%s被重新启动", triggerKey.toString()));
        }

    }



    @Test public void resumeGroup() {
        quartzJobScheduler.start();
        quartzJobScheduler.clear();
        quartzJobScheduler.setqJobBuilder(new QCronJobBuilder());
        quartzJobScheduler.schedule(new CronJob(1));

        try {
            quartzJobScheduler.getScheduler().getListenerManager().addSchedulerListener(new CronJobSchedulerListener());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        quartzJobScheduler.shutdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        quartzJobScheduler.start();
        String group = new CronJob(1).getGroupName();
        quartzJobScheduler.resumeGroup(group);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
