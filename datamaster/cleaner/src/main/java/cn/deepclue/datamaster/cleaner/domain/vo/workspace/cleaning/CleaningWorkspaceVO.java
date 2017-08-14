package cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning;

import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.task.TaskStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.CleaningWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.CleaningWorkspaceStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEdition;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.WorkspaceVO;
import org.springframework.beans.BeanUtils;

/**
 * Created by magneto on 17-5-15.
 */
public class CleaningWorkspaceVO extends WorkspaceVO {
    private CleaningWorkspaceStatus status;
    private WorkspaceEdition edition;
    private DataSource dataSource;

    public CleaningWorkspaceStatus getStatus() {
        return status;
    }

    public void setStatus(CleaningWorkspaceStatus status) {
        this.status = status;
    }

    public WorkspaceEdition getEdition() {
        return edition;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setEdition(WorkspaceEdition edition) {
        this.edition = edition;
    }

    public WorkspaceVO fromBO(CleaningWorkspaceBO cleaningWorkspaceBO) {
        BeanUtils.copyProperties(cleaningWorkspaceBO, this);
        //如果清洗未完成，返回的修改时间设置为上次清洗任务（包括清洗和分析）完成（包括完成和失败）的时间
        if (status != CleaningWorkspaceStatus.FINISHED) {
            if (edition != null && edition.getWsversion() > 0) {
                Task analysisTask = edition.getAnalysisTask();
                if (analysisTask != null &&
                        (analysisTask.getTaskStatus() == TaskStatus.FINISHED || analysisTask.getTaskStatus() == TaskStatus.FAILED)) {
                    setModifiedOn(analysisTask.getModifiedOn());
                }
            }
        }
        return this;
    }
}
