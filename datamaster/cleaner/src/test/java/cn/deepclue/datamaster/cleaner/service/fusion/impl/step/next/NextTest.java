package cn.deepclue.datamaster.cleaner.service.fusion.impl.step.next;

import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.ontology.ObjectTypeBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.model.ontology.ObjectType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by magneto on 17-5-22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NextTest {
    @MockBean
    protected FusionWorkspaceDao fusionWorkspaceDao;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void fusionNextStep() {
        int fwsid = 1;
        FusionWorkspaceBO fusionWorkspaceBO = fusionWorkspaceDao.getWorkspace(fwsid);
        FusionNextStep nextStep = new OntologyNext(fusionWorkspaceBO);
        assertThat(FusionStep.getFusionStep(nextStep.getStep())).isEqualTo(FusionStep.ONTOLOGY_SELECTION);
        assertThat(FusionStep.getFusionStep(nextStep.changeStep())).isEqualTo(FusionStep.DATASOURCE_SELECTION);

        nextStep = new SelectDataSourceNext(fusionWorkspaceBO);
        assertThat(FusionStep.getFusionStep(nextStep.getStep())).isEqualTo(FusionStep.DATASOURCE_SELECTION);
        assertThat(FusionStep.getFusionStep(nextStep.changeStep())).isEqualTo(FusionStep.FUSION_MODE);

        nextStep = new SetFusionNext(fusionWorkspaceBO);
        assertThat(FusionStep.getFusionStep(nextStep.getStep())).isEqualTo(FusionStep.FUSION_MODE);
        assertThat(FusionStep.getFusionStep(nextStep.changeStep())).isEqualTo(FusionStep.FUSION);

        nextStep = new FinishFusionNext(fusionWorkspaceBO);
        assertThat(FusionStep.getFusionStep(nextStep.getStep())).isEqualTo(FusionStep.FUSION);
        assertThat(FusionStep.getFusionStep(nextStep.changeStep())).isEqualTo(FusionStep.FUSION);

    }

    @Test
    public void ontologyValid() {
        int fwsid = 1;
        FusionWorkspaceBO fusionWorkspaceBO = new FusionWorkspaceBO();
        fusionWorkspaceBO.setWsid(fwsid);
        ObjectTypeBO ot = new ObjectTypeBO();
        Integer otid = 1;
        ot.setOtid(otid);
        fusionWorkspaceBO.setObjectTypeBO(ot);
        when(fusionWorkspaceDao.getWorkspace(fwsid)).thenReturn(fusionWorkspaceBO);
        OntologyNext ontologyNext = new OntologyNext(fusionWorkspaceBO);
        assertThat(ontologyNext.valid()).isTrue();

        otid = null;
        ot.setOtid(otid);
        expectedException.expect(CleanerException.class);
        ontologyNext.valid();
    }
}
