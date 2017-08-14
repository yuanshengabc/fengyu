package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.config.CleanerConfigurationProperties;
import cn.deepclue.datamaster.cleaner.dao.WorkspaceSourceDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordSourceDao;
import cn.deepclue.datamaster.cleaner.domain.WorkspaceType;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordList;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.CleaningWorkspaceStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.OntologyMapping;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEdition;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.cleaning.WorkspaceEditionStatus;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspaceSourcePO;
import cn.deepclue.datamaster.cleaner.domain.vo.data.RecordListVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.CreateWorkspaceReqVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.WorkspaceListRespVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.WorkspaceVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.AddOntologyMappingReqVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.CleaningWorkspaceVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.TransformationVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.TransformedRecordsReqVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionWorkspaceVO;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import cn.deepclue.datamaster.cleaner.scheduler.TaskScheduler;
import cn.deepclue.datamaster.cleaner.service.cleaning.CleaningWorkspaceService;
import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.KafkaConfig;
import cn.deepclue.datamaster.streamer.io.KafkaHelper;
import cn.deepclue.datamaster.streamer.transform.TransformHelper;
import cn.deepclue.datamaster.streamer.transform.filter.Filter;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;
import cn.deepclue.datamaster.streamer.transform.transformer.field.ConvertTypeTF;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by xuzb on 31/03/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WorkspaceServiceIntegTests {
    @Autowired
    private CleanerConfigurationProperties properties;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private CleaningWorkspaceService cleaningWorkspaceService;

    @Autowired
    private WorkspaceSourceDao workspaceSourceDao;

    @MockBean
    private TaskScheduler taskScheduler;

    @MockBean
    private RecordDao recordDao;

    @Autowired
    private RecordSourceDao recordSourceDao;

    @Test
    public void testGetWorkspaces() {
        WorkspaceListRespVO workspaceListRespVO = workspaceService.getWorkspaces(0, 10, null, null, null);
        assertThat(workspaceListRespVO.getWorkspaces().size()).isEqualTo(6);
        assertThat(workspaceListRespVO.getCleaningCount()).isEqualTo(2);
        assertThat(workspaceListRespVO.getFusionCount()).isEqualTo(4);
        assertThat(workspaceListRespVO.getWscount()).isEqualTo(6);

        workspaceListRespVO = workspaceService.getWorkspaces(0, 10, WorkspaceType.CLEANING, null, null);
        assertThat(workspaceListRespVO.getWorkspaces().size()).isEqualTo(2);
        assertThat(workspaceListRespVO.getCleaningCount()).isEqualTo(2);
        assertThat(workspaceListRespVO.getFusionCount()).isEqualTo(4);
        assertThat(workspaceListRespVO.getWscount()).isEqualTo(6);

        workspaceListRespVO = workspaceService.getWorkspaces(0, 10, WorkspaceType.CLEANING, FinishedStatus.UNFINISHED, null);
        assertThat(workspaceListRespVO.getWorkspaces().size()).isEqualTo(1);
        assertThat(workspaceListRespVO.getCleaningCount()).isEqualTo(1);
        assertThat(workspaceListRespVO.getFusionCount()).isEqualTo(4);
        assertThat(workspaceListRespVO.getWscount()).isEqualTo(5);

        workspaceListRespVO = workspaceService.getWorkspaces(0, 10, WorkspaceType.FUSION, FinishedStatus.UNFINISHED, "step");
        assertThat(workspaceListRespVO.getWorkspaces().size()).isEqualTo(4);
        assertThat(workspaceListRespVO.getCleaningCount()).isEqualTo(0);
        assertThat(workspaceListRespVO.getFusionCount()).isEqualTo(4);
        assertThat(workspaceListRespVO.getWscount()).isEqualTo(4);
    }

    @Test
    public void testGetWorkspace() {
        CleaningWorkspaceVO cleaningWorkspaceVO = (CleaningWorkspaceVO) workspaceService.getWorkspace(1);
        assertThat(cleaningWorkspaceVO.getWsid()).isEqualTo(1);
        assertThat(cleaningWorkspaceVO.getWstype()).isEqualTo(WorkspaceType.CLEANING);
        assertThat(cleaningWorkspaceVO.getEdition().getWsversion()).isEqualTo(1);
        assertThat(cleaningWorkspaceVO.getEdition().getObjectType().getOtid()).isEqualTo(1);
        assertThat(cleaningWorkspaceVO.getEdition().getImportTask().getTid()).isEqualTo(1);
        assertThat(cleaningWorkspaceVO.getEdition().getAnalysisTask().getTid()).isEqualTo(2);
        assertThat(cleaningWorkspaceVO.getEdition().getTransformTask().getTid()).isEqualTo(3);
        assertThat(cleaningWorkspaceVO.getEdition().getExportTask().getTid()).isEqualTo(4);
        assertThat(cleaningWorkspaceVO.getDataSource()).isNotNull();

        FusionWorkspaceVO fusionWorkspaceVO = (FusionWorkspaceVO) workspaceService.getWorkspace(3);
        assertThat(fusionWorkspaceVO.getWsid()).isEqualTo(3);
        assertThat(fusionWorkspaceVO.getWstype()).isEqualTo(WorkspaceType.FUSION);
        assertThat(fusionWorkspaceVO.getStep()).isEqualTo(FusionStep.ONTOLOGY_SELECTION);
        //assertThat(fusionWorkspaceVO.getDataSources().size()).isEqualTo(2);
    }

    @Test
    public void testGetTransformations() {
        List<TransformationVO> transformations = cleaningWorkspaceService.getTransformations(1, 1);

        for (TransformationVO transformation : transformations) {
            assertThat(transformation.getWsid()).isEqualTo(1);
            assertThat(transformation.getWsversion()).isEqualTo(1);
        }
    }

    @Test
    public void testAddTransformation() {
        TransformationVO transformation = new TransformationVO();
        transformation.setWsid(1);
        transformation.setWsversion(1);
        transformation.setFilters(null);
        transformation.addArg("sourceFieldName", "f1");
        transformation.setTftype("ToUpperTF");

        RecordList recordList = new RecordList();
        recordList.setRecords(new ArrayList<>());
        when(recordDao.getRecords(any(RecordSource.class), any(RSSchema.class),
                any(Integer.class), any(Integer.class), anyString(), any(Boolean.class), anyListOf(Filter.class))).thenReturn(recordList);

        assertThat(cleaningWorkspaceService.addTransformation(transformation)).isTrue();

        TransformationVO dupFieldTF = new TransformationVO();
        dupFieldTF.setWsid(1);
        dupFieldTF.setWsversion(1);
        dupFieldTF.setFilters(null);
        dupFieldTF.addArg("sourceFieldName", "f1");
        dupFieldTF.setTftype("DupFieldTF");

        assertThat(cleaningWorkspaceService.addTransformation(dupFieldTF)).isTrue();

        assertThat(dupFieldTF.getArg("targetFieldName")).isEqualTo("f1_COPY");
    }

    @Test
    public void testDeleteTransformation() {
        // Delete single transformation
        assertThat(cleaningWorkspaceService.deleteTransformation(1, 1, 1)).isEqualTo(1);

        // Delete cascade transformation
        assertThat(cleaningWorkspaceService.deleteTransformation(1, 1, 2)).isEqualTo(2);

        // Delete split tf
        assertThat(cleaningWorkspaceService.deleteTransformation(1, 1, 4)).isEqualTo(3);
    }

    @Test
    public void testGetTransformedSchema() {
        RSSchema rsSchema = cleaningWorkspaceService.getTransformedSchema(1, 1);
        assertThat(rsSchema.getField("f1")).isNotNull();
        assertThat(rsSchema.getField("f2")).isNotNull();
    }

    @Test
    public void testGetTransformedRecords() {

        RSSchema schema = new RSSchema();
        schema.addField(new RSField("f1", BaseType.TEXT));
        schema.addField(new RSField("f2", BaseType.INT));
        schema.addField(new RSField("field1", BaseType.TEXT));
        schema.addField(new RSField("field2", BaseType.INT));
        schema.addField(new RSField("field3", BaseType.DATE));

        Record record = new Record(schema);
        record.addValue("aBc");
        record.addValue(10);
        record.addValue(null);
        record.addValue(null);
        record.addValue(null);

        List<Record> records = new ArrayList<>();
        records.add(record);

        RecordList recordList = new RecordList();
        recordList.setRecords(records);

        when(recordDao.getRecords(any(RecordSource.class), any(RSSchema.class),
                any(Integer.class), any(Integer.class), anyString(), any(Boolean.class), anyListOf(Filter.class))).thenReturn(recordList);

        TransformedRecordsReqVO transformedRecordsReqVO = new TransformedRecordsReqVO();
        transformedRecordsReqVO.setWsid(1);
        transformedRecordsReqVO.setWsversion(1);
        RecordListVO targetRecords = cleaningWorkspaceService.getTransformedRecords(transformedRecordsReqVO);

        assertThat(targetRecords.getRecords().size()).isEqualTo(1);
    }

    @Test
    public void testGetOntologyMappings() {
        List<OntologyMapping> ontologyMappingList = cleaningWorkspaceService.getOntologyMappings(1, 1);
        for (OntologyMapping ontologyMapping : ontologyMappingList) {
            assertThat(ontologyMapping.getPropertyType()).isNotNull();
        }
    }

    @Test
    public void testAddOntologyMapping() {
        AddOntologyMappingReqVO ontologyMappingReqDTO = new AddOntologyMappingReqVO();
        ontologyMappingReqDTO.setFieldName("field2");
        ontologyMappingReqDTO.setPtid(null);
        ontologyMappingReqDTO.setWsid(1);
        ontologyMappingReqDTO.setWsversion(1);

        assertThat(cleaningWorkspaceService.addOntologyMapping(ontologyMappingReqDTO)).isTrue();

        List<TransformationVO> transformations = cleaningWorkspaceService.getTransformations(1, 1);
        for (TransformationVO transformationVO : transformations) {
            Transformer transformer = TransformHelper.buildTransformer(TransformationVO.toBO(transformationVO));
            if (transformer instanceof ConvertTypeTF) {
                assertThat(((ConvertTypeTF) transformer).getSourceFieldName()).isNotEqualTo("field2");
            }
        }
    }

    @Test
    public void testCreateFusionWorkspace() {
        CreateWorkspaceReqVO workspaceReqDTO = new CreateWorkspaceReqVO();
        workspaceReqDTO.setName("fwstest1");
        workspaceReqDTO.setDescription("fwsdescription-test1");
        workspaceReqDTO.setWstype(WorkspaceType.FUSION);

        int wsid = workspaceService.createWorkspace(workspaceReqDTO);

        WorkspaceVO workspaceVO = workspaceService.getWorkspace(wsid);
        assertThat(workspaceVO.getWstype()).isEqualTo(WorkspaceType.FUSION);

        FusionWorkspaceVO fusionWorkspaceVO = (FusionWorkspaceVO) workspaceVO;
        assertThat(fusionWorkspaceVO.getRecordSource()).isNotNull();
        assertThat(fusionWorkspaceVO.getStep()).isEqualTo(FusionStep.ONTOLOGY_SELECTION);
        assertThat(fusionWorkspaceVO.getName()).isEqualTo("fwstest1");
        assertThat(fusionWorkspaceVO.getDescription()).isEqualTo("fwsdescription-test1");
        assertThat(fusionWorkspaceVO.getDataSources()).isNull();
        assertThat(fusionWorkspaceVO.getObjectType()).isNull();
        assertThat(fusionWorkspaceVO.getCreatedOn()).isNotNull();
    }

    @Test
    public void testCreateWorkspace() {
        Task task = new Task();
        task.setTid(1);
        when(taskScheduler.schedule(any(Task.class))).thenReturn(true);

        CreateWorkspaceReqVO workspaceReqDTO = new CreateWorkspaceReqVO();
        workspaceReqDTO.setUid(1);
        workspaceReqDTO.setName("wstest8");
        workspaceReqDTO.setDescription("wsdescription-test");
        workspaceReqDTO.setWstype(WorkspaceType.CLEANING);

        workspaceReqDTO.setDbname("test1");
        workspaceReqDTO.setDtname("test1");
        workspaceReqDTO.setDhid(1);
        int wsid = workspaceService.createWorkspace(workspaceReqDTO);

        WorkspaceVO workspaceVO = workspaceService.getWorkspace(wsid);
        assertThat(workspaceVO.getWstype()).isEqualTo(WorkspaceType.CLEANING);

        CleaningWorkspaceVO cleaningWorkspaceVO = (CleaningWorkspaceVO) workspaceVO;
        assertThat(cleaningWorkspaceVO.getStatus()).isEqualTo(CleaningWorkspaceStatus.IMPORTING);
        assertThat(cleaningWorkspaceVO.getDataSource()).isNotNull();
        assertThat(cleaningWorkspaceVO.getDataSource().getDsid()).isEqualTo(1);
        assertThat(cleaningWorkspaceVO.getEdition().getWsversion()).isEqualTo(0);
        //assertThat(cleaningWorkspaceVO.getEdition().getRsid()).isEqualTo(3);
        assertThat(cleaningWorkspaceVO.getEdition().getStatus()).isEqualTo(WorkspaceEditionStatus.UNCLEANED);
        assertThat(cleaningWorkspaceVO.getEdition().getObjectType()).isNull();
        assertThat(cleaningWorkspaceVO.getEdition().getImportTask()).isNotNull();
        assertThat(cleaningWorkspaceVO.getEdition().getAnalysisTask()).isNotNull();
        assertThat(cleaningWorkspaceVO.getEdition().getTransformTask()).isNull();
        assertThat(cleaningWorkspaceVO.getEdition().getExportTask()).isNull();
    }

    @Test
    public void testDeleteWorkspace() {
        when(recordDao.deleteIndex(anyString())).thenReturn(true);
        RecordSource recordSource = recordSourceDao.getRecordSource(2);
        KafkaConfig kafkaConfig = properties.getKafkaConfig();
        KTopicConfig kTopicConfig = new KTopicConfig();
        kTopicConfig.setKconfig(kafkaConfig);
        kTopicConfig.setTopic(recordSource.getKafkaTopic());
        KafkaHelper.createTopic(kTopicConfig);

        boolean ret = workspaceService.deleteWorkspace(2);
        assertThat(ret).isTrue();

        List<WorkspaceSourcePO> workspaceSourcePOs = workspaceSourceDao.getWorkspaceSourcesByWsid(2);
        assertThat(workspaceSourcePOs.size()).isEqualTo(0);

        recordSource = recordSourceDao.getRecordSource(2);
        assertThat(recordSource).isNull();

        //Add funsion workspace delete
        assertThat(workspaceService.deleteWorkspace(3)).isTrue();

        assertThatThrownBy(() -> {
            workspaceService.getWorkspace(3);
        }).isInstanceOf(JdbcException.class);

        assertThat(workspaceSourceDao.getWorkspaceSourcesByWsid(2).size()).isEqualTo(0);
    }

    @Test
    public void testBindOntology() {
        assertThat(cleaningWorkspaceService.bindOntology(1, 1, 1)).isTrue();
        CleaningWorkspaceVO workspace = (CleaningWorkspaceVO) workspaceService.getWorkspace(1);
        assertThat(workspace.getEdition().getObjectType().getOtid()).isEqualTo(1);
    }

    @Test
    public void testStartWorking() {
        when(taskScheduler.schedule(any(Task.class))).thenReturn(true);

        WorkspaceEdition workspaceEdition = cleaningWorkspaceService.startWorking(1, 1);
        CleaningWorkspaceVO workspace = (CleaningWorkspaceVO) workspaceService.getWorkspace(1);

        assertThat(workspace.getEdition().getWsversion()).isEqualTo(workspaceEdition.getWsversion());
    }

    @Test
    public void testFinishWorking() {
        when(taskScheduler.schedule(any(Task.class))).thenReturn(true);
        assertThat(cleaningWorkspaceService.finishWorking(2, 1));
    }

}
