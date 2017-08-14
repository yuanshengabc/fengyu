package cn.deepclue.datamaster.cleaner.dao;

import cn.deepclue.datamaster.cleaner.domain.WorkspaceType;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.WorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspacePO;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.CleaningWorkspacePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.FusionWorkspacePO;

import java.util.List;

/**
 * Created by xuzb on 17/03/2017.
 */
public interface WorkspaceDao {
    boolean insertWorkspace(WorkspaceBO workspaceBO);

    boolean insertWorkspace(WorkspacePO workspacePO);

    boolean insertWorkspace(CleaningWorkspacePO workspacePO);

    boolean insertWorkspace(FusionWorkspacePO workspacePO);

    List<WorkspaceBO> getWorkspaces(int page, int pageSize, WorkspaceType wstype, FinishedStatus status, String keyword);

    WorkspaceBO getWorkspace(int wsid);

    Integer getWorkspacesCount(WorkspaceType wstype, FinishedStatus status, String keyword);

    boolean updateWorkspace(int wsid, String name, String description);

    boolean deleteCleaningWorkspace(int wsid);

    boolean deleteFusionWorkspace(int wsid);

    boolean updateWorkspaceStatus(int wsid, FinishedStatus finished);
}
