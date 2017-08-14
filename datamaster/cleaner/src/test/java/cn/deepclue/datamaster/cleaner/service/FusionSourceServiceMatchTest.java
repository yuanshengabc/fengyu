package cn.deepclue.datamaster.cleaner.service;

import cn.deepclue.datamaster.cleaner.dao.cleaning.DataHouseDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.DataSourceDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordSourceDao;
import cn.deepclue.datamaster.cleaner.domain.SourceType;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.po.WorkspaceSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.OntologySourcePO;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionSourceService;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FusionSourceServiceMatchTest {
    @MockBean
    private DataHouseDao dataHouseDao;
    @MockBean
    private RecordSourceDao recordSourceDao;
    @MockBean
    private DataSourceDao dataSourceDao;

    @Autowired
    private FusionSourceService fusionSourceService;
    private List<DataSource> dataSources;
    private List<DatamasterSourcePO> datamasterSourcePOs;
    private List<WorkspaceSourcePO> workspaceSourcePOs;
    private DataHouse dataHouse;
    private DataSource dataSource;

    public FusionSourceServiceMatchTest() {
        init();
    }

    private void init() {
        dataHouse = new DataHouse();
        dataHouse.setDhid(1);

        dataSource = new DataSource();
        dataSource.setDsid(1);
        dataSource.setDhid(1);

        dataSources = new LinkedList<>();
        dataSources.add(dataSource);

        datamasterSourcePOs = new LinkedList<>();
        DatamasterSourcePO datamasterSourcePO = new DatamasterSourcePO();
        datamasterSourcePO.setDsid(1);
        datamasterSourcePO.setName("datamastersource1");
        datamasterSourcePO.setRsid(1);
        datamasterSourcePOs.add(datamasterSourcePO);

        workspaceSourcePOs = new LinkedList<>();
        WorkspaceSourcePO workspaceSourcePO1 = new WorkspaceSourcePO();
        workspaceSourcePO1.setWsdsid(1);
        workspaceSourcePO1.setWsdsid(1);
        workspaceSourcePO1.setSid(1);
        workspaceSourcePO1.setStype(SourceType.DATASOURCE.getType());
        workspaceSourcePOs.add(workspaceSourcePO1);
        WorkspaceSourcePO workspaceSourcePO2 = new WorkspaceSourcePO();
        workspaceSourcePO2.setWsdsid(2);
        workspaceSourcePO2.setWsdsid(1);
        workspaceSourcePO2.setSid(1);
        workspaceSourcePO2.setStype(SourceType.DATAMASTER_SOURCE.getType());
        workspaceSourcePOs.add(workspaceSourcePO2);
    }

    @Test
    public void matchTest() {
        //业务模型
        ObjectTypeBO objectTypeBO = new ObjectTypeBO();
        objectTypeBO.setOtid(1);
        objectTypeBO.setName("ot1");
        List<PropertyType> propertyTypes = new LinkedList<>();

        PropertyType propertyType1 = new PropertyType();
        propertyType1.setPtid(1);
        propertyType1.setName("text");
        propertyType1.setBaseType(BaseType.TEXT.getValue());
        propertyTypes.add(propertyType1);

        PropertyType propertyType2 = new PropertyType();
        propertyType2.setPtid(2);
        propertyType2.setName("int");
        propertyType2.setBaseType(BaseType.INT.getValue());
        propertyTypes.add(propertyType2);

        PropertyType propertyType3 = new PropertyType();
        propertyType3.setPtid(3);
        propertyType3.setName("long");
        propertyType3.setBaseType(BaseType.LONG.getValue());
        propertyTypes.add(propertyType3);

        objectTypeBO.setPropertyTypes(propertyTypes);

        //外部数据源模型
        when(dataHouseDao.getDataHouse(1)).thenReturn(dataHouse);

        RSSchema datasourceSchema = new RSSchema();
        datasourceSchema.setName("dt1");
        List<RSField> dataSourceFields = new LinkedList<>();

        RSField datasourceField1 = new RSField();
        datasourceField1.setName("text");
        datasourceField1.setBaseType(BaseType.TEXT);
        dataSourceFields.add(datasourceField1);

        RSField datasourceField2 = new RSField();
        datasourceField2.setName("int");
        datasourceField2.setBaseType(BaseType.INT);
        dataSourceFields.add(datasourceField2);

        RSField datasourceField3 = new RSField();
        datasourceField3.setName("long1");
        datasourceField3.setBaseType(BaseType.LONG);
        dataSourceFields.add(datasourceField3);

        RSField datasourceField4 = new RSField();
        datasourceField4.setName("long2");
        datasourceField4.setBaseType(BaseType.LONG);
        dataSourceFields.add(datasourceField4);

        datasourceSchema.setFields(dataSourceFields);
        when(dataSourceDao.fetchSchema(dataHouse, dataSource)).thenReturn(datasourceSchema);

        //星河数据源模型
        RSSchema datamastersourceSchema = new RSSchema();
        datamastersourceSchema.setName("datamasterSource1");
        List<RSField> datamasterSourceFields = new LinkedList<>();

        RSField datamasterSourceField1 = new RSField();
        datamasterSourceField1.setName("text");
        datamasterSourceField1.setBaseType(BaseType.TEXT);
        datamasterSourceFields.add(datamasterSourceField1);

        RSField datamasterSourceField2 = new RSField();
        datamasterSourceField2.setName("int");
        datamasterSourceField2.setBaseType(BaseType.INT);
        datamasterSourceFields.add(datamasterSourceField2);

        RSField datamasterSourceField3 = new RSField();
        datamasterSourceField3.setName("long1");
        datamasterSourceField3.setBaseType(BaseType.LONG);
        datamasterSourceFields.add(datamasterSourceField3);

        RSField datamasterSourceField4 = new RSField();
        datamasterSourceField4.setName("long2");
        datamasterSourceField4.setBaseType(BaseType.LONG);
        datamasterSourceFields.add(datamasterSourceField4);

        datamastersourceSchema.setFields(datamasterSourceFields);
        when(recordSourceDao.getRSSchema(1)).thenReturn(datamastersourceSchema);

        //判定是否匹配
        List<OntologySourcePO> ontologySourcePOs = fusionSourceService.match(objectTypeBO, workspaceSourcePOs, dataSources, datamasterSourcePOs);
        assertThat(ontologySourcePOs.size()).isEqualTo(2);
        assertThat(ontologySourcePOs.get(0).getMatch()).isEqualTo(true);
        assertThat(ontologySourcePOs.get(0).getMultimatchPtids()).contains("3");
        assertThat(ontologySourcePOs.get(1).getMatch()).isEqualTo(true);
        assertThat(ontologySourcePOs.get(1).getMultimatchPtids()).contains("3");
    }
}
