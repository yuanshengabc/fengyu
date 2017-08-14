package cn.deepclue.datamaster.cleaner.dao.fusion;

import cn.deepclue.datamaster.cleaner.domain.po.fusion.OntologySourcePO;

import java.util.List;

/**
 * Created by magneto on 17-5-23.
 */
public interface OntologySourceDao {
    boolean delete(int wsdsid);

    boolean delete(List<Integer> wsdsids);

    boolean insert(OntologySourcePO ontologySourcePO);

    boolean insertList(List<OntologySourcePO> ontologySourcePOs);
}
