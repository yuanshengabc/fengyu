package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDatamasterSourceBO;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 融合星河数据源VO
 * Created by sunxingwen on 17-5-18.
 */
public class FusionDatamasterSourceVO extends DatamasterSourcePO {
    //是否被选择
    private Boolean selected;

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public FusionDatamasterSourceVO from(DatamasterSourcePO datamasterSource, List<FusionDatamasterSourceBO> selectedDatamasterSources) {
        BeanUtils.copyProperties(datamasterSource, this);
        this.setSelected(selectedDatamasterSources);
        return this;
    }

    private void setSelected(List<FusionDatamasterSourceBO> selectedDatamasterSources) {
        Boolean tempSelected = false;
        for (FusionDatamasterSourceBO selectedDatamasterSource : selectedDatamasterSources) {
            if (selectedDatamasterSource.getDatamasterSourcePO().getDsid().equals(this.getDsid())) {
                tempSelected = true;
                break;
            }
        }
        this.setSelected(tempSelected);
    }
}
