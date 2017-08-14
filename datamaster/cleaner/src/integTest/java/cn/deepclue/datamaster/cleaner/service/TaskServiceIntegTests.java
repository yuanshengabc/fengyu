package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.dao.cleaning.DataSourceDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordSourceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.scheduler.TaskScheduler;
import cn.deepclue.datamaster.cleaner.service.cleaning.TaskService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by magneto on 17-4-8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TaskServiceIntegTests {
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Autowired
    @Qualifier(value = "taskService")
    private TaskService taskService;

    @Autowired
    private DataSourceDao dataSourceDao;

    @Autowired
    private RecordSourceDao recordSourceDao;

    @MockBean
    private TaskScheduler taskScheduler;

    @Test public void getRunningTasks() {
        List<Task> tasks = taskService.getRunningTasks(0, 10);
        assertThat(tasks.size()).isLessThanOrEqualTo(10);
    }

    @Test public void getPendingTasks() {
        List<Task> tasks = taskService.getPendingTasks(0, 10);
        assertThat(tasks.size()).isEqualTo(1);
    }

    @Test public void getFinishedTasks() {
        List<Task> tasks = taskService.getFinishedTasks(0, 10);
        assertThat(tasks.size()).isEqualTo(3);
    }

    @Test public void getFailedTasks() {
        List<Task> tasks = taskService.getFailedTasks(0, 10);
        assertThat(tasks.size()).isEqualTo(1);
    }
}
