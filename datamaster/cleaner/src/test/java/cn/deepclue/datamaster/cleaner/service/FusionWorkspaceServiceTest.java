package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.dao.cleaning.TaskDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.SourceType;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataTable;
import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.bo.task.Task;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.*;
import cn.deepclue.datamaster.cleaner.domain.fusion.AddressCodeType;
import cn.deepclue.datamaster.cleaner.domain.fusion.EntropyCalculationStatus;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import cn.deepclue.datamaster.cleaner.domain.vo.data.DataTableListVO;
import cn.deepclue.datamaster.cleaner.domain.vo.fusion.*;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.scheduler.TaskScheduler;
import cn.deepclue.datamaster.cleaner.service.fusion.EntropyService;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionSourceService;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionWorkspaceService;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import cn.deepclue.datamaster.model.schema.BaseType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.offset;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * 融合空间服务测试
 * Created by sunxingwen on 17-5-19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FusionWorkspaceServiceTest {
    @MockBean
    private FusionWorkspaceDao fusionWorkspaceDao;
    @MockBean
    private EntropyService entropyService;
    @MockBean
    private FusionSourceService fusionSourceService;

    @MockBean
    private TaskDao taskDao;

    @MockBean
    private TaskScheduler taskScheduler;

    @Autowired
    private FusionWorkspaceService fusionWorkspaceService;

    private FusionWorkspaceBO fusionWorkspaceBO;
    private List<FusionDataSourceBO> fusionDataSourceBOs;
    private List<FusionDatamasterSourceBO> fusionDatamasterSourceBOs;
    private EntropyTableBO entropyTableBO;

    @Before
    public void setUp() {
        fusionWorkspaceBOInit();
        fusionDataSourceBOsInit();
        fusionDatamasterSourceBOsInit();
        entropyTableBOInit();
    }

    @After
    public void tearDown() {

    }

    private void fusionWorkspaceBOInit() {
        int fwsid = 1;
        fusionWorkspaceBO = new FusionWorkspaceBO();

        ObjectTypeBO objectTypeBO = new ObjectTypeBO();
        objectTypeBO.setOtid(1);
        objectTypeBO.setName("ot1");
        List<PropertyType> propertyTypes = new LinkedList<>();
        PropertyType propertyType = new PropertyType();
        propertyType.setPtid(1);
        propertyType.setName("pt1");
        propertyType.setBaseType(BaseType.TEXT.getValue());
        propertyTypes.add(propertyType);
        objectTypeBO.setPropertyTypes(propertyTypes);
        fusionWorkspaceBO.setObjectTypeBO(objectTypeBO);
        fusionWorkspaceBO.setThreshold(2.0);
        fusionWorkspaceBO.setAddressCodeType(AddressCodeType.SINGLE_ADDRESS_CODE);
    }

    private void fusionDataSourceBOsInit() {
        fusionDataSourceBOs = new LinkedList<>();
        FusionDataSourceBO fusionDataSourceBO = new FusionDataSourceBO();
        DataSourceBO dataSourceBO = new DataSourceBO();
        dataSourceBO.setDsid(1);
        dataSourceBO.setDbname("db1");
        dataSourceBO.setDtname("dt1");
        DataHouse dataHouse = new DataHouse();
        dataHouse.setDhid(1);
        dataHouse.setIp("127.0.0.1");
        dataHouse.setPort(3306);
        dataSourceBO.setDataHouse(dataHouse);
        fusionDataSourceBO.setDataSourceBO(dataSourceBO);
        fusionDataSourceBO.setCreatedOn(new Date());
        fusionDataSourceBO.setMatch(false);
        fusionDataSourceBOs.add(fusionDataSourceBO);
    }

    private void fusionDatamasterSourceBOsInit() {
        fusionDatamasterSourceBOs = new LinkedList<>();
        FusionDatamasterSourceBO fusionDatamasterSourceBO = new FusionDatamasterSourceBO();
        DatamasterSourcePO datamasterSourcePO = new DatamasterSourcePO();
        datamasterSourcePO.setDsid(1);
        datamasterSourcePO.setName("datamasterSource1");
        fusionDatamasterSourceBO.setDatamasterSourcePO(datamasterSourcePO);
        fusionDatamasterSourceBO.setCreatedOn(new Date());
        fusionDatamasterSourceBO.setMatch(true);
        fusionDatamasterSourceBOs.add(fusionDatamasterSourceBO);
    }

    private void entropyTableBOInit() {
        entropyTableBO = new EntropyTableBO();
        List<EntropyFieldBO> entropyFields = new LinkedList<>();
        PropertyType propertyType = new PropertyType();
        propertyType.setPtid(1);
        propertyType.setName("pt1");
        propertyType.setBaseType(BaseType.TEXT.getValue());
        EntropyFieldBO entropyField = new EntropyFieldBO();
        entropyField.setPropertyType(propertyType);
        entropyField.setEntropy(2.0);
        entropyField.setSelected(true);
        entropyField.setUnique(false);
        entropyFields.add(entropyField);
        entropyTableBO.setEntropyFields(entropyFields);
    }

    @Test
    public void getSelectedObjectTypeTest() {
        int fwsid = 1;
        when(fusionWorkspaceDao.getWorkspace(fwsid)).thenReturn(fusionWorkspaceBO);

        FusionObjectTypeVO fusionObjectTypeVO = fusionWorkspaceService.getSelectedObjectType(fwsid);
        assertThat(fusionObjectTypeVO.getOtid()).isEqualTo(1);
        assertThat(fusionObjectTypeVO.getName()).isEqualTo("ot1");
        List<FusionPropertyTypeVO> propertyTypes = fusionObjectTypeVO.getPropertyTypes();
        assertThat(propertyTypes.size()).isEqualTo(1);
        FusionPropertyTypeVO propertyType = propertyTypes.get(0);
        assertThat(propertyType.getPtid()).isEqualTo(1);
        assertThat(propertyType.getName()).isEqualTo("pt1");
        assertThat(propertyType.getBaseType()).isEqualTo(BaseType.TEXT);
    }

    @Test
    public void getDatasourcesTest() {
        int fwsid = 1;
        when(fusionSourceService.getFusionDataSourceBOs(fwsid)).thenReturn(fusionDataSourceBOs);
        when(fusionSourceService.getFusionDatamasterSourceBOs(fwsid)).thenReturn(fusionDatamasterSourceBOs);

        List<FusionDataSourceVO> fusionDataSourceVOS = fusionWorkspaceService.getSelectedDataSources(fwsid);
        assertThat(fusionDataSourceVOS.size()).isEqualTo(2);
        FusionDataSourceVO fusionDataSourceVO1 = fusionDataSourceVOS.get(0);
        assertThat(fusionDataSourceVO1.getDsid()).isEqualTo(1);
        assertThat(fusionDataSourceVO1.getType()).isEqualTo(SourceType.DATASOURCE);
        assertThat(fusionDataSourceVO1.getName()).isEqualTo("dt1");
        assertThat(fusionDataSourceVO1.getDhid()).isEqualTo(1);
        assertThat(fusionDataSourceVO1.getDbname()).isEqualTo("db1");
        assertThat(fusionDataSourceVO1.getIp()).isEqualTo("127.0.0.1");
        assertThat(fusionDataSourceVO1.getPort()).isEqualTo(3306);
        assertThat(fusionDataSourceVO1.getStatus()).isEqualTo(false);
        FusionDataSourceVO fusionDataSourceVO2 = fusionDataSourceVOS.get(1);
        assertThat(fusionDataSourceVO2.getDsid()).isEqualTo(1);
        assertThat(fusionDataSourceVO2.getType()).isEqualTo(SourceType.DATAMASTER_SOURCE);
        assertThat(fusionDataSourceVO2.getName()).isEqualTo("datamasterSource1");
        assertThat(fusionDataSourceVO2.getStatus()).isEqualTo(true);
    }

    @Test
    public void fetchExternalDatasourcesTest() {
        int fwsid = 1;
        int dhid = 1;
        String dbName = "db1";
        int page = 1;
        int pageSize = 10;
        when(fusionSourceService.getFusionDataSourceBOs(fwsid)).thenReturn(fusionDataSourceBOs);

        DataTableListVO dataTableListVO = new DataTableListVO();
        dataTableListVO.setDsCount(2);
        List<DataTable> dataTables = new LinkedList<>();
        DataTable dataTable1 = new DataTable();
        dataTable1.setDhid(dhid);
        dataTable1.setDbname(dbName);
        dataTable1.setDtname("dt1");
        dataTables.add(dataTable1);
        DataTable dataTable2 = new DataTable();
        dataTable2.setDhid(dhid);
        dataTable2.setDbname(dbName);
        dataTable2.setDtname("dt2");
        dataTables.add(dataTable2);
        dataTableListVO.setDataTables(dataTables);
        when(fusionSourceService.fetchDataTables(dhid, dbName, page, pageSize)).thenReturn(dataTableListVO);

        FusionExternalDataSourceListVO fusionExternalDataSourceListVO =
                fusionWorkspaceService.fetchExternalDataSources(fwsid, dhid, dbName, page, pageSize);
        assertThat(fusionExternalDataSourceListVO.getDsCount()).isEqualTo(2);
        List<FusionExternalDataSourceVO> fusionExternalDataSourceVOS = fusionExternalDataSourceListVO.getExternalDataSources();
        FusionExternalDataSourceVO fusionExternalDataSourceVO1 = fusionExternalDataSourceVOS.get(0);
        assertThat(fusionExternalDataSourceVO1.getDtname()).isEqualTo("dt1");
        assertThat(fusionExternalDataSourceVO1.getSelected()).isEqualTo(true);
        FusionExternalDataSourceVO fusionExternalDataSourceVO2 = fusionExternalDataSourceVOS.get(1);
        assertThat(fusionExternalDataSourceVO2.getDtname()).isEqualTo("dt2");
        assertThat(fusionExternalDataSourceVO2.getSelected()).isEqualTo(false);
    }

    @Test
    public void getDatamasterSourcesTest() {
        int fwsid = 1;
        int page = 1;
        int pageSize = 10;
        when(fusionSourceService.getFusionDatamasterSourceBOs(fwsid)).thenReturn(fusionDatamasterSourceBOs);

        DatamasterSourceListVO datamasterSourceListVO = new DatamasterSourceListVO();
        datamasterSourceListVO.setDsCount(2);
        List<DatamasterSourcePO> datamasterSourcePOS = new LinkedList<>();
        DatamasterSourcePO datamasterSourcePO1 = new DatamasterSourcePO();
        datamasterSourcePO1.setDsid(1);
        datamasterSourcePO1.setName("datamasterSource1");
        datamasterSourcePOS.add(datamasterSourcePO1);
        DatamasterSourcePO datamasterSourcePO2 = new DatamasterSourcePO();
        datamasterSourcePO2.setDsid(2);
        datamasterSourcePO2.setName("datamasterSource2");
        datamasterSourcePOS.add(datamasterSourcePO2);
        datamasterSourceListVO.setDatamasterSources(datamasterSourcePOS);
        when(fusionSourceService.getDatamasterSources(page, pageSize)).thenReturn(datamasterSourceListVO);

        FusionDatamasterSourceListVO fusionDatamasterSourceListVO =
                fusionWorkspaceService.getDatamasterSources(fwsid, page, pageSize);
        assertThat(fusionDatamasterSourceListVO.getDsCount()).isEqualTo(2);
        List<FusionDatamasterSourceVO> fusionDatamasterSourceVOS = fusionDatamasterSourceListVO.getDatamasterSources();
        FusionDatamasterSourceVO fusionDatamasterSourceVO1 = fusionDatamasterSourceVOS.get(0);
        assertThat(fusionDatamasterSourceVO1.getDsid()).isEqualTo(1);
        assertThat(fusionDatamasterSourceVO1.getName()).isEqualTo("datamasterSource1");
        assertThat(fusionDatamasterSourceVO1.getSelected()).isEqualTo(true);
        FusionDatamasterSourceVO fusionDatamasterSourceVO2 = fusionDatamasterSourceVOS.get(1);
        assertThat(fusionDatamasterSourceVO2.getDsid()).isEqualTo(2);
        assertThat(fusionDatamasterSourceVO2.getName()).isEqualTo("datamasterSource2");
        assertThat(fusionDatamasterSourceVO2.getSelected()).isEqualTo(false);
    }

    @Test
    public void getEntropyTableTest() {
        int fwsid = 1;
        when(fusionWorkspaceDao.getWorkspace(fwsid)).thenReturn(fusionWorkspaceBO);
        when(entropyService.getEntropyTable(fwsid)).thenReturn(entropyTableBO);

        EntropyTableVO entropyTableVO = fusionWorkspaceService.getEntropyTable(fwsid);
        assertThat(entropyTableVO.getOtid()).isEqualTo(1);
        assertThat(entropyTableVO.getName()).isEqualTo("ot1");
        List<EntropyFieldVO> entropyFieldVOs = entropyTableVO.getPropertyTypes();
        assertThat(entropyFieldVOs.size()).isEqualTo(1);
        EntropyFieldVO entropyFieldVO = entropyFieldVOs.get(0);
        assertThat(entropyFieldVO.getPtid()).isEqualTo(1);
        assertThat(entropyFieldVO.getName()).isEqualTo("pt1");
        assertThat(entropyFieldVO.getEntropy()).isEqualTo(2.0);
        assertThat(entropyFieldVO.getSelected()).isTrue();
        assertThat(entropyFieldVO.getUnique()).isFalse();
        assertThat(entropyFieldVO.getBaseType()).isEqualTo(BaseType.TEXT);
    }

    @Test
    public void getFusionStatus() {
        int fwsid = 1;
        when(fusionWorkspaceDao.getWorkspace(fwsid)).thenReturn(fusionWorkspaceBO);
        FusionInfoVO fusionInfoVO = fusionWorkspaceService.getFusionInfo(fwsid);
        assertThat(fusionInfoVO.getAddressCodeType()).isEqualTo(AddressCodeType.SINGLE_ADDRESS_CODE);
        assertThat(fusionInfoVO.getEntropyCalculationStatus()).isEqualTo(EntropyCalculationStatus.UNCALCULATED);
        assertThat(fusionInfoVO.getThreshold()).isEqualTo(2.0, offset(0.01));
    }

    @Test
    public void nextTest() {
        int fwsid = 1;
        Task task = new Task();
        task.setTid(15);
        when(fusionWorkspaceDao.getWorkspace(fwsid)).thenReturn(fusionWorkspaceBO);
        when(taskDao.createSimilarityComputeTask(any(FusionWorkspaceBO.class))).thenReturn(task);
        when(taskScheduler.schedule(task)).thenReturn(true);

        fusionWorkspaceBO.setFusionStep(FusionStep.ONTOLOGY_SELECTION);

        // FIXME: How to disable transaction? We should move this unit test to integ test right now.
//        boolean next = fusionWorkspaceService.nextStep(fwsid);
//        assertThat(next).isTrue();
    }

    @Test
    public void previousText() {

    }
}
