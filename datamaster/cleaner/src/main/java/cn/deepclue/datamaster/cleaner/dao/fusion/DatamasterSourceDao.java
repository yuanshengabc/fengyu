package cn.deepclue.datamaster.cleaner.dao.fusion;

import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;

import java.util.List;

/**
 * Created by sunxingwen on 17-5-18.
 */
public interface DatamasterSourceDao {
    Integer getDatamasterSourceCount();

    List<DatamasterSourcePO> getDatamasterSources(int page, int pageSize);

    DatamasterSourcePO insertDatamasterSource(DatamasterSourcePO datamasterSourcePO);

    boolean deleteDatamasterSources(int dsid);

    DatamasterSourcePO getDatamasterSourceByDsid(int dsid);

    List<DatamasterSourcePO> getDatamasterSourcesByDsids(List<Integer> dsids);

    List<DatamasterSourcePO> getDatamasterSourcesByRsid(int rsid);

    List<DatamasterSourcePO> getDatamasterSources();
}
