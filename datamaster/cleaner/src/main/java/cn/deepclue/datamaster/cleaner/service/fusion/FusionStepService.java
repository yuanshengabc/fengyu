package cn.deepclue.datamaster.cleaner.service.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.service.fusion.impl.step.next.FusionNextStep;
import cn.deepclue.datamaster.cleaner.service.fusion.impl.step.previous.FusionPreviousStep;

/**
 * Created by magneto on 17-5-22.
 */
public interface FusionStepService {
    FusionNextStep currentNextStep(FusionWorkspaceBO fusionWorkspaceBO);
    FusionPreviousStep currentPreviousStep(FusionWorkspaceBO fusionWorkspaceBO);
}
