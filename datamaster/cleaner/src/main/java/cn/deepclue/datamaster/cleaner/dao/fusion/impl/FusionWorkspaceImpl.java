package cn.deepclue.datamaster.cleaner.dao.fusion.impl;

import cn.deepclue.datamaster.cleaner.dao.WorkspaceMapper;
import cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.OntologyMapper;
import cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.RecordSourceMapper;
import cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TaskMapper;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.mapper.FusionWorkspaceMapper;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.fusion.AddressCodeType;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspacePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.FusionWorkspacePO;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by magneto on 17-5-17.
 */
@Repository("fusionWorkspaceDao")
public class FusionWorkspaceImpl implements FusionWorkspaceDao {
    @Autowired
    private WorkspaceMapper workspaceMapper;
    @Autowired
    private FusionWorkspaceMapper fusionWorkspaceMapper;
    @Autowired
    private OntologyMapper ontologyMapper;
    @Autowired
    private RecordSourceMapper recordSourceMapper;
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public FusionWorkspaceBO getWorkspace(int fswid) {
        FusionWorkspaceBO fusionWorkspaceBO = new FusionWorkspaceBO();
        //工作空间
        WorkspacePO workspacePO = workspaceMapper.getWorkspace(fswid);
        BeanUtils.copyProperties(workspacePO, fusionWorkspaceBO);
        //融合工作空间
        FusionWorkspacePO fusionWorkspacePO = fusionWorkspaceMapper.getFusionWorkspace(fswid);
        BeanUtils.copyProperties(fusionWorkspacePO, fusionWorkspaceBO);
        if (fusionWorkspacePO.getAddressCodeType() != null) {
            fusionWorkspaceBO.setAddressCodeType(AddressCodeType.getAddressCodeType(fusionWorkspacePO.getAddressCodeType()));
        }
        //业务模型
        if (fusionWorkspacePO.getOtid() != null) {
            ObjectTypeBO objectTypeBO = ontologyMapper.getObjectTypeBO(fusionWorkspacePO.getOtid());
            fusionWorkspaceBO.setObjectTypeBO(objectTypeBO);
        }
        //融合结果
        if (fusionWorkspacePO.getRsid() != null) {
            RecordSource recordSource = recordSourceMapper.getRecordSource(fusionWorkspacePO.getRsid());
            fusionWorkspaceBO.setRecordSource(recordSource);
        }

        //任务信息
        if (fusionWorkspacePO.getEntropyCalculationTid() != null) {
            Task entropyTask = taskMapper.getTask(fusionWorkspacePO.getEntropyCalculationTid());
            fusionWorkspaceBO.setEntropyCalculationTask(entropyTask);
        }

        if (fusionWorkspacePO.getFusionTid() != null) {
            Task fusionTask = taskMapper.getTask(fusionWorkspacePO.getFusionTid());
            fusionWorkspaceBO.setSimilarityTask(fusionTask);
        }

        return fusionWorkspaceBO;
    }

    @Override
    public FusionWorkspacePO getFusionWorkspaceByRsid(int rsid) {
        return fusionWorkspaceMapper.getFusionWorkspaceByRsid(rsid);
    }

    @Override
    public boolean updateObjectType(int fwsid, Integer otid) {
        if (fusionWorkspaceMapper.updateObjectType(fwsid, otid)) {
            return true;
        }
        throw new JdbcException(JdbcErrorEnum.UPDATE);
    }

    @Override
    public boolean updateAddressCodeType(int fwsid, AddressCodeType addressCodeType) {
        if (fusionWorkspaceMapper.updateAddressCodeType(fwsid, addressCodeType == null ? null : addressCodeType.getAddressCode())) {
            return true;
        }

        throw new JdbcException(JdbcErrorEnum.UPDATE);
    }

    @Override
    public boolean updateStep(int fwsid, int step) {
        if (fusionWorkspaceMapper.updateStep(fwsid, step)) {
            return true;
        }

        throw new JdbcException(JdbcErrorEnum.UPDATE);
    }

    public boolean updateThreshold(int fwsid, Double threshold) {
        if (fusionWorkspaceMapper.updateThreshold(fwsid, threshold)) {
            return true;
        }

        throw new JdbcException(JdbcErrorEnum.UPDATE);
    }

    @Override
    public boolean updateSimilarityTask(int fwsid, Integer tid) {
        if (fusionWorkspaceMapper.updateSimilarityTask(fwsid, tid)) {
            return true;
        }

        throw new JdbcException(JdbcErrorEnum.UPDATE);
    }

    @Override
    public boolean updateEntropyCalculationTask(int fwsid, Integer tid) {
        if (fusionWorkspaceMapper.updateEntropyCalculationTask(fwsid, tid)) {
            return true;
        }

        throw new JdbcException(JdbcErrorEnum.UPDATE);
    }
}
