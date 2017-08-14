package cn.deepclue.datamaster.cleaner.service.cleaning.impl;

import cn.deepclue.datamaster.cleaner.config.CleanerConfigurationProperties;
import cn.deepclue.datamaster.cleaner.dao.WorkspaceDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.*;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordList;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.WorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.*;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.OntologyMappingPO;
import cn.deepclue.datamaster.cleaner.domain.po.cleaning.TransformationPO;
import cn.deepclue.datamaster.cleaner.domain.vo.data.RecordListVO;
import cn.deepclue.datamaster.cleaner.domain.vo.data.RecordVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.*;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import cn.deepclue.datamaster.cleaner.scheduler.TaskScheduler;
import cn.deepclue.datamaster.cleaner.service.cleaning.CleaningWorkspaceService;
import cn.deepclue.datamaster.cleaner.service.cleaning.RecordService;
import cn.deepclue.datamaster.cleaner.service.cleaning.TaskService;
import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.SimpleTransformer;
import cn.deepclue.datamaster.streamer.transform.Rewriter;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.Transformation;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;
import cn.deepclue.datamaster.streamer.transform.transformer.TransformerDef;
import cn.deepclue.datamaster.streamer.transform.transformer.field.ConvertTypeTF;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 17/03/2017.
 */
@Service("cleaningWorkspaceService")
public class CleaningWorkspaceServiceImpl implements CleaningWorkspaceService {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private CleaningWorkspaceDao cleaningWorkspaceDao;

    @Autowired
    private WorkspaceDao workspaceDao;

    @Autowired
    private DataSourceDao dataSourceDao;

    @Autowired
    private RecordSourceDao recordSourceDao;

    @Autowired
    private RecordDao recordDao;

    @Autowired
    private DataHouseDao dataHouseDao;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private CleanerConfigurationProperties properties;

    @Autowired
    @Qualifier(value = "taskDao")
    private TaskDao taskDao;

    @Override
    public RSSchema getTransformedSchema(int wsid, int wsversion) {
        WorkspaceEdition workspaceEdition = cleaningWorkspaceDao.getWorkspaceEdition(wsid, wsversion);

        int rsid = workspaceEdition.getRsid();

        // Get schema
        RSSchema rsSchema = recordSourceDao.getRSSchema(rsid);

        return getTransformedSchema(wsid, wsversion, rsSchema);
    }

    private RSSchema getTransformedSchema(int wsid, int wsversion, RSSchema rsSchema) {
        List<TransformationVO> transformationVOs = getTransformations(wsid, wsversion);
        List<Transformation> transformations = TransformationVO.toBOs(transformationVOs);

        SimpleTransformer simpleTransformer = new SimpleTransformer(transformations);

        return simpleTransformer.transformSchema(rsSchema);
    }

    @Override
    public RecordListVO getTransformedRecords(TransformedRecordsReqVO transformedRecordsReqVO) {

        int wsid = transformedRecordsReqVO.getWsid();
        int wsversion = transformedRecordsReqVO.getWsversion();
        WorkspaceEdition workspaceEdition = cleaningWorkspaceDao.getWorkspaceEdition(wsid, wsversion);

        int rsid = workspaceEdition.getRsid();

        // Get schema
        RSSchema rsSchema = recordSourceDao.getRSSchema(rsid);
        if (rsSchema == null) {
            throw new JdbcException("Failed to get rsschema with rsid: " + rsid,
                    "根据数据源id：" + rsid + "获取数据源业务模型失败。",
                    JdbcErrorEnum.SELECT);
        }

        // Get records
        RecordSource recordSource = recordSourceDao.getRecordSource(rsid);
        RecordList recordList = recordDao.getRecords(recordSource, rsSchema,
                transformedRecordsReqVO.getPage(), transformedRecordsReqVO.getPageSize(),
                transformedRecordsReqVO.getOrderBy(), transformedRecordsReqVO.getAsc(),
                FilterVO.toFilters(transformedRecordsReqVO.getFilters()));

        // Build transformers
        List<TransformationVO> transformationVOs = getTransformations(wsid, wsversion);
        List<Transformation> transformations = TransformationVO.toBOs(transformationVOs);

        SimpleTransformer simpleTransformer = new SimpleTransformer(transformations);

        List<Record> records = recordList.getRecords();
        List<Record> transformRecords = simpleTransformer.transformRecords(rsSchema, records);
        List<RecordVO> recordVOs = new ArrayList<>(records.size());
        for (Record record : transformRecords) {
            RecordVO recordVO = new RecordVO();
            for (Object value : record.getValues()) {
                recordVO.addValue(value);
            }
            recordVO.setSchema(record.getSchema());
            recordVOs.add(recordVO);
        }

        RecordListVO recordListVO = new RecordListVO();
        recordListVO.setTotal(recordList.getTotal());
        recordListVO.setRecords(recordVOs);

        return recordListVO;
    }

