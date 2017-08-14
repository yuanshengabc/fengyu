package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.data.DataSourceBO;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataTable;
import cn.deepclue.datamaster.cleaner.domain.bo.workspace.fusion.FusionDataSourceBO;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 融合外部数据源VO
 * Created by sunxingwen on 17-5-18.
 */
public class FusionExternalDataSourceVO extends DataTable {
    //是否被选择
    private Boolean selected;

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public FusionExternalDataSourceVO from(DataTable dataTable, List<FusionDataSourceBO> selectedDataSources) {
        BeanUtils.copyProperties(dataTable, this);
        this.setSelected(selectedDataSources);
        return this;
    }

    private void setSelected(List<FusionDataSourceBO> selectedDataSources) {
        Boolean tempSelected = false;
        for (FusionDataSourceBO selectedDataSource : selectedDataSources) {
            DataSourceBO dataSourceBO = selectedDataSource.getDataSourceBO();
            if (dataSourceBO.getDataHouse().getDhid().equals(this.getDhid()) &&
                    dataSourceBO.getDbname().equals(this.getDbname()) &&
                    dataSourceBO.getDtname().equals(this.getDtname())) {
                tempSelected = true;
                break;
            }
        }
        this.setSelected(tempSelected);
    }
}
