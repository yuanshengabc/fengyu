package cn.deepclue.datamaster.scheduler.impl;

import cn.deepclue.datamaster.cleaning.job.AnalysisJob;
import cn.deepclue.datamaster.streamer.job.StreamerJob;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static org.junit.Assert.*;

/**
 * Created by magneto on 17-3-24.
 */
public class QStreamJobBuilderTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private QStreamJobBuilder qJobBuilder;
    private StreamerJob job;

    @Before public void setUp() {
        qJobBuilder = new QStreamJobBuilder();
        job = new AnalysisJob(1, null, null);
    }

    @Test
    public void buildJobDetail() {
        JobDetail jobDetail = qJobBuilder.buildJobDetail(job);

        JobDataMap jobDataMap = jobDetail.getJobDataMap();

        assertEquals(jobDataMap.getString(QStreamJobBuilder.JOB_CLASS_KEY), job.getClass().getName());
        assertEquals(jobDataMap.getString(QStreamJobBuilder.JOB_DATA_KEY), job.serialize());

        assertEquals("1", jobDetail.getKey().getName());
    }

    @Test public void buildTrigger() {

        Trigger trigger = qJobBuilder.buildTrigger(job);

        assertEquals("1", trigger.getKey().getName());
        assertEquals(job.getGroupName(), trigger.getKey().getGroup());
        assertEquals(0, trigger.getJobDataMap().size());

        assertFalse(trigger.mayFireAgain());
        assertNull(trigger.getEndTime());
        assertEquals(5, trigger.getPriority());
    }

}
