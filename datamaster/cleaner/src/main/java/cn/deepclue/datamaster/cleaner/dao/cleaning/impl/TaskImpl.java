package cn.deepclue.datamaster.cleaner.dao.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.TaskDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.task.TaskStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.task.TaskType;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by magneto on 17-4-8.
 */
@Repository("taskDao")
public class TaskImpl implements TaskDao {

    @Autowired
    private TaskMapper taskMapper;

    private Task newPendingTask() {
        Task task = new Task();
        task.setCreatedOn(new Date());
        task.setModifiedOn(new Date());
        task.setTaskStatus(TaskStatus.PENDING);

        return task;
    }

    @Override
    public Task createImportTask(DataSource dataSource, RecordSource recordSource) {
        Task task = newPendingTask();
        task.setName(recordSource.getName());
        task.setSinkId(recordSource.getRsid());
        task.setSourceId(dataSource.getDsid());
        task.setTaskType(TaskType.IMPORT);

        insertTask(task);

        return task;
    }

    public void insertTask(Task task) {
        if (!taskMapper.insertTask(task)) {
            throw new JdbcException(JdbcErrorEnum.CREATE_TASK);
        }
    }

    @Override
    public Task createExportTask(RecordSource source, DataSource sink, String rewriters) {
        Task task = newPendingTask();
        task.setSourceId(source.getRsid());
        task.setSinkId(sink.getDsid());
        task.setTaskType(TaskType.EXPORT);
        task.setName(source.getName());
        task.setExtraData(rewriters);

        insertTask(task);

        return task;
    }

    @Override
    public Task createTransformTask(RecordSource source, RecordSource sink, String extraData) {
        Task task = newPendingTask();
        task.setSourceId(source.getRsid());
        task.setSinkId(sink.getRsid());
        task.setTaskType(TaskType.TRANSFORM);
        task.setName(source.getName());
        task.setExtraData(extraData);

        insertTask(task);

        return task;
    }

    @Override
    public Task createAnalysisTask(RecordSource recordSource) {
        Task task = newPendingTask();
        task.setName(recordSource.getName());
        task.setSinkId(recordSource.getRsid());
        task.setSourceId(recordSource.getRsid());
        task.setTaskType(TaskType.ANALYSIS);

        insertTask(task);

        return task;
    }

    @Override
    public Task createPersistenceTask(RecordSource recordSource) {
        Task task = newPendingTask();
        task.setName(recordSource.getName());
        task.setSourceId(recordSource.getRsid());
        task.setSinkId(recordSource.getRsid());
        task.setTaskType(TaskType.PERSISTENCE);

        insertTask(task);

        return task;
    }

    @Override
    public Task createSimilarityComputeTask(FusionWorkspaceBO fusionWorkspaceBO) {
        Task task = newPendingTask();
        task.setTaskType(TaskType.SIMILARITY_COMPUTE);
        task.setName(fusionWorkspaceBO.getName());
        task.setSinkId(fusionWorkspaceBO.getRecordSource().getRsid());
        task.setExtraData(String.valueOf(fusionWorkspaceBO.getWsid()));

        insertTask(task);

        return task;
    }

    @Override
    public Task createEntropyComputeTask(FusionWorkspaceBO fusionWorkspaceBO) {
        Task task = newPendingTask();
        task.setTaskType(TaskType.ENTROPY_COMPUTE);
        task.setName(fusionWorkspaceBO.getName());
        task.setSinkId(fusionWorkspaceBO.getRecordSource().getRsid());
        task.setExtraData(String.valueOf(fusionWorkspaceBO.getWsid()));

        insertTask(task);
        return task;
    }

    @Override
    public Task getTask(int tid) {
        return taskMapper.getTask(tid);
    }

    @Override
    public List<Task> getTasks(int page, int pageSize) {
        return taskMapper.getAllTasks(page * pageSize, pageSize);
    }

    @Override
    public List<Task> getTasks(int page, int pageSize, TaskStatus taskStatus) {
        return taskMapper.getTasks(page * pageSize, pageSize, taskStatus.getValue());
    }

    @Override
    public boolean update(Task task) {
        return taskMapper.update(task);
    }
}
