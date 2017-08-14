package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.domain.WorkspaceType;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.CreateWorkspaceReqVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.WorkspaceListRespVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.WorkspaceVO;

/**
 * Created by xuzb on 14/03/2017.
 */
public interface WorkspaceService {
    /**
     * Get  workspace list by status.
     * @param page page index starting from 0
     * @param pageSize page size
     * @return list of finished workspace
     */
    WorkspaceListRespVO getWorkspaces(int page, int pageSize,  WorkspaceType wstype, FinishedStatus status, String keyword);

    /**
     * create workspace from a data source
     * @param workspace workspace info without wsid
     * @return the new workspace
     */
    int createWorkspace(CreateWorkspaceReqVO workspace);

    WorkspaceVO getWorkspace(int wsid);

    boolean updateWorkspace(int wsid, String name, String description);

    boolean deleteWorkspace(int wsid);
}