    @Override
    @Transactional
    public boolean addTransformation(TransformationVO transformationVO) {
        CleaningWorkspaceBO workspaceBO = (CleaningWorkspaceBO) workspaceDao.getWorkspace(transformationVO.getWsid());
        if (workspaceBO.getFinished() == FinishedStatus.FINISHED) {
            throw new CleanerException(BizErrorEnum.WORKSPACE_INVALID_OPERATION_AFTER_FINISH);
        }

        // Check if transformation is valid.
        Transformer transformer = TransformHelper.buildTransformer(TransformationVO.toBO(transformationVO));

        // Check if transformation is valid.
        RSSchema schema = getTransformedSchema(transformationVO.getWsid(), transformationVO.getWsversion());
        transformer.prepareSchema(schema);

        transformationVO.setArgs(TransformHelper.buildTransformerArgs(transformer));

        boolean result = cleaningWorkspaceDao.addTransformation(TransformationVO.toPO(transformationVO));

        // FIXME: How to make sure transformations are valid?
        TransformedRecordsReqVO transformedRecordsReqVO = new TransformedRecordsReqVO();
        transformedRecordsReqVO.setPage(0);
        transformedRecordsReqVO.setPageSize(10);
        transformedRecordsReqVO.setWsid(transformationVO.getWsid());
        transformedRecordsReqVO.setWsversion(transformationVO.getWsversion());
        getTransformedRecords(transformedRecordsReqVO);

        return result;
    }

    @Override
    @Transactional
    public int deleteTransformation(int wsid, int wsversion, int tfid) {
        WorkspaceEdition workspaceEdition = cleaningWorkspaceDao.getWorkspaceEdition(wsid, wsversion);
        RSSchema schema = recordSourceDao.getRSSchema(workspaceEdition.getRsid());

        List<TransformationVO> transformationVOs = getTransformations(wsid, wsversion);

        int i = 0;
        Transformer transformer = null;
        TransformationVO transformationVO = null;
        RSSchema target = schema;
        while (i < transformationVOs.size()) {
            transformationVO = transformationVOs.get(i++);
            transformer = TransformHelper.buildTransformer(TransformationVO.toBO(transformationVO));
            target = transformer.prepareSchema(target);

            if (transformationVO.getTfid() == tfid) {
                break;
            }
        }

        // Transformation does not exist.
        if (transformationVO == null) {
            return 0;
        }

        int nDelete = 0;
        for (; i < transformationVOs.size(); ++i) {
            TransformationVO followingTransformationVO = transformationVOs.get(i);
            Transformer followingTransformer =
                    TransformHelper.buildTransformer(TransformationVO.toBO(followingTransformationVO));

            target = followingTransformer.prepareSchema(target);
            if (transformer.isCascadedWith(followingTransformer)) {
                if (deleteTransformation(wsid, wsversion, followingTransformationVO)) {
                    nDelete++;
                }
            }
        }

        if (deleteTransformation(wsid, wsversion, transformationVO)) {
            nDelete++;
        }

        return nDelete;
    }

    private boolean deleteTransformation(int wsid, int wsversion, TransformationVO transformationVO) {
        // FIXME: We should not hard code tftype and tf arg here.
        switch (transformationVO.getTftype()) {
            case "AddFieldTF":
            case "DupFieldTF":
            case "ConcatFieldTF":
                Object targetFieldName = transformationVO.getArg("targetFieldName");
                if (targetFieldName != null && !"".equals(targetFieldName)) {
                    deleteOntologyMapping(wsid, wsversion, targetFieldName.toString());
                }
                break;
            case "ConvertTypeTF":
                Object sourceFieldName = transformationVO.getArg("sourceFieldName");
                if (sourceFieldName != null && !"".equals(sourceFieldName)) {
                    deleteOntologyMapping(wsid, wsversion, sourceFieldName.toString());
                }
                break;
        }

        return cleaningWorkspaceDao.deleteTransformation(transformationVO.getTfid());
    }

    private boolean deleteOntologyMapping(int wsid, int wsversion, String fieldName) {
        return cleaningWorkspaceDao.deleteOntologyMapping(wsid, wsversion, fieldName);
    }

    @Override
    public List<TransformationVO> getTransformations(int wsid, int wsversion) {
        List<TransformationPO> transformationPOs = cleaningWorkspaceDao.getTransformations(wsid, wsversion);
        List<TransformationVO> transformationVOs = new ArrayList<>();
        for (TransformationPO transformationPO : transformationPOs) {
            transformationVOs.add(TransformationVO.fromPO(transformationPO));
        }

        return transformationVOs;
    }

