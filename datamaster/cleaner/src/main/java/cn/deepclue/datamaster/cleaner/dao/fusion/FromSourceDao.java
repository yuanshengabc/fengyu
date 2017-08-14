package cn.deepclue.datamaster.cleaner.dao.fusion;

import cn.deepclue.datamaster.cleaner.domain.po.fusion.FromSourcePO;

import java.util.List;

/**
 * Created by magneto on 17-6-6.
 */
public interface FromSourceDao {
    List<FromSourcePO> getFromSources(Integer dmsid);

    List<FromSourcePO> getAllFromSources();

    List<FromSourcePO> insertFromSources(List<FromSourcePO> fromSourcePOs);
}
