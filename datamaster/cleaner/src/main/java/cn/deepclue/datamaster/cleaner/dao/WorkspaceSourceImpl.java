package cn.deepclue.datamaster.cleaner.dao;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDatamasterSourceBO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspaceSourcePO;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by magneto on 17-5-16.
 */
@Repository("workspaceDataSourceDao")
public class WorkspaceSourceImpl implements WorkspaceSourceDao {
    @Autowired
    private WorkspaceSourceMapper workspaceSourceMapper;
    private final byte[] lock = new byte[0];

    /**
     * 如果工作空间和数据源的关联不存在，则插入，同时更新wsdsid，返回true
     * 如果存在，则更新wsdsid，返回false
     *
     * @param workspaceSource 工作空间和数据源的关联
     * @return 是否新增
     */
    @Override
    public boolean insertWorkspaceSource(WorkspaceSourcePO workspaceSource) {
        synchronized (lock) {
            WorkspaceSourcePO tempWorkspaceSource = workspaceSourceMapper.getWorkspaceSource(
                    workspaceSource.getWsid(), workspaceSource.getSid(), workspaceSource.getStype());
            if (tempWorkspaceSource == null) {
                if (workspaceSourceMapper.insertWorkspaceDataSource(workspaceSource)) {
                    return true;
                }

                throw new JdbcException(JdbcErrorEnum.INSERT);
            } else {
                workspaceSource.setWsdsid(tempWorkspaceSource.getWsdsid());
                return false;
            }
        }
    }

    @Override
    public Integer deleteByWsid(int wsid) {
        return workspaceSourceMapper.deleteByWsid(wsid);
    }

    @Override
    public boolean delete(int wsid, int sid, int stype) {
        if (workspaceSourceMapper.delete(wsid, sid, stype)) {
            return true;
        }

        throw new JdbcException(JdbcErrorEnum.DELETE);
    }

    @Override
    public List<WorkspaceSourcePO> getWorkspaceSourcesByWsid(int wsid) {
        return workspaceSourceMapper.getWorkspaceDataSourcesByWsid(wsid);
    }

    @Override
    public WorkspaceSourcePO getWorkspaceSource(int wsid, int sid, int stype) {
        return workspaceSourceMapper.getWorkspaceSource(wsid, sid, stype);
    }

    @Override
    public List<FusionDataSourceBO> getFusionDataSources(int fwsid) {
        return workspaceSourceMapper.getFusionDataSources(fwsid);
    }

    @Override
    public List<FusionDatamasterSourceBO> getFusionDatamasterSources(int fwsid) {
        return workspaceSourceMapper.getFusionDatamasterSources(fwsid);
    }

    @Override
    public int getSourcesCount(int fwsid) {
        return workspaceSourceMapper.getSourcesCount(fwsid);
    }

    @Override
    public List<String> getMultiMatchPtids(int fwsid) {
        return workspaceSourceMapper.getMultiMatchPtids(fwsid);
    }
}
