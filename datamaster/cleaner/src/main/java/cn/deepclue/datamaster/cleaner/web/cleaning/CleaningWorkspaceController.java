package cn.deepclue.datamaster.cleaner.web.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.OntologyMapping;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEdition;
import cn.deepclue.datamaster.cleaner.domain.vo.data.RecordListVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.*;
import cn.deepclue.datamaster.cleaner.service.cleaning.CleaningWorkspaceService;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.transform.transformer.TransformerDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by magneto on 17-5-15.
 */
@RestController
public class CleaningWorkspaceController {
    @Autowired
    private CleaningWorkspaceService cleaningWorkspaceService;

    @RequestMapping(value = "/cleaningWorkspace/{wsid}/editions/{wsversion}/transform", method = RequestMethod.POST)
    public WorkspaceEdition startWorking(@PathVariable("wsid") Integer wsid, @PathVariable("wsversion") Integer wsversion) {
        return this.cleaningWorkspaceService.startWorking(wsid, wsversion);
    }

    @RequestMapping("/cleaningWorkspace/{wsid}/editions/{wsversion}/finish_workspace")
    public boolean finishWorking(@PathVariable("wsid") Integer wsid, @PathVariable("wsversion") Integer wsversion) {
        return cleaningWorkspaceService.finishWorking(wsid, wsversion);
    }

    @RequestMapping(path = "/cleaningWorkspace/{wsid}/editions/{wsversion}/transformations")
    public List<TransformationVO> getTransformations(@PathVariable("wsid") int wsid,
                                                     @PathVariable("wsversion") int wsversion) {
        return cleaningWorkspaceService.getTransformations(wsid, wsversion);
    }

    @RequestMapping(path = "/cleaningWorkspace/{wsid}/editions/{wsversion}/transformations", method = RequestMethod.POST)
    public boolean addTransformation(@RequestBody TransformationVO transformation) {
        return cleaningWorkspaceService.addTransformation(transformation);
    }

    @RequestMapping(path = "/cleaningWorkspace/{wsid}/editions/{wsversion}/transformations/{tfid}", method = RequestMethod.DELETE)
    public boolean deleteTransformation(@PathVariable("wsid") int wsid,
                                        @PathVariable("wsversion") int wsversion,
                                        @PathVariable("tfid") int tfid) {
        return cleaningWorkspaceService.deleteTransformation(wsid, wsversion, tfid) > 0;
    }

    @RequestMapping(path = "/transformer_defs")
    public List<TransformerDef> getTransformerDefs(@RequestParam(defaultValue = "0") int type) {
        return cleaningWorkspaceService.getTransformerDefs(type);
    }

    @RequestMapping(path = "/cleaningWorkspace/{wsid}/editions/{wsversion}/ontology_mappings")
    public List<OntologyMapping> getOntologyMappings(@PathVariable("wsid") int wsid,
                                                     @PathVariable("wsversion") int wsversion) {
        return cleaningWorkspaceService.getOntologyMappings(wsid, wsversion);
    }

    @RequestMapping(path = "/cleaningWorkspace/{wsid}/editions/{wsversion}/ontology_mappings", method = RequestMethod.POST)
    public boolean addOntologyMapping(@Valid AddOntologyMappingReqVO ontologyMappingReq) {
        return cleaningWorkspaceService.addOntologyMapping(ontologyMappingReq);
    }

    @RequestMapping(path = "/cleaningWorkspace/{wsid}/editions/{wsversion}/transformed_schema")
    public RSSchema getTransformedSchema(@PathVariable("wsid") int wsid,
                                         @PathVariable("wsversion") int wsversion) {
        return cleaningWorkspaceService.getTransformedSchema(wsid, wsversion);
    }

    // FIXME: filters may be too long if we use GET method.
    @RequestMapping(path = "/cleaningWorkspace/{wsid}/editions/{wsversion}/transformed_records", method = RequestMethod.POST)
    public RecordListVO getTransformedRecords(@RequestBody TransformedRecordsReqVO transformedRecordsReqVO) {
        return cleaningWorkspaceService.getTransformedRecords(transformedRecordsReqVO);
    }

    @RequestMapping(path = "/cleaningWorkspace/{wsid}/editions/{wsversion}/ontology", method = RequestMethod.POST)
    public boolean bindOntology(@PathVariable("wsid") int wsid,
                                @PathVariable("wsversion") int wsversion,
                                @RequestParam Integer otid) {
        return cleaningWorkspaceService.bindOntology(wsid, wsversion, otid);
    }

    @RequestMapping(path = "/cleaningWorkspace/{wsid}/editions/{wsversion}/transformed_top_values")
    public TransformedTopValues transformedTopValues(@PathVariable("wsid") int wsid,
                                                     @PathVariable("wsversion") int wsversion,
                                                     @RequestParam String fieldName) {
        return cleaningWorkspaceService.getTransformedTopValues(wsid, wsversion, fieldName);
    }

    @RequestMapping(path = "/cleaningWorkspace/{wsid}/editions/{wsversion}/transformed_stats")
    public TransformedStats transformedStats(@PathVariable("wsid") int wsid,
                                             @PathVariable("wsversion") int wsversion,
                                             @RequestParam String fieldName) {
        return cleaningWorkspaceService.getTransformedStats(wsid, wsversion, fieldName);
    }
}
