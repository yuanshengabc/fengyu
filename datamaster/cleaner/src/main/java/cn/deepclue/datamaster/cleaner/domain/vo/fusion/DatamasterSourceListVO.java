package cn.deepclue.datamaster.cleaner.domain.vo.fusion;


import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;

import java.util.List;

/**
 * 融合星河数据源ListVO
 * Created by magneto on 17-5-17.
 */
public class DatamasterSourceListVO {
    //数量
    private Integer dsCount;
    //数据源
    private List<DatamasterSourcePO> datamasterSources;

    public Integer getDsCount() {
        return dsCount;
    }

    public void setDsCount(Integer dsCount) {
        this.dsCount = dsCount;
    }

    public List<DatamasterSourcePO> getDatamasterSources() {
        return datamasterSources;
    }

    public void setDatamasterSources(List<DatamasterSourcePO> datamasterSources) {
        this.datamasterSources = datamasterSources;
    }
}
