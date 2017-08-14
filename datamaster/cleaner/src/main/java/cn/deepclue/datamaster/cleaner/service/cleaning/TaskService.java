package cn.deepclue.datamaster.cleaner.service.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;

import java.util.List;

/**
 * Created by xuzb on 14/03/2017.
 */
public interface TaskService {

    /**
     * Export source to target
     * @param wsid workspace id
     * @param wsversion workspace edition id
     * @param dhid target data house
     * @param dbname database name
     * @param dtname table name
     * @return running task
     */
    Task exportSource(int wsid, int wsversion, int dhid, String dbname, String dtname, String rewriter);

    Task getTask(int tid);

    boolean stopTask(int tid);

    List<Task> getTasks(int page, int pageSize);

    List<Task> getRunningTasks(int page, int pageSize);
    List<Task> getPendingTasks(int page, int pageSize);
    List<Task> getFinishedTasks(int page, int pageSize);
    List<Task> getFailedTasks(int page, int pageSize);
}
