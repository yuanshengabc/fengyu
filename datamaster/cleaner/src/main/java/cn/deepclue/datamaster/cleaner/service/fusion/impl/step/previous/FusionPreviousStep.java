package cn.deepclue.datamaster.cleaner.service.fusion.impl.step.previous;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;

/**
 * Created by magneto on 17-5-22.
 */
public abstract class FusionPreviousStep extends PreviousStep {

    protected FusionWorkspaceBO fusionWorkspaceBO;

    public FusionPreviousStep(FusionWorkspaceBO fusionWorkspaceBO) {
        this.fusionWorkspaceBO = fusionWorkspaceBO;
    }

    public final FusionStep previous() {
        if (clear()) {
            changeStep();
        }

        return FusionStep.getFusionStep(step);
    }

    protected abstract boolean clear();

}
