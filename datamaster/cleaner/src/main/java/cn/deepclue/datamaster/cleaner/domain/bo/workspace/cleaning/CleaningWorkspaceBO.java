package cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning;

import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.WorkspaceBO;

/**
 * Created by xuzb on 14/03/2017.
 */
public class CleaningWorkspaceBO extends WorkspaceBO {
    private DataSource dataSource;
    private WorkspaceEdition edition;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public CleaningWorkspaceStatus getStatus() {
        if (getFinished() == FinishedStatus.FINISHED) {
            return CleaningWorkspaceStatus.FINISHED;
        }

        if (edition.getTransformTask() != null) {
            switch (edition.getTransformTask().getTaskStatus()) {
                case RUNNING:
                case PENDING:
                    return CleaningWorkspaceStatus.TRANSFORMING;
                case FAILED:
                    return CleaningWorkspaceStatus.TRANSFORM_FAILED;
            }
        }

        if (edition.getImportTask() != null) {
            switch (edition.getImportTask().getTaskStatus()) {
                case RUNNING:
                case PENDING:
                    return CleaningWorkspaceStatus.IMPORTING;
                case FAILED:
                    return CleaningWorkspaceStatus.IMPORT_FAILED;
            }
        }

        if (edition.getAnalysisTask() != null) {
            switch (edition.getAnalysisTask().getTaskStatus()) {
                case RUNNING:
                case PENDING:
                    return CleaningWorkspaceStatus.ANALYZING;
                case FAILED:
                    return CleaningWorkspaceStatus.ANALYSIS_FAILED;
            }
        }

        if (edition.getExportTask() != null) {
            switch (edition.getExportTask().getTaskStatus()) {
                case RUNNING:
                case PENDING:
                    return CleaningWorkspaceStatus.EXPORTING;
                case FAILED:
                    return CleaningWorkspaceStatus.EXPORT_FAILD;
            }
        }


        return CleaningWorkspaceStatus.IDLE;
    }

    public WorkspaceEdition getEdition() {
        return edition;
    }

    public void setEdition(WorkspaceEdition edition) {
        this.edition = edition;
    }
}
