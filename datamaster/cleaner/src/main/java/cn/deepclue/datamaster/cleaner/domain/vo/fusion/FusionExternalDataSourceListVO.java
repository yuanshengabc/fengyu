package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import java.util.List;

/**
 * 融合外部数据源ListVO
 * Created by magneto on 17-5-17.
 */
public class FusionExternalDataSourceListVO {
    //数量
    private Integer dsCount;
    //数据源
    private List<FusionExternalDataSourceVO> externalDataSources;

    public Integer getDsCount() {
        return dsCount;
    }

    public void setDsCount(Integer dsCount) {
        this.dsCount = dsCount;
    }

    public List<FusionExternalDataSourceVO> getExternalDataSources() {
        return externalDataSources;
    }

    public void setExternalDataSources(List<FusionExternalDataSourceVO> externalDataSources) {
        this.externalDataSources = externalDataSources;
    }
}
