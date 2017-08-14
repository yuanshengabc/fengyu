package cn.deepclue.datamaster.cleaner.service.fusion.impl.step.previous;

import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.fusion.AddressCodeType;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import cn.deepclue.datamaster.model.schema.BaseType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by magneto on 17-5-22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PreviousTest {
    @MockBean
    protected FusionWorkspaceDao fusionWorkspaceDao;

    private FusionWorkspaceBO fusionWorkspaceBO;

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

    @Before
    public void setUp() {
        fusionWorkspaceBOInit();
    }

    @Test
    public void fusionPreviousStep() {
        int fwsid = 1;

        when(fusionWorkspaceDao.getWorkspace(fwsid)).thenReturn(fusionWorkspaceBO);
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
        PreviousStep previousStep = new SelectDataSourcePrevious(fusionWorkspaceBO);
        assertThat(FusionStep.getFusionStep(previousStep.getStep())).isEqualTo(FusionStep.DATASOURCE_SELECTION);
        assertThat(FusionStep.getFusionStep(previousStep.changeStep())).isEqualTo(FusionStep.ONTOLOGY_SELECTION);

        previousStep = new SetFusionPrevious(fusionWorkspaceBO);
        assertThat(FusionStep.getFusionStep(previousStep.getStep())).isEqualTo(FusionStep.FUSION_MODE);
        assertThat(FusionStep.getFusionStep(previousStep.changeStep())).isEqualTo(FusionStep.DATASOURCE_SELECTION);

        previousStep = new FinishFusionPrevious(fusionWorkspaceBO);
        assertThat(FusionStep.getFusionStep(previousStep.getStep())).isEqualTo(FusionStep.FUSION);
        assertThat(FusionStep.getFusionStep(previousStep.changeStep())).isEqualTo(FusionStep.FUSION_MODE);
    }
}