    @Override
    public boolean addRewriter(int uid, WorkspaceBO workspace, Rewriter rewriter) {
        return false;
    }

    @Override
    @Transactional
    public WorkspaceEdition startWorking(int wsid, int wsversion) {
        CleaningWorkspaceBO cleaningWorkspaceBO = (CleaningWorkspaceBO) workspaceDao.getWorkspace(wsid);

        if (cleaningWorkspaceBO.getFinished() == FinishedStatus.FINISHED) {
            throw new CleanerException(BizErrorEnum.WORKSPACE_INVALID_OPERATION_AFTER_FINISH);
        }

        if (cleaningWorkspaceBO.getEdition().getWsversion() != wsversion) {
            throw new CleanerException(BizErrorEnum.WORKSPACE_EDITION_INCONSIST);
        }
        // Fetch transformations and create a simple transformer
        List<TransformationVO> transformationVOs = getTransformations(wsid, wsversion);
        if (transformationVOs == null || transformationVOs.isEmpty()) {
            throw new CleanerException(BizErrorEnum.WORKSPACE_NO_RULERS);
        }

        List<Transformation> transformations = TransformationVO.toBOs(transformationVOs);

        SimpleTransformer simpleTransformer = new SimpleTransformer(transformations);

        // Prepare source and sink record source.
        RecordSource source = recordSourceDao.getRecordSource(cleaningWorkspaceBO.getEdition().getRsid());
        RecordSource sink = new RecordSource();
        BeanUtils.copyProperties(source, sink);
        sink.setRsid(null);
        recordSourceDao.insertRecordSource(sink);


        // Save transformed schema.
        RSSchema sourceSchema = recordSourceDao.getRSSchema(source.getRsid());
        RSSchema sinkSchema = simpleTransformer.transformSchema(sourceSchema);
        recordSourceDao.saveRSSchema(sink.getRsid(), sinkSchema);

        // Save transformations to task extra data.
        String transformationStr = TransformHelper.serializeTransformations(transformations);
        Task transformTask = taskDao.createTransformTask(source, sink, transformationStr);

        // Create Analysis Task
        Task analysisTask = taskDao.createAnalysisTask(sink);

        // Upgrade workspace edition with task id binding.
        WorkspaceEdition workspaceEdition = cleaningWorkspaceDao.upgradeWorkspaceEdition(cleaningWorkspaceBO, sink.getRsid(), transformTask.getTid());
        workspaceDao.updateWorkspaceStatus(wsid, FinishedStatus.UNFINISHED);

        workspaceEdition.setTransformTask(transformTask);
        workspaceEdition.setAnalysisTask(analysisTask);
        cleaningWorkspaceDao.updateWorkspaceEdition(WorkspaceEdition.toPO(workspaceEdition));

        // Schedule task
        taskScheduler.schedule(transformTask);
        taskScheduler.schedule(analysisTask);

        return workspaceEdition;
    }

    @Override
    public List<TransformerDef> getTransformerDefs(int type) {
        return cleaningWorkspaceDao.getTransformerDefs(type);
    }

    @Override
    public List<OntologyMapping> getOntologyMappings(int wsid, int wsversion) {
        return cleaningWorkspaceDao.getOntologyMappings(wsid, wsversion);
    }

    private int deleteConvertTypeTF(int wsid, int wsversion, String fieldName) {
        int deletedCount = 0;
        List<TransformationVO> transformations = getTransformations(wsid, wsversion);
        for (TransformationVO transformationVO : transformations) {
            Transformer transformer = TransformHelper.buildTransformer(TransformationVO.toBO(transformationVO));
            if (transformer instanceof ConvertTypeTF) {
                if (fieldName.equals(((ConvertTypeTF) transformer).getSourceFieldName())) {
                    deletedCount += deleteTransformation(wsid, wsversion, transformationVO.getTfid());
                }
            }
        }

        return deletedCount;
    }

