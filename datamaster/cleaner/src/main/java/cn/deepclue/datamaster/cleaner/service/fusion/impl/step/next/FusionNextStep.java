package cn.deepclue.datamaster.cleaner.service.fusion.impl.step.next;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by magneto on 17-5-22.
 */
@Component
@Scope("prototype")
public abstract class FusionNextStep extends NextStep {
    protected Logger logger = LoggerFactory.getLogger(FusionNextStep.class);
    protected FusionWorkspaceBO fusionWorkspaceBO;

    public FusionNextStep(FusionWorkspaceBO fusionWorkspaceBO) {
        this.fusionWorkspaceBO = fusionWorkspaceBO;
    }

    public final FusionStep next() {
        if (valid()) {
            changeStep();
            work();
            return FusionStep.getFusionStep(step);
        }

        throw new CleanerException("Please select correct data for next step!", "请选择符合要求的数据！", BizErrorEnum.FUSION_WORKSPACE_STEP_EXCEPTION);
    }

    protected abstract boolean valid();

    /**
     * external work when next, default do nothing
     */
    protected void work(){}
}
