package cn.deepclue.datamaster.cleaner.dao.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.CleaningWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.OntologyMapping;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEdition;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.OntologyMappingPO;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.TransformationPO;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.WorkspaceEditionPO;
import cn.deepclue.datamaster.streamer.transform.transformer.TransformerDef;

import java.util.List;

/**
 * Created by magneto on 17-5-18.
 */
public interface CleaningWorkspaceDao {
    List<TransformationPO> getTransformations(int wsid, int wsversion);

    List<TransformerDef> getTransformerDefs(int type);

    boolean addTransformation(TransformationPO transformation);

    boolean deleteTransformation(int tfid);

    List<OntologyMapping> getOntologyMappings(Integer wsid, Integer wsversion);

    boolean updateOntologyMapping(OntologyMappingPO ontologyMappingPO);

    OntologyMapping getOntologyMapping(int wsid, int wsversion, String fieldName);

    boolean insertOntologyMapping(OntologyMappingPO ontologyMappingPO);

    void insertWorkspaceEdition(WorkspaceEditionPO workspaceEditionPO);

    boolean updateWorkspaceEdition(WorkspaceEditionPO workspaceEditionPO);

    WorkspaceEdition getWorkspaceEdition(int wsid, int wsversion);
    List<WorkspaceEdition> getWorkspaceEditions(int wsid);
    boolean deleteWorkspaceEditions(int wsid);

    boolean deleteOntologyMappings(int wsid, int wsversion);
    boolean deleteOntologyMapping(int wsid, int wsversion, String fieldName);

    WorkspaceEdition upgradeWorkspaceEdition(CleaningWorkspaceBO workspace, int rsid, int tid);

    boolean bindOntology(int wsid, int wsversion, Integer otid);

    boolean deleteKakfa(String topic);
}
