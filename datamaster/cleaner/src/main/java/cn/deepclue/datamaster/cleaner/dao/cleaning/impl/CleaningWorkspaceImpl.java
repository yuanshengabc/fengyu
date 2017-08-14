package cn.deepclue.datamaster.cleaner.dao.cleaning.impl;

import cn.deepclue.datamaster.cleaner.config.CleanerConfigurationProperties;
import cn.deepclue.datamaster.cleaner.dao.cleaning.CleaningWorkspaceDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.CleaningWorkspaceMapper;
import cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.TransformationMapper;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.CleaningWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.OntologyMapping;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEdition;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.OntologyMappingPO;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.TransformationPO;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.WorkspaceEditionPO;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import cn.deepclue.datamaster.model.ontology.ObjectType;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.KafkaConfig;
import cn.deepclue.datamaster.streamer.io.KafkaHelper;
import cn.deepclue.datamaster.streamer.session.KafkaZkSession;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.transformer.TransformerDef;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by magneto on 17-5-18.
 */
@Service("cleaningWorkspaceDao")
public class CleaningWorkspaceImpl implements CleaningWorkspaceDao {
    @Autowired
    private CleanerConfigurationProperties properties;

    @Autowired
    private CleaningWorkspaceMapper cleaningWorkspaceMapper;

    @Autowired
    private TransformationMapper transformationMapper;

    @Override
    public List<TransformationPO> getTransformations(int wsid, int wsversion) {
        return transformationMapper.getTransformations(wsid, wsversion);
    }

    @Override
    public List<TransformerDef> getTransformerDefs(int type) {
        if (type == 0) {
            return TransformHelper.buildCommonTransformerDefs();
        } else if (type == 1){
            return TransformHelper.buildDsrTransformerDefs();
        } else {
            return TransformHelper.buildFieldTransformerDefs();
        }
    }

    @Override
    public boolean addTransformation(TransformationPO transformation) {
        if (transformationMapper.insertTransformation(transformation)) {
            return true;
        }

        throw new JdbcException(JdbcErrorEnum.CREATE_TRANS);
    }

    @Override
    public boolean deleteTransformation(int tfid) {
        return transformationMapper.deleteTransformation(tfid);
    }

    @Override
    public List<OntologyMapping> getOntologyMappings(Integer wsid, Integer wsversion) {
        return cleaningWorkspaceMapper.getOntologyMappings(wsid, wsversion);
    }

    @Override
    public boolean updateOntologyMapping(OntologyMappingPO ontologyMapping) {
        return cleaningWorkspaceMapper.updateOntologyMapping(ontologyMapping);
    }

    @Override
    public OntologyMapping getOntologyMapping(int wsid, int wsversion, String fieldName) {
        return cleaningWorkspaceMapper.getOntologyMapping(wsid, wsversion, fieldName);
    }

    @Override
    public boolean insertOntologyMapping(OntologyMappingPO ontologyMapping) {
        return cleaningWorkspaceMapper.insertOntologyMapping(ontologyMapping);
    }

    @Override
    public void insertWorkspaceEdition(WorkspaceEditionPO workspaceEditionPO) {
        if (!cleaningWorkspaceMapper.insertWorkspaceEdition(workspaceEditionPO)) {
            throw new JdbcException(JdbcErrorEnum.CREATE_WORKSPACE);
        }
    }

    @Override
    public boolean updateWorkspaceEdition(WorkspaceEditionPO workspaceEditionPO) {
        return cleaningWorkspaceMapper.updateWorkspaceEdition(workspaceEditionPO);
    }

    @Override
    public WorkspaceEdition getWorkspaceEdition(int wsid, int wsversion) {
        WorkspaceEdition workspaceEdition = cleaningWorkspaceMapper.getWorkspaceEdition(wsid, wsversion);
        if (workspaceEdition == null) {
            throw new JdbcException(JdbcErrorEnum.ID_NOT_FOUND);
        }

        return workspaceEdition;
    }

