package cn.deepclue.datamaster.cleaner.dao.fusion;

import cn.deepclue.datamaster.cleaner.domain.po.fusion.EntropyPropertyPO;

import java.util.List;

/**
 * Created by ggchangan on 17-5-23.
 */
public interface EntropyPropertyDao {
    boolean delete(int fwsid, List<Integer> ptids);
    Integer delete(int fwsid);
    List<EntropyPropertyPO> insert(List<EntropyPropertyPO> entropyPropertyPOs);
    List<EntropyPropertyPO> getAll(int fwsid);
}
