package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import java.util.List;

/**
 * 融合星河数据源ListVO
 * Created by magneto on 17-5-17.
 */
public class FusionDatamasterSourceListVO {
    //数量
    private Integer dsCount;
    //数据源
    private List<FusionDatamasterSourceVO> datamasterSources;

    public Integer getDsCount() {
        return dsCount;
    }

    public void setDsCount(Integer dsCount) {
        this.dsCount = dsCount;
    }

    public List<FusionDatamasterSourceVO> getDatamasterSources() {
        return datamasterSources;
    }

    public void setDatamasterSources(List<FusionDatamasterSourceVO> datamasterSources) {
        this.datamasterSources = datamasterSources;
    }
}