    @Override
    public List<WorkspaceEdition> getWorkspaceEditions(int wsid) {
        List<WorkspaceEdition> wsEdtions = cleaningWorkspaceMapper.getWorkspaceEditions(wsid);
        if (wsEdtions == null) {
            throw new JdbcException(JdbcErrorEnum.ID_NOT_FOUND);
        }

        return wsEdtions;
    }

    @Override
    public boolean deleteOntologyMappings(int wsid, int wsversion) {
        return cleaningWorkspaceMapper.deleteOntologyMappings(wsid, wsversion);
    }

    @Override
    public boolean deleteOntologyMapping(int wsid, int wsversion, String fieldName) {
        return cleaningWorkspaceMapper.deleteOntologyMapping(wsid, wsversion, fieldName);
    }

    private void duplicateOntologyMappings(int wsid, int oldWsversion) {
        List<OntologyMappingPO> ontologyMappings = cleaningWorkspaceMapper.getOntologyMappingPOs(wsid, oldWsversion);
        // No ontology mappings to be dup.
        if (ontologyMappings == null || ontologyMappings.isEmpty()) {
            return;
        }

        for (OntologyMappingPO ontologyMapping : ontologyMappings) {
            ontologyMapping.setOmid(null);
            ontologyMapping.setWsversion(oldWsversion + 1);
        }

        cleaningWorkspaceMapper.insertOntologyMappings(ontologyMappings);
    }

    @Override
    public boolean deleteWorkspaceEditions(int wsid) {
        return cleaningWorkspaceMapper.deleteWorkspaceEditions(wsid);
    }

    @Override
    public WorkspaceEdition upgradeWorkspaceEdition(CleaningWorkspaceBO cleaningWorkspaceBO, int rsid, int tid) {
        int wsid = cleaningWorkspaceBO.getWsid();
        WorkspaceEdition workspaceEdition = cleaningWorkspaceBO.getEdition();
        int oldVersion = workspaceEdition.getWsversion();

        WorkspaceEditionPO workspaceEditionPO = new WorkspaceEditionPO();
        BeanUtils.copyProperties(workspaceEdition, workspaceEditionPO);
        workspaceEditionPO.setWsversion(oldVersion + 1);
        workspaceEditionPO.setRsid(rsid);
        //TODO ZR
        //workspaceEditionPO.setTid(tid);
        workspaceEditionPO.setModifiedOn(new Date());

        ObjectType objectType = workspaceEdition.getObjectType();
        if (objectType != null) {
            workspaceEditionPO.setOtid(objectType.getOtid());
        }

        if (cleaningWorkspaceMapper.insertWorkspaceEdition(workspaceEditionPO)) {
            if (cleaningWorkspaceMapper.upgradeWorkspaceVersion(wsid, oldVersion)) {
                // Duplicate ontology mapping
                bindOntology(wsid, workspaceEditionPO.getWsversion(), workspaceEditionPO.getOtid());

                duplicateOntologyMappings(wsid, oldVersion);

                return getWorkspaceEdition(wsid, oldVersion + 1);
            } else {
                throw new JdbcException(JdbcErrorEnum.UPDATE_WORKSPACE_EDITION);
            }
        } else {
            throw new JdbcException(JdbcErrorEnum.CREATE_WORKSPACE_EDITION);
        }
    }

    @Override
    public boolean bindOntology(int wsid, int wsversion, Integer otid) {
        return cleaningWorkspaceMapper.bindOntology(wsid, wsversion, otid);
    }

    @Override
    public boolean deleteKakfa(String topic) {
        KafkaConfig kafkaConfig = properties.getKafkaConfig();
        KTopicConfig kTopicConfig = new KTopicConfig();
        kTopicConfig.setKconfig(kafkaConfig);
        kTopicConfig.setTopic(topic);
        return KafkaZkSession.deleteTopic(kTopicConfig);
    }
}
