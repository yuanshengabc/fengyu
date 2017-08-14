package cn.deepclue.datamaster.cleaner.service.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.WorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.OntologyMapping;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEdition;
import cn.deepclue.datamaster.cleaner.domain.vo.data.RecordListVO;
import cn.deepclue.datamaster.cleaner.domain.vo.data.RecordVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.*;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.transform.Rewriter;
import cn.deepclue.datamaster.streamer.transform.transformer.TransformerDef;

import java.util.List;

/**
 * Created by magneto on 17-5-15.
 */
public interface CleaningWorkspaceService {
    /**
     * Start data transform.
     * @param wsid workspace id.
     * @return working task.
     */
    WorkspaceEdition startWorking(int wsid, int wsversion);

    boolean finishWorking(int wsid, int wsversion);

    List<OntologyMapping> getOntologyMappings(int wsid, int wsversion);

    /**
     * Get tagged fields in ontology
     * @param wsid current workspace
     * @param wsversion workspace version
     * @return list of transformed fields
     */
    RSSchema getTransformedSchema(int wsid, int wsversion);

    /**
     * Get transformed records
     * @return Get transformed records
     */
    RecordListVO getTransformedRecords(TransformedRecordsReqVO transformedRecordsReqVO);

    /**
     * Add a new transformer
     * @param transformation the new transformation
     * @return true if success else false.
     */
    boolean addTransformation(TransformationVO transformation);

    /**
     * Delete transformation by tfid
     * @param tfid the transformation id.
     * @return the number of transformations that are deleted.
     */
    int deleteTransformation(int wsid, int wsversion, int tfid);

    /**
     * Get transformers in current workspace edition.
     * @param wsid current workspace
     * @param wsversion workspace version
     * @return
     */
    List<TransformationVO> getTransformations(int wsid, int wsversion);

    /**
     * Add a new rewriter
     * @param uid operating user id
     * @param workspace current workspace
     * @param rewriter the new rewriter to rename field name.
     * @return true if success else false.
     */
    boolean addRewriter(int uid, WorkspaceBO workspace, Rewriter rewriter);

    List<TransformerDef> getTransformerDefs(int type);

    boolean addOntologyMapping(AddOntologyMappingReqVO ontologyMappingReq);

    boolean bindOntology(int wsid, int wsversion, Integer otid);

    TransformedTopValues getTransformedTopValues(int wsid, int wsversion, String fieldName);

    TransformedStats getTransformedStats(int wsid, int wsversion, String fieldName);
}
