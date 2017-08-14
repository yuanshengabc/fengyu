package cn.deepclue.datamaster.cleaner.domain.vo.fusion;

import java.util.ArrayList;
import java.util.List;

public class DataSourcesResqVO {
    //外部数据源
    List<ExternalDataSourceResqVO> externalDataSources;
    //星河数据源
    List<Integer> datamasterSources;

    public List<ExternalDataSourceResqVO> getExternalDataSources() {
        return externalDataSources == null ? new ArrayList<>() : externalDataSources;
    }

    public void setExternalDataSources(List<ExternalDataSourceResqVO> externalDataSources) {
        this.externalDataSources = externalDataSources;
    }

    public List<Integer> getDatamasterSources() {
        return datamasterSources == null ? new ArrayList<>() : datamasterSources;
    }

    public void setDatamasterSources(List<Integer> datamasterSources) {
        this.datamasterSources = datamasterSources;
    }
}
