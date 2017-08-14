package cn.deepclue.datamaster.cleaner.service.fusion.impl.step.previous;

import cn.deepclue.datamaster.cleaner.dao.fusion.FusionWorkspaceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.EntropyTableBO;
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
public class SetFusionPrevious extends FusionPreviousStep {
    @Autowired
    EntropyService entropyService;
    @Autowired
    FusionWorkspaceDao fusionWorkspaceDao;

    public SetFusionPrevious(FusionWorkspaceBO fusionWorkspaceBO) {
        super(fusionWorkspaceBO);
        setStep(FusionStep.FUSION_MODE.getStep());
    }

    @Override
    @Transactional
    protected boolean clear() {
        int fwsid = fusionWorkspaceBO.getWsid();

        fusionWorkspaceDao.updateAddressCodeType(fwsid, null);
        fusionWorkspaceDao.updateThreshold(fwsid, null);
        fusionWorkspaceDao.updateEntropyCalculationTask(fwsid, null);
        entropyService.deleteFusionStore(fusionWorkspaceBO.getRecordSource());

        EntropyTableBO entropyTableBO = entropyService.getSelectedEntropyTable(fwsid);
        if (entropyTableBO == null || entropyTableBO.getEntropyFields() == null || entropyTableBO.getEntropyFields().isEmpty()) {
            return true;
        }

        return entropyService.deleteEntropyTable(fwsid) == entropyTableBO.getEntropyFields().size();
    }
}
