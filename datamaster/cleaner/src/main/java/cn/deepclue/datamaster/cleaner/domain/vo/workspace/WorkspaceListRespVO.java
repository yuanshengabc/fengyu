package cn.deepclue.datamaster.cleaner.domain.vo.workspace;

import java.util.List;

/**
 * Created by xuzb on 13/04/2017.
 */
public class WorkspaceListRespVO {
    private int wscount;
    private int cleaningCount;
    private int fusionCount;
    private List<WorkspaceVO> workspaces;

    public int getWscount() {
        return wscount;
    }

    public void setWscount(int wscount) {
        this.wscount = wscount;
    }

    public int getCleaningCount() {
        return cleaningCount;
    }

    public void setCleaningCount(int cleaningCount) {
        this.cleaningCount = cleaningCount;
    }

    public int getFusionCount() {
        return fusionCount;
    }

    public void setFusionCount(int fusionCount) {
        this.fusionCount = fusionCount;
    }

    public List<WorkspaceVO> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<WorkspaceVO> workspaces) {
        this.workspaces = workspaces;
    }
}
