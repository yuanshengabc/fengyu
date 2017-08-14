package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordSourceDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.EntropyPropertyDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FromSourceDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.SourceType;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.fusion.AddressCodeType;
import cn.deepclue.datamaster.cleaner.domain.fusion.EntropyCalculationStatus;
import cn.deepclue.datamaster.cleaner.domain.fusion.FusionTaskStatus;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.*;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.cleaner.scheduler.TaskScheduler;
import cn.deepclue.datamaster.cleaner.service.fusion.DatamasterSourceService;
import cn.deepclue.datamaster.cleaner.service.fusion.EntropyService;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionWorkspaceService;
import cn.deepclue.datamaster.fusion.domain.FieldEntropy;
import cn.deepclue.datamaster.fusion.domain.IntervalNumber;
import cn.deepclue.datamaster.fusion.domain.PreviewStats;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * 融合工作空间集成测试
 * Created by sunxingwen on 17-5-19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FusionWorkspaceServiceIntegTests {
    @Autowired
    private FusionWorkspaceService fusionWorkspaceService;
    @MockBean
    private FusionDao fusionDao;
    @MockBean
    private TaskScheduler taskScheduler;
    @Autowired
    private EntropyPropertyDao entropyPropertyDao;
    @Autowired
    private EntropyService entropyService;
    @Autowired
    private FusionWorkspaceDao fusionWorkspaceDao;
    @Autowired
    private RecordSourceDao recordSourceDao;
    @Autowired
    private FromSourceDao fromSourceDao;
    @Autowired
    private DatamasterSourceService datamasterSourceService;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private final int step1Fwsid = 3;
    private final int step2Fwsid = 4;
    private final int step3Fwsid = 5;
    private final int step4Fwsid = 6;

    @Test
    public void selectedObjectTypeTest() {
        //selectObjectType
        int otid = 3;
        boolean result = fusionWorkspaceService.selectObjectType(step1Fwsid, otid);
        assertThat(result).isEqualTo(true);

        //getSelectedObjectType
        FusionObjectTypeVO fusionObjectTypeVO = fusionWorkspaceService.getSelectedObjectType(step1Fwsid);
        assertThat(fusionObjectTypeVO.getOtid()).isEqualTo(otid);
        List<FusionPropertyTypeVO> fusionPropertyTypeVOs = fusionObjectTypeVO.getPropertyTypes();
        assertThat(fusionPropertyTypeVOs.size()).isGreaterThan(0);
    }

    @Test
    public void dataSourcesTest() {
        //addDataSources
        DataSourcesResqVO dataSourcesResqVO = prepareDataSourcesResqVO();

        boolean result = fusionWorkspaceService.addDataSources(step2Fwsid, dataSourcesResqVO);
        assertThat(result).isTrue();

        //getSelectedDataSources
        List<FusionDataSourceVO> datasources = fusionWorkspaceService.getSelectedDataSources(step2Fwsid);
        assertThat(datasources.size()).isGreaterThan(0);

        //fetchExternalDataSources
        FusionExternalDataSourceListVO fusionExternalDataSourceListVO = fusionWorkspaceService.fetchExternalDataSources(step2Fwsid, 1, "test1", 0, 10);
        assertThat(fusionExternalDataSourceListVO.getDsCount()).isGreaterThan(0);

        //getDatamasterSources
        FusionDatamasterSourceListVO fusionDatamasterSourceListVO = fusionWorkspaceService.getDatamasterSources(step2Fwsid, 0, 10);
        assertThat(fusionDatamasterSourceListVO.getDsCount()).isGreaterThan(0);

        //deleteDataSource
        result = fusionWorkspaceService.deleteDataSource(step2Fwsid, 1, SourceType.DATASOURCE.name());
        assertThat(result).isTrue();

        result = fusionWorkspaceService.deleteDataSource(step2Fwsid, 1, SourceType.DATAMASTER_SOURCE.name());
        assertThat(result).isTrue();
    }

    @Test
    public void fusionStatusTest1() {
        //updateFusionStatus
        boolean result = fusionWorkspaceService.updateFusionStatus(step3Fwsid, AddressCodeType.MULTI_ADDRESS_CODE.name());
        assertThat(result).isTrue();

        //getFusionInfo
        FusionInfoVO fusionInfoVO = fusionWorkspaceService.getFusionInfo(step3Fwsid);
        assertThat(fusionInfoVO.getAddressCodeType()).isEqualTo(AddressCodeType.MULTI_ADDRESS_CODE);
    }

    @Test
    public void fusionStatusTest2() {
        //previousStep
        boolean result = fusionWorkspaceService.previousStep(step4Fwsid);
        assertThat(result).isTrue();

        //updateFusionStatus
        result = fusionWorkspaceService.updateFusionStatus(step4Fwsid, AddressCodeType.SINGLE_ADDRESS_CODE.name());
        assertThat(result).isTrue();

        //getFusionInfo
        FusionInfoVO fusionInfoVO = fusionWorkspaceService.getFusionInfo(step4Fwsid);
        assertThat(fusionInfoVO.getAddressCodeType()).isEqualTo(AddressCodeType.SINGLE_ADDRESS_CODE);
        assertThat(fusionInfoVO.getThreshold()).isNull();

        //getEntropyTable
        EntropyTableVO entropyTable = fusionWorkspaceService.getEntropyTable(step4Fwsid);
        for (EntropyFieldVO entropyField : entropyTable.getPropertyTypes()) {
            assertThat(entropyField.getSelected()).isFalse();
            assertThat(entropyField.getUnique()).isFalse();
        }
    }

    @Test
    public void startEntropyCalculationTest() {
        //startEntropyCalculation
        when(taskScheduler.schedule(any(Task.class))).thenReturn(true);
        boolean result = fusionWorkspaceService.startEntropyCalculation(step3Fwsid);
        assertThat(result).isTrue();

        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(step3Fwsid);
        assertThat(fusionWorkspaceBO.getEntropyCalculationTask()).isNotNull();

        //getFusionInfo
        FusionInfoVO fusionInfoVO = fusionWorkspaceService.getFusionInfo(step3Fwsid);
        assertThat(fusionInfoVO.getEntropyCalculationStatus()).isEqualTo(EntropyCalculationStatus.UNCALCULATED);
    }

    @Test
    public void entropyTableTest() {
        //updateEntropyFields
        List<Integer> ptids = new ArrayList<>();
        ptids.add(11);
        ptids.add(12);
        ptids.add(13);
        Double threshold = 1.0;
        Integer uniquedPtid = 11;
        boolean result = fusionWorkspaceService.updateEntropyFields(step3Fwsid, AddressCodeType.MULTI_ADDRESS_CODE.name(), ptids, threshold, uniquedPtid);
        assertThat(result).isTrue();

        //getFusionInfo
        FusionInfoVO fusionInfoVO = fusionWorkspaceService.getFusionInfo(step3Fwsid);
        assertThat(fusionInfoVO.getAddressCodeType()).isEqualTo(AddressCodeType.MULTI_ADDRESS_CODE);
        assertThat(fusionInfoVO.getThreshold()).isEqualTo(1.0, offset(0.01));

        //getEntropyTable
        List<FieldEntropy> fieldEntropies = getMockFieldEntropies();
        when(fusionDao.getFieldEntropies(any(RecordSource.class))).thenReturn(fieldEntropies);

        EntropyTableVO entropyTableVO = fusionWorkspaceService.getEntropyTable(step3Fwsid);
        assertThat(entropyTableVO.getOtid()).isEqualTo(3);
        assertThat(entropyTableVO.getName()).isEqualToIgnoringCase("otmatch");
        assertThat(entropyTableVO.getPropertyTypes().size()).isEqualTo(5);

        List<EntropyFieldVO> entropyFieldVOs = entropyTableVO.getPropertyTypes();
        for (EntropyFieldVO entropyFieldVO : entropyFieldVOs) {
            switch (entropyFieldVO.getPtid()) {
                case 11:
                    assertThat(entropyFieldVO.getSelected()).isTrue();
                    assertThat(entropyFieldVO.getUnique()).isTrue();
                    assertThat(entropyFieldVO.getEntropy()).isEqualTo(1.0, offset(0.01));
                    break;
                case 12:
                    assertThat(entropyFieldVO.getSelected()).isTrue();
                    assertThat(entropyFieldVO.getUnique()).isFalse();
                    assertThat(entropyFieldVO.getEntropy()).isEqualTo(2.0, offset(0.01));
                    break;
                case 13:
                    assertThat(entropyFieldVO.getSelected()).isTrue();
                    assertThat(entropyFieldVO.getUnique()).isFalse();
                    assertThat(entropyFieldVO.getEntropy()).isEqualTo(0.0, offset(0.01));
                    break;
                default:
                    assertThat(entropyFieldVO.getSelected()).isFalse();
                    assertThat(entropyFieldVO.getUnique()).isFalse();
                    assertThat(entropyFieldVO.getEntropy()).isEqualTo(0.0, offset(0.01));
            }
        }
    }

    @Test
    public void getFusionSourcesTest() {
        FusionSourcesVO fusionSourcesVO = datamasterSourceService.getFusionSources();
        assertThat(fusionSourcesVO.getFusionSources().size()).isGreaterThanOrEqualTo(2);
        assertThat(fusionSourcesVO.getHdfs()).isEqualToIgnoringCase("hdfs://172.24.8.117:9000");
    }

    @Test
    public void getAllDataSourcesTest() {
        int dmsid = 2;
        List<FusionKeyDataSourceVO> fusionKeyDataSourceVOs = fusionWorkspaceService.getAllDataSources(dmsid);
        assertThat(fusionKeyDataSourceVOs.size()).isEqualTo(3);
    }

    @Test
    public void saveFusionResultTest() {
        assertThat(fromSourceDao.getAllFromSources().size()).isEqualTo(5);
        DatamasterSourcePO datamasterSourcePO = fusionWorkspaceService.saveFusionResult(step4Fwsid, "test100", "test11");
        assertThat(datamasterSourcePO.getDsid()).isEqualTo(3);
        assertThat(datamasterSourcePO.getRsid()).isEqualTo(6);
        RSSchema rsSchema = recordSourceDao.getRSSchema(6);
        assertThat(rsSchema).isNotNull();
        assertThat(fromSourceDao.getAllFromSources().size()).isEqualTo(7);
        assertThat(fromSourceDao.getFromSources(datamasterSourcePO.getDsid()).size()).isEqualTo(2);
    }

    @Test
    public void getFusionTaskStatusInfoTest() {
        List<FieldEntropy> fieldEntropies = getMockFieldEntropies();
        when(fusionDao.getFieldEntropies(any(RecordSource.class))).thenReturn(fieldEntropies);
        PreviewStats previewStats = getMockPreviewStats();
        when(fusionDao.getPreviewStats(any(RecordSource.class))).thenReturn(previewStats);
        when(fusionDao.getSingleFieldFusionNum(any(RecordSource.class))).thenReturn(1000l);
        when(fusionDao.getMultiFieldFusionNum(any(RecordSource.class))).thenReturn(1000l);

        FusionTaskStatusInfoVO fusionTaskStatusInfo = fusionWorkspaceService.getFusionTaskStatusInfo(step4Fwsid);
        assertThat(fusionTaskStatusInfo.getAddressCodeType()).isEqualTo(AddressCodeType.MULTI_ADDRESS_CODE);
        assertThat(fusionTaskStatusInfo.getSimilarity()).isEqualTo(0.5);
        assertThat(fusionTaskStatusInfo.getFusionTaskStatus()).isEqualTo(FusionTaskStatus.CALCULATED);
        assertThat(fusionTaskStatusInfo.getIntervalNumbers().size()).isGreaterThan(0);
    }

    @Test
    public void step1NextStepTest1() {
        //nextStep
        expectedException.expect(CleanerException.class);
        fusionWorkspaceService.nextStep(step1Fwsid);
    }

    @Test
    public void step1NextStepTest2() {
        //selectObjectType
        fusionWorkspaceService.selectObjectType(step1Fwsid, 3);
        boolean result = fusionWorkspaceService.nextStep(step1Fwsid);
        assertThat(result).isTrue();

        //getWorkspace
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(step1Fwsid);
        assertThat(fusionWorkspaceBO.getFusionStep()).isEqualTo(FusionStep.DATASOURCE_SELECTION);
    }

    @Test
    public void step2PreviousStepTest() {
        //addDataSources
        DataSourcesResqVO dataSourcesResqVO = prepareDataSourcesResqVO();
        boolean result = fusionWorkspaceService.addDataSources(step2Fwsid, dataSourcesResqVO);
        assertThat(result).isTrue();

        //previousStep
        fusionWorkspaceService.previousStep(step2Fwsid);

        //getSelectedDataSources
        List<FusionDataSourceVO> selectedDataSources = fusionWorkspaceService.getSelectedDataSources(step2Fwsid);
        assertThat(selectedDataSources.size()).isEqualTo(0);

        //getWorkspace
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(step2Fwsid);
        assertThat(fusionWorkspaceBO.getFusionStep()).isEqualTo(FusionStep.ONTOLOGY_SELECTION);
    }

    @Test
    public void step2NextStepTest1() {
        //nextStep
        expectedException.expect(CleanerException.class);
        fusionWorkspaceService.nextStep(step2Fwsid);
    }

    @Test
    public void step2NextStepTest2() {
        //addDataSources
        DataSourcesResqVO dataSourcesResqVO = prepareNotMatchDataSourcesResqVO();
        boolean result = fusionWorkspaceService.addDataSources(step2Fwsid, dataSourcesResqVO);
        assertThat(result).isTrue();

        //nextStep
        expectedException.expect(CleanerException.class);
        fusionWorkspaceService.nextStep(step2Fwsid);
    }

    @Test
    public void step2NextStepTest3() {
        //addDataSources
        DataSourcesResqVO dataSourcesResqVO = prepareDataSourcesResqVO();
        boolean result = fusionWorkspaceService.addDataSources(step2Fwsid, dataSourcesResqVO);
        assertThat(result).isTrue();

        //nextStep
        result = fusionWorkspaceService.nextStep(step2Fwsid);
        assertThat(result).isTrue();

        //getWorkspace
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(step2Fwsid);
        assertThat(fusionWorkspaceBO.getFusionStep()).isEqualTo(FusionStep.FUSION_MODE);
    }

    @Test
    public void step3PreviousStepTest() {
        //updateEntropyFields
        List<Integer> ptids = new ArrayList<>();
        ptids.add(11);
        ptids.add(12);
        ptids.add(13);
        Double threshold = 1.0;
        Integer uniquedPtid = 11;
        boolean result = fusionWorkspaceService.updateEntropyFields(step3Fwsid, AddressCodeType.MULTI_ADDRESS_CODE.name(), ptids, threshold, uniquedPtid);
        assertThat(result).isTrue();

        //previousStep
        when(fusionDao.deleteFusionStore(any(RecordSource.class))).thenReturn(true);
        result = fusionWorkspaceService.previousStep(step3Fwsid);
        assertThat(result).isTrue();

        //getFusionInfo
        FusionInfoVO fusionInfo = fusionWorkspaceService.getFusionInfo(step3Fwsid);
        assertThat(fusionInfo.getThreshold()).isNull();
        assertThat(fusionInfo.getAddressCodeType()).isEqualTo(AddressCodeType.SINGLE_ADDRESS_CODE);

        //getEntropyTable
        List<FieldEntropy> fieldEntropies = getMockFieldEntropies();
        when(fusionDao.getFieldEntropies(any(RecordSource.class))).thenReturn(fieldEntropies);
        EntropyTableVO entropyTable = fusionWorkspaceService.getEntropyTable(step3Fwsid);
        List<EntropyFieldVO> propertyTypes = entropyTable.getPropertyTypes();
        for (EntropyFieldVO propertyType : propertyTypes) {
            assertThat(propertyType.getSelected()).isFalse();
            assertThat(propertyType.getUnique()).isFalse();
        }
    }

    @Test
    public void step3NextStepTest1() {
        //nextStep
        expectedException.expect(CleanerException.class);
        fusionWorkspaceService.nextStep(step3Fwsid);
    }

    @Test
    public void step3NextStepTest2() {
        //updateEntropyFields
        List<Integer> ptids = new ArrayList<>();
        ptids.add(11);
        ptids.add(12);
        ptids.add(13);
        Double threshold = 3.0;
        Integer uniquedPtid = 11;
        boolean result = fusionWorkspaceService.updateEntropyFields(step3Fwsid, AddressCodeType.SINGLE_ADDRESS_CODE.name(), ptids, threshold, uniquedPtid);
        assertThat(result).isTrue();

        //nextStep
        List<FieldEntropy> fieldEntropies = getMockFieldEntropies();
        when(fusionDao.getFieldEntropies(any(RecordSource.class))).thenReturn(fieldEntropies);
        when(taskScheduler.schedule(any(Task.class))).thenReturn(true);
        expectedException.expect(CleanerException.class);
        fusionWorkspaceService.nextStep(step3Fwsid);
    }

    @Test
    public void step3NextStepTest3() {
        //updateEntropyFields
        List<Integer> ptids = new ArrayList<>();
        ptids.add(11);
        ptids.add(12);
        ptids.add(13);
        Double threshold = 1.0;
        Integer uniquedPtid = 11;
        boolean result = fusionWorkspaceService.updateEntropyFields(step3Fwsid, AddressCodeType.MULTI_ADDRESS_CODE.name(), ptids, threshold, uniquedPtid);
        assertThat(result).isTrue();

        //nextStep
        List<FieldEntropy> fieldEntropies = getMockFieldEntropies();
        when(fusionDao.getFieldEntropies(any(RecordSource.class))).thenReturn(fieldEntropies);
        when(taskScheduler.schedule(any(Task.class))).thenReturn(true);
        result = fusionWorkspaceService.nextStep(step3Fwsid);
        assertThat(result).isTrue();

        //getWorkspace
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(step3Fwsid);
        assertThat(fusionWorkspaceBO.getFusionStep()).isEqualTo(FusionStep.FUSION);
    }

    @Test
    public void step4PreviousStepTest() {
        //previousStep
        boolean result = fusionWorkspaceService.previousStep(step4Fwsid);
        assertThat(result).isTrue();

        //getWorkspace
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(step4Fwsid);
        assertThat(fusionWorkspaceBO.getFusionStep()).isEqualTo(FusionStep.FUSION_MODE);
    }

    @Test
    public void step4NextStepTest() {
        //nextStep
        boolean result = fusionWorkspaceService.nextStep(step4Fwsid);
        assertThat(result).isTrue();

        //getWorkspace
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(step4Fwsid);
        assertThat(fusionWorkspaceBO.getFinished()).isEqualTo(FinishedStatus.FINISHED);
    }

    private List<FieldEntropy> getMockFieldEntropies() {
        List<FieldEntropy> fieldEntropies = new ArrayList<>();
        FieldEntropy fieldEntropy1 = new FieldEntropy();
        fieldEntropy1.setFieldName("f1");
        fieldEntropy1.setEntropy(1.0);
        fieldEntropies.add(fieldEntropy1);
        FieldEntropy fieldEntropy2 = new FieldEntropy();
        fieldEntropy2.setFieldName("f2");
        fieldEntropy2.setEntropy(2.0);
        fieldEntropies.add(fieldEntropy2);
        return fieldEntropies;
    }

    public PreviewStats getMockPreviewStats() {
        PreviewStats mockPreviewStats = new PreviewStats();
        List<IntervalNumber> intervalNumbers = new ArrayList<>();
        IntervalNumber intervalNumber1 = new IntervalNumber();
        intervalNumber1.setStart(0.5);
        intervalNumber1.setEnd(0.75);
        intervalNumber1.setNumber(80);
        intervalNumbers.add(intervalNumber1);
        IntervalNumber intervalNumber2 = new IntervalNumber();
        intervalNumber2.setStart(0.75);
        intervalNumber2.setEnd(1.0);
        intervalNumber2.setNumber(50);
        intervalNumbers.add(intervalNumber2);
        mockPreviewStats.setIntervalNumbers(intervalNumbers);

        return mockPreviewStats;
    }

    private DataSourcesResqVO prepareDataSourcesResqVO() {
        DataSourcesResqVO dataSourcesResqVO = new DataSourcesResqVO();

        List<ExternalDataSourceResqVO> externalDataSources = new LinkedList<>();
        ExternalDataSourceResqVO externalDataSource = new ExternalDataSourceResqVO();
        externalDataSource.setDhid(1);
        externalDataSource.setDbname("test1");
        externalDataSource.setDtname("test1");
        externalDataSources.add(externalDataSource);
        dataSourcesResqVO.setExternalDataSources(externalDataSources);

        List<Integer> datamasterSources = new LinkedList<>();
        datamasterSources.add(1);
        dataSourcesResqVO.setDatamasterSources(datamasterSources);

        return dataSourcesResqVO;
    }

    private DataSourcesResqVO prepareNotMatchDataSourcesResqVO() {
        DataSourcesResqVO dataSourcesResqVO = new DataSourcesResqVO();

        List<ExternalDataSourceResqVO> externalDataSources = new LinkedList<>();
        ExternalDataSourceResqVO externalDataSource = new ExternalDataSourceResqVO();
        externalDataSource.setDhid(1);
        externalDataSource.setDbname("test1");
        externalDataSource.setDtname("test2");
        externalDataSources.add(externalDataSource);
        dataSourcesResqVO.setExternalDataSources(externalDataSources);

        List<Integer> datamasterSources = new LinkedList<>();
        datamasterSources.add(1);
        dataSourcesResqVO.setDatamasterSources(datamasterSources);

        return dataSourcesResqVO;
    }
}
