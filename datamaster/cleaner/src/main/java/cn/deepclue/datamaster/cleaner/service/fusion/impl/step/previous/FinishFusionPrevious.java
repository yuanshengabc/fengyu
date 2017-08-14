package cn.deepclue.datamaster.cleaner.service.fusion.impl.step.previous;

import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.service.fusion.EntropyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by magneto on 17-5-22.
 */
@Component
@Scope("prototype")
public class FinishFusionPrevious extends FusionPreviousStep {
    @Autowired
    FusionWorkspaceDao fusionWorkspaceDao;
    @Autowired
    EntropyService entropyService;

    public FinishFusionPrevious(FusionWorkspaceBO fusionWorkspaceBO) {
        super(fusionWorkspaceBO);
        setStep(FusionStep.FUSION.getStep());
    }

    @Override
    @Transactional
    protected boolean clear() {
        int fwsid = fusionWorkspaceBO.getWsid();
        fusionWorkspaceDao.updateSimilarityTask(fwsid, null);

        return true;
    }
}
