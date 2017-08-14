package cn.deepclue.datamaster.cleaner.service.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.*;
import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.task.TaskStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEdition;
import cn.deepclue.datamaster.cleaner.scheduler.TaskScheduler;
import cn.deepclue.datamaster.cleaner.service.cleaning.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xuzb on 17/03/2017.
 */
@Service("taskService")
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private DataHouseDao dataHouseDao;

    @Autowired
    private DataSourceDao dataSourceDao;

    @Autowired
    private RecordSourceDao recordSourceDao;

    @Autowired
    private CleaningWorkspaceDao cleaningWorkspaceDao;

    @Autowired
    @Qualifier(value = "taskDao")
    private TaskDao taskDao;

    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void setDataHouseDao(DataHouseDao dataHouseDao) {
        this.dataHouseDao = dataHouseDao;
    }

    public void setDataSourceDao(DataSourceDao dataSourceDao) {
        this.dataSourceDao = dataSourceDao;
    }

    public void setRecordSourceDao(RecordSourceDao recordSourceDao) {
        this.recordSourceDao = recordSourceDao;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }


    @Override
    @Transactional
    public Task exportSource(int wsid, int wsversion, int dhid, String dbname, String dtname, String rewriter) {
        WorkspaceEdition workspaceEdition = cleaningWorkspaceDao.getWorkspaceEdition(wsid, wsversion);

        int rsid = workspaceEdition.getRsid();

        RecordSource recordSource = recordSourceDao.getRecordSource(rsid);

        DataSource dataSource = new DataSource(dhid, dbname, dtname);
        dataSourceDao.insertDataSource(dataSource);

        // TODO: Check if rewriters is valid
        Task task = taskDao.createExportTask(recordSource, dataSource, rewriter);
        workspaceEdition.setExportTask(task);

        cleaningWorkspaceDao.updateWorkspaceEdition(WorkspaceEdition.toPO(workspaceEdition));

        taskScheduler.schedule(task);

        return task;
    }

    @Override
    public Task getTask(int tid) {
        return taskDao.getTask(tid);
    }

    @Override
    public boolean stopTask(int tid) {
        return taskScheduler.stopTaskById(tid);
    }

    @Override
    public List<Task> getTasks(int page, int pageSize) {
        return taskDao.getTasks(page, pageSize);
    }

    @Override
    public List<Task> getRunningTasks(int page, int pageSize) {
        return taskDao.getTasks(page, pageSize, TaskStatus.RUNNING);
    }

    @Override
    public List<Task> getPendingTasks(int page, int pageSize) {
        return taskDao.getTasks(page, pageSize, TaskStatus.PENDING);
    }

    @Override
    public List<Task> getFinishedTasks(int page, int pageSize) {
        return taskDao.getTasks(page, pageSize, TaskStatus.FINISHED);
    }

    @Override
    public List<Task> getFailedTasks(int page, int pageSize) {
        return taskDao.getTasks(page, pageSize, TaskStatus.FAILED);
    }
}