    @Override
    @Transactional
    public boolean addOntologyMapping(AddOntologyMappingReqVO ontologyMappingReq) {

        int wsid = ontologyMappingReq.getWsid();
        int wsversion = ontologyMappingReq.getWsversion();
        String fieldName = ontologyMappingReq.getFieldName();

        OntologyMappingPO ontologyMappingPO = new OntologyMappingPO();
        BeanUtils.copyProperties(ontologyMappingReq, ontologyMappingPO);

        RSSchema schema = getTransformedSchema(wsid, wsversion);
        if (schema.getField(ontologyMappingReq.getFieldName()) == null) {
            // Emtpy mapping for this field.
            if (ontologyMappingReq.getPtid() == null) {
                return false;
            }
            throw new CleanerException(BizErrorEnum.UNKNOWN_FIELD_NAME_IN_TRANSFORMED_SCHEMA);
        }

        OntologyMapping ontologyMapping = cleaningWorkspaceDao.getOntologyMapping(wsid, wsversion, fieldName);
        if (ontologyMappingReq.getPtid() == null ||
                (ontologyMapping != null && ontologyMapping.getPropertyType() != null)) {
            deleteConvertTypeTF(ontologyMappingReq.getWsid(), ontologyMappingReq.getWsversion(), fieldName);
        }

        // FIXME: Make it thread-safety.
        if (cleaningWorkspaceDao.updateOntologyMapping(ontologyMappingPO)) {
            return true;
        }

        if (cleaningWorkspaceDao.insertOntologyMapping(ontologyMappingPO)) {
            return true;
        }

        throw new JdbcException(JdbcErrorEnum.ADD_ONTOLOGY);
    }

    @Override
    @Transactional
    public boolean bindOntology(int wsid, int wsversion, Integer otid) {
        WorkspaceEdition workspaceEdition = cleaningWorkspaceDao.getWorkspaceEdition(wsid, wsversion);
        if (workspaceEdition.getObjectType() != null) {
            // Delete all ontology mappings
            cleaningWorkspaceDao.deleteOntologyMappings(wsid, wsversion);
        }

        return cleaningWorkspaceDao.bindOntology(wsid, wsversion, otid);
    }

    @Override
    public boolean finishWorking(int wsid, int wsversion) {
        CleaningWorkspaceBO cleaningWorkspaceBO = (CleaningWorkspaceBO) workspaceDao.getWorkspace(wsid);
        if (cleaningWorkspaceBO.getEdition().getWsversion() != wsversion) {
            throw new CleanerException(BizErrorEnum.WS_VERSION_INCONSIST);
        }

        FinishedStatus status = cleaningWorkspaceBO.getFinished();
        if (status == FinishedStatus.FINISHED) {
            return true;
        }

        CleaningWorkspaceStatus taskStatus = cleaningWorkspaceBO.getStatus();
        if (taskStatus != CleaningWorkspaceStatus.FINISHED && taskStatus != CleaningWorkspaceStatus.IDLE) {
            throw new CleanerException(BizErrorEnum.TASK_STATUS_INVALID_FINISH);
        }

        // Set workspace status to finish
        if (!workspaceDao.updateWorkspaceStatus(wsid, FinishedStatus.FINISHED)) {
            throw new JdbcException("Failed to finish working.",
                    "完成清洗失败。"
                    , JdbcErrorEnum.UPDATE);
        }

        // create save task
        if (!WorkspaceEditionStatus.UNCLEANED.equals(cleaningWorkspaceBO.getEdition().getStatus())) {
            RecordSource recordSource = recordSourceDao.getRecordSource(cleaningWorkspaceBO.getEdition().getRsid());
            Task persistenceTask = taskDao.createPersistenceTask(recordSource);
            taskScheduler.schedule(persistenceTask);
        }

        return true;
    }

    @Override
    public TransformedTopValues getTransformedTopValues(int wsid, int wsversion, String fieldName) {
        WorkspaceEdition workspaceEdition = cleaningWorkspaceDao.getWorkspaceEdition(wsid, wsversion);
        int rsid = workspaceEdition.getRsid();
        RSSchema rsSchema = recordSourceDao.getRSSchema(rsid);
        RSSchema transformedSchema = getTransformedSchema(wsid, wsversion, rsSchema);

        TransformedTopValues transformedTopValues = new TransformedTopValues();
        // Do not have the field in original record source.
        if (rsSchema.getField(fieldName) == null && transformedSchema.getField(fieldName) != null) {
            transformedTopValues.setSupported(false);
        } else {
            transformedTopValues.setSupported(true);
            transformedTopValues.setTopValues(recordService.getTopValues(rsid, fieldName));
        }
        return transformedTopValues;
    }

    @Override
    public TransformedStats getTransformedStats(int wsid, int wsversion, String fieldName) {
        WorkspaceEdition workspaceEdition = cleaningWorkspaceDao.getWorkspaceEdition(wsid, wsversion);
        int rsid = workspaceEdition.getRsid();

        RSSchema rsSchema = recordSourceDao.getRSSchema(rsid);
        RSSchema transformedSchema = getTransformedSchema(wsid, wsversion, rsSchema);

        TransformedStats transformedStats = new TransformedStats();

        // Do not have the field in original record source.
        if (rsSchema.getField(fieldName) == null && transformedSchema.getField(fieldName) != null) {
            transformedStats.setSupported(false);
        } else {
            transformedStats.setSupported(true);
            transformedStats.setStats(recordService.stats(rsid, fieldName));
        }

        return transformedStats;
    }


}
