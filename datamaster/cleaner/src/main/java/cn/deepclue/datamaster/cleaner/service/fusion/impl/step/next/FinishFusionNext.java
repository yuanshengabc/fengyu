package cn.deepclue.datamaster.cleaner.service.fusion.impl.step.next;

import cn.deepclue.datamaster.cleaner.dao.WorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.FinishedStatus;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.fusion.FusionTaskStatus;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by magneto on 17-5-22.
 */
@Component
@Scope("prototype")
public class FinishFusionNext extends FusionNextStep {
    @Autowired
    private WorkspaceDao workspaceDao;

    public FinishFusionNext(FusionWorkspaceBO fusionWorkspaceBO) {
        super(fusionWorkspaceBO);
        setStep(FusionStep.FUSION.getStep());
    }

    @Override
    protected boolean valid() {
        FusionTaskStatus fusionTaskStatus = fusionWorkspaceBO.getStatus();
        if (fusionTaskStatus == FusionTaskStatus.FAILED) {
            throw new CleanerException(BizErrorEnum.FUSIONTASK_FIELD);
        }
        return true;
    }


    /**
     * the last step do not change step
     *
     * @return
     */
    @Override
    protected int changeStep() {
        return getStep();
    }

    @Override
    protected void work() {
        super.work();
        workspaceDao.updateWorkspaceStatus(fusionWorkspaceBO.getWsid(), FinishedStatus.FINISHED);
    }
}
