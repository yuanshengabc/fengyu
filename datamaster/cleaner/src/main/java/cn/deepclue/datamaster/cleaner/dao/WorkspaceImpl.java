package cn.deepclue.datamaster.cleaner.dao;

import cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.DataSourceMapper;
import cn.deepclue.datamaster.cleaner.domain.WorkspaceType;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.WorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.CleaningWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspacePO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspaceSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.CleaningWorkspacePO;
import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.FusionWorkspacePO;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import cn.deepclue.datamaster.cleaner.utils.SqlUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 31/03/2017.
 */
@Repository("workspaceDao")
public class WorkspaceImpl implements WorkspaceDao {

    @Autowired
    private WorkspaceMapper workspaceMapper;

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Transactional
    @Override
    public boolean insertWorkspace(WorkspaceBO workspaceBO) {
        if (workspaceBO.getWstype() == WorkspaceType.CLEANING) {
            CleaningWorkspaceBO cleaningWorkspaceBO = (CleaningWorkspaceBO) workspaceBO;
            insertCleaningWorkspace(cleaningWorkspaceBO);
        } else {
            FusionWorkspaceBO fusionWorkspaceBO = (FusionWorkspaceBO) workspaceBO;
            insertFusionWorkspace(fusionWorkspaceBO);
        }

        return true;
    }

    @Override
    public boolean insertWorkspace(WorkspacePO workspacePO) {
        if (!workspaceMapper.insertWorkspace(workspacePO)) {
            throw new JdbcException(JdbcErrorEnum.CREATE_WORKSPACE);
        }

        return true;
    }

    @Override
    public boolean insertWorkspace(CleaningWorkspacePO cleaningWorkspacePO) {
        if (!workspaceMapper.insertCleaningWorkspace(cleaningWorkspacePO)) {
            throw new JdbcException(JdbcErrorEnum.CREATE_WORKSPACE);
        }

        return true;
    }

    @Override
    public boolean insertWorkspace(FusionWorkspacePO fusionWorkspacePO) {
        if (!workspaceMapper.insertFusionWorkspace(fusionWorkspacePO)) {
            throw new JdbcException(JdbcErrorEnum.CREATE_WORKSPACE);
        }

        return true;
    }

    @Transactional
    WorkspaceBO insertCleaningWorkspace(CleaningWorkspaceBO cleaningWorkspaceBO) {
        //save workspace
        WorkspacePO workspacePO = new WorkspacePO();
        BeanUtils.copyProperties(cleaningWorkspaceBO, workspacePO);
        workspacePO.setWstype(cleaningWorkspaceBO.getWstype().getType());
        if (!workspaceMapper.insertWorkspace(workspacePO)) {
            throw new JdbcException(JdbcErrorEnum.CREATE_WORKSPACE);
        }
        cleaningWorkspaceBO.setWsid(workspacePO.getWsid());

        //save cleaning workspace
        CleaningWorkspacePO cleaningWorkspacePO = new CleaningWorkspacePO();
        BeanUtils.copyProperties(cleaningWorkspaceBO, cleaningWorkspacePO);
        if (!workspaceMapper.insertCleaningWorkspace(cleaningWorkspacePO)) {
            throw new JdbcException(JdbcErrorEnum.CREATE_WORKSPACE);
        }

        //save datasource
        DataSource dataSource = cleaningWorkspaceBO.getDataSource();
        if (!dataSourceMapper.insertDataSource(dataSource)) {
            throw new JdbcException(JdbcErrorEnum.CREATE_WORKSPACE);
        }

        //bind workspace <--> datasource
        WorkspaceSourcePO workspaceSourcePO = new WorkspaceSourcePO();
        workspaceSourcePO.setWsid(workspacePO.getWsid());
        workspaceSourcePO.setSid(dataSource.getDsid());
        if (!workspaceMapper.insertWorkspaceSource(workspaceSourcePO)) {
            throw new JdbcException(JdbcErrorEnum.CREATE_WORKSPACE);
        }

        return cleaningWorkspaceBO;
    }

    @Transactional
    WorkspaceBO insertFusionWorkspace(FusionWorkspaceBO fusionWorkspaceBO) {
        //save workspace
        WorkspacePO workspacePO = new WorkspacePO();
        BeanUtils.copyProperties(fusionWorkspaceBO, workspacePO);
        workspacePO.setWstype(fusionWorkspaceBO.getWstype().getType());
        if (!workspaceMapper.insertWorkspace(workspacePO)) {
            throw new JdbcException(JdbcErrorEnum.CREATE_WORKSPACE);
        }

        FusionWorkspacePO fusionWorkspacePO = new FusionWorkspacePO();
        BeanUtils.copyProperties(fusionWorkspaceBO, fusionWorkspacePO);
        if (!workspaceMapper.insertFusionWorkspace(fusionWorkspacePO)) {
            throw new JdbcException(JdbcErrorEnum.CREATE_WORKSPACE);
        }

        return fusionWorkspaceBO;
    }

    @Override
    public List<WorkspaceBO> getWorkspaces(int page, int pageSize, WorkspaceType wstype, FinishedStatus status, String keyword) {
        List<WorkspacePO> workspacePOs = workspaceMapper.getWorkspaces(page * pageSize, pageSize,
                wstype == null ? null : wstype.getType(), status == null ? null : status.getValue(),
                SqlUtils.likeEscape(keyword, true, true));
        return getWorkspaces(workspacePOs);
    }

    private List<WorkspaceBO> getWorkspaces(List<WorkspacePO> workspacePOs) {
        List<WorkspaceBO> workspaceBOs = new ArrayList<>();
        for (WorkspacePO workspacePO : workspacePOs) {
            int wsid = workspacePO.getWsid();
            WorkspaceBO workspaceBO = getWorkspace(wsid);
            workspaceBOs.add(workspaceBO);
        }

        return workspaceBOs;
    }

    @Transactional
    @Override
    public WorkspaceBO getWorkspace(int wsid) {
        WorkspaceBO workspaceBO = null;
        WorkspacePO workspacePO = workspaceMapper.getWorkspace(wsid);
        if (workspacePO != null) {
            if (workspacePO.getWstype() == WorkspaceType.CLEANING.getType()) {
                workspaceBO = workspaceMapper.getCleaningWorkspace(wsid);
            } else {
                workspaceBO = workspaceMapper.getFusionWorkspace(wsid);
            }
        }

        if (workspaceBO == null) {
            throw new JdbcException(JdbcErrorEnum.ID_NOT_FOUND);
        }

        return workspaceBO;
    }

    @Override
    public Integer getWorkspacesCount(WorkspaceType wstype, FinishedStatus status, String keyword) {
        return workspaceMapper.getWorkspacesCount(wstype == null ? null : wstype.getType(),
                status == null ? null : status.getValue(),
                SqlUtils.likeEscape(keyword, true, true));
    }

    @Override
    public boolean updateWorkspace(int wsid, String name, String description) {
        return workspaceMapper.updateWorkspace(wsid, name, description);
    }

    @Override
    public boolean deleteFusionWorkspace(int wsid) {
        return workspaceMapper.deleteFusionWorkspace(wsid);
    }

    @Override
    public boolean deleteCleaningWorkspace(int wsid) {
        return workspaceMapper.deleteCleaningWorkspace(wsid);
    }

    @Override
    public boolean updateWorkspaceStatus(int wsid, FinishedStatus status) {
        if (workspaceMapper.updateWorkspaceStatus(wsid, status.getValue())) {
            return true;
        }
        throw new JdbcException(JdbcErrorEnum.UPDATE);
    }
}
