package cn.deepclue.datamaster.cleaner.service.fusion.impl;

import cn.deepclue.datamaster.cleaner.dao.fusion.EntropyPropertyDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyFieldBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyTableBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.service.fusion.EntropyService;
import cn.deepclue.datamaster.fusion.domain.FieldEntropy;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.offset;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by magneto on 17-5-24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EntropyServiceImplTest {
    @Autowired
    EntropyService entropyService;

    @MockBean
    FusionWorkspaceDao fusionWorkspaceDao;

    @MockBean
    FusionDao fusionDao;

    @MockBean
    EntropyPropertyDao entropyPropertyDao;

    @Test
    public void getEntropyTable() throws Exception {
        int fwsid = 1;
        FusionWorkspaceBO fusionWorkspaceBO = new FusionWorkspaceBO();
        ObjectTypeBO objectTypeBO = new ObjectTypeBO();
        List<PropertyType> properties = new ArrayList<>();
        PropertyType pt1 = new PropertyType();
        pt1.setPtid(1);
        pt1.setName("pt1");
        PropertyType pt2 = new PropertyType();
        pt2.setPtid(2);
        pt2.setName("pt2");
        properties.add(pt1);
        properties.add(pt2);
        objectTypeBO.setPropertyTypes(properties);
        fusionWorkspaceBO.setObjectTypeBO(objectTypeBO);
        when(fusionWorkspaceDao.getWorkspace(fwsid)).thenReturn(fusionWorkspaceBO);

        List<FieldEntropy> fieldEntropies = new ArrayList<>();
        FieldEntropy fieldEntropy1 = new FieldEntropy();
        fieldEntropy1.setFieldName("pt1");
        fieldEntropy1.setEntropy(1.0);
        fieldEntropies.add(fieldEntropy1);
        FieldEntropy fieldEntropy2 = new FieldEntropy();
        fieldEntropy2.setFieldName("pt2");
        fieldEntropy2.setEntropy(2.0);
        fieldEntropies.add(fieldEntropy2);
        when(fusionDao.getFieldEntropies(any(RecordSource.class))).thenReturn(fieldEntropies);

        when(entropyPropertyDao.getAll(fwsid)).thenReturn(new ArrayList<>());

        EntropyTableBO entropyTableBO = entropyService.getEntropyTable(fwsid);
        assertThat(entropyTableBO).isNotNull();
        List<EntropyFieldBO> entropyFieldBOs = entropyTableBO.getEntropyFields();
        assertThat(entropyFieldBOs.size()).isEqualTo(2);
        EntropyFieldBO entropyFieldBO1 = entropyFieldBOs.get(0);
        assertThat(entropyFieldBO1.getWsid()).isEqualTo(fwsid);
        assertThat(entropyFieldBO1.getPropertyType().getPtid()).isEqualTo(1);
        assertThat(entropyFieldBO1.getEntropy()).isEqualTo(1.0, offset(0.01));
        assertThat(entropyFieldBO1.getSelected()).isFalse();
        assertThat(entropyFieldBO1.getUnique()).isFalse();
        EntropyFieldBO entropyFieldBO2 = entropyFieldBOs.get(1);
        assertThat(entropyFieldBO2.getWsid()).isEqualTo(fwsid);
        assertThat(entropyFieldBO2.getSelected()).isFalse();
        assertThat(entropyFieldBO2.getUnique()).isFalse();
        assertThat(entropyFieldBO2.getEntropy()).isEqualTo(2.0, offset(0.01));
    }

}
