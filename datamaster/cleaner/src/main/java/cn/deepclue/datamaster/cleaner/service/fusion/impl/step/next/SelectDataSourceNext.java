package cn.deepclue.datamaster.cleaner.service.fusion.impl.step.next;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDatamasterSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionWorkspaceBO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.fusion.FusionStep;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.cleaner.service.fusion.FusionSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by magneto on 17-5-22.
 */
@Component
@Scope("prototype")
public class SelectDataSourceNext extends FusionNextStep {
    @Autowired
    private FusionSourceService fusionSourceService;

    public SelectDataSourceNext(FusionWorkspaceBO fusionWorkspaceBO) {
        super(fusionWorkspaceBO);
        setStep(FusionStep.DATASOURCE_SELECTION.getStep());

    }

    @Override
    protected boolean valid() {

        List<FusionDatamasterSourceBO> fusionDatamasterSourceBOs = fusionSourceService.getFusionDatamasterSourceBOs(fusionWorkspaceBO.getWsid());
        List<FusionDataSourceBO> fusionDataSourceBOs = fusionSourceService.getFusionDataSourceBOs(fusionWorkspaceBO.getWsid());

        if (fusionDataSourceBOs.isEmpty() && fusionDatamasterSourceBOs.isEmpty()) {
            throw new CleanerException(BizErrorEnum.FUSION_WORKSPACE_SELECT_DATASOURCE_EXCEPTION);
        }

        for (FusionDataSourceBO fusionDataSourceBO : fusionDataSourceBOs) {
            if (!fusionDataSourceBO.getMatch()) {
                return false;
            }
        }

        for (FusionDatamasterSourceBO fusionDatamasterSourceBO : fusionDatamasterSourceBOs) {
            if (!fusionDatamasterSourceBO.getMatch()) {
                return false;
            }
        }

        return true;
    }
}
