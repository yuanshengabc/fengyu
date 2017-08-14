package cn.deepclue.datamaster.cleaner.service.fusion.impl.step.next;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by magneto on 17-5-22.
 */
@Component
@Scope("prototype")
public class OntologyNext extends FusionNextStep {

    public OntologyNext(FusionWorkspaceBO fusionWorkspaceBO) {
        super(fusionWorkspaceBO);
        setStep(FusionStep.ONTOLOGY_SELECTION.getStep());
    }

    @Override
    protected boolean valid() {
        if (fusionWorkspaceBO == null
                || fusionWorkspaceBO.getObjectTypeBO() == null
                || fusionWorkspaceBO.getObjectTypeBO().getOtid() == null) {
            throw new CleanerException("unset ontology!", "未设置业务模型！", BizErrorEnum.FUSION_WORKSPACE_STEP_EXCEPTION);
        }

        return true;
    }
}
