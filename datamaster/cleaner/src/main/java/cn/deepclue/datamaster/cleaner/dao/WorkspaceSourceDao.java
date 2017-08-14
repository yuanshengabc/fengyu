package cn.deepclue.datamaster.cleaner.dao;


import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDatamasterSourceBO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspaceSourcePO;

import java.util.List;

/**
 * Created by magneto on 17-5-16.
 */
public interface WorkspaceSourceDao {
    boolean insertWorkspaceSource(WorkspaceSourcePO workspaceSourcePO);

    Integer deleteByWsid(int wsid);

    boolean delete(int wsid, int sid, int stype);

    List<WorkspaceSourcePO> getWorkspaceSourcesByWsid(int wsid);

    WorkspaceSourcePO getWorkspaceSource(int wsid, int sid, int stype);

    List<FusionDataSourceBO> getFusionDataSources(int fwsid);

    List<FusionDatamasterSourceBO> getFusionDatamasterSources(int fwsid);

    int getSourcesCount(int wsid);

    List<String> getMultiMatchPtids(int fwsid);
}
