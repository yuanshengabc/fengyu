package cn.deepclue.datamaster.cleaner.service.fusion.impl.step.previous;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionSourceService;
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by magneto on 17-5-22.
 */
@Component
@Scope("prototype")
public class SelectDataSourcePrevious extends FusionPreviousStep {
    @Autowired
    FusionSourceService fusionSourceService;

    public SelectDataSourcePrevious(FusionWorkspaceBO fusionWorkspaceBO) {
        super(fusionWorkspaceBO);
        setStep(FusionStep.DATASOURCE_SELECTION.getStep());
    }

    @Override
    @Transactional
    protected boolean clear() {
        int sourceCount = fusionSourceService.getSources(fusionWorkspaceBO.getWsid());

        if (sourceCount == 0) {
            return true;
        }

        return fusionSourceService.removeSources(fusionWorkspaceBO.getWsid()) == sourceCount;
    }
}
