package cn.deepclue.datamaster.cleaner.dao.cleaning.impl;

import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.task.TaskType;
import cn.deepclue.datamaster.cleaner.scheduler.JobFactory;
import cn.deepclue.datamaster.cleaner.scheduler.TaskScheduler;
import cn.deepclue.datamaster.streamer.job.Job;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by magneto on 17-3-27.
 */
public class LocalTaskSchedulerTest {

    @Autowired
    TaskScheduler taskScheduler;

    @MockBean
    JobFactory jobFactory;

    @Before
    public void setUp() {
        when(jobFactory.createJob(any(Task.class))).thenReturn(new Job(1) {
            @Override
            public boolean run() {
                return true;
            }

            @Override
            public String serialize() {
                return "";
            }

            @Override
            public void deserialize(String jsonMap) {
            }
        });
    }

    public void schedule() throws InterruptedException {
        taskScheduler.clear();
        Task task = new Task();
        task.setTid(1);
        task.setTaskType(TaskType.ANALYSIS);
        taskScheduler.schedule(task);

        //使得任务被调度
        Thread.sleep(1000);

        taskScheduler.shutdown();
    }

    public void stopTask() throws InterruptedException {
        taskScheduler.clear();
        List<AnalysisTaskMock> tasks = new ArrayList<>();
        for (int i=0; i<=20; i++) {
            AnalysisTaskMock analysisTask = new AnalysisTaskMock(i);
            tasks.add(analysisTask);
            taskScheduler.schedule(analysisTask);
        }

        boolean retStatus = taskScheduler.stopTask(tasks.get(20));
        assertTrue(retStatus);


        Thread.sleep(100);
        //taskScheduler.shutdown();
    }

    class AnalysisTaskMock extends Task {
        public AnalysisTaskMock(int tId) {
            setTid(tId);
            setTaskType(TaskType.ANALYSIS);
        }
    }

}
