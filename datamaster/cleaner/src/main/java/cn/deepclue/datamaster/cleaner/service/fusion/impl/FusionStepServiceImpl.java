package cn.deepclue.datamaster.cleaner.service.fusion.impl;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionStepService;
import cn.deepclue.datamaster.cleaner.service.fusion.impl.step.next.*;
import cn.deepclue.datamaster.cleaner.service.fusion.impl.step.previous.FinishFusionPrevious;
import cn.deepclue.datamaster.cleaner.service.fusion.impl.step.previous.FusionPreviousStep;
import cn.deepclue.datamaster.cleaner.service.fusion.impl.step.previous.SelectDataSourcePrevious;
import cn.deepclue.datamaster.cleaner.service.fusion.impl.step.previous.SetFusionPrevious;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Created by magneto on 17-5-22.
 */
@Service("fusionStepService")
public class FusionStepServiceImpl implements FusionStepService {
    @Autowired
    private ApplicationContext context;

    @Override
    public FusionNextStep currentNextStep(FusionWorkspaceBO fusionWorkspaceBO) {
        FusionStep fusionStep = fusionWorkspaceBO.getFusionStep();
        switch (fusionStep) {
            case ONTOLOGY_SELECTION:
                return context.getBean(OntologyNext.class, fusionWorkspaceBO);
            case DATASOURCE_SELECTION:
                return context.getBean(SelectDataSourceNext.class, fusionWorkspaceBO);
            case FUSION_MODE:
                return context.getBean(SetFusionNext.class, fusionWorkspaceBO);
            case FUSION:
                return context.getBean(FinishFusionNext.class, fusionWorkspaceBO);
        }

        throw new CleanerException(BizErrorEnum.FUSION_WORKSPACE_STEP_EXCEPTION);
    }

    @Override
    public FusionPreviousStep currentPreviousStep(FusionWorkspaceBO fusionWorkspaceBO) {
        FusionStep fusionStep = fusionWorkspaceBO.getFusionStep();
        switch (fusionStep) {
            case DATASOURCE_SELECTION:
                return context.getBean(SelectDataSourcePrevious.class, fusionWorkspaceBO);
            case FUSION_MODE:
                return context.getBean(SetFusionPrevious.class, fusionWorkspaceBO);
            case FUSION:
                return context.getBean(FinishFusionPrevious.class, fusionWorkspaceBO);
        }

        throw new CleanerException(BizErrorEnum.FUSION_WORKSPACE_STEP_EXCEPTION);
    }
}
