package cn.deepclue.datamaster.cleaner.dao.cleaning;

import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.task.TaskStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;

import java.util.List;

/**
 * Created by xuzb on 17/03/2017.
 * 用于访问Task
 */
public interface TaskDao {
    Task createImportTask(DataSource source, RecordSource sink);
    Task createExportTask(RecordSource recordSource, DataSource dataSource, String rewriters);
    Task createTransformTask(RecordSource source, RecordSource sink, String extraData);
    Task createAnalysisTask(RecordSource recordSource);
    Task createSimilarityComputeTask(FusionWorkspaceBO fusionWorkspaceBO);
    Task createEntropyComputeTask(FusionWorkspaceBO fusionWorkspaceBO);
    Task createPersistenceTask(RecordSource recordSource);

    Task getTask(int tid);

    List<Task> getTasks(int page, int pageSize);
    List<Task> getTasks(int page, int pageSize, TaskStatus taskStatus);

    boolean update(Task task);
}
