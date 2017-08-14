package cn.deepclue.datamaster.cleaner.dao.fusion.impl;

import cn.deepclue.datamaster.cleaner.dao.fusion.OntologySourceDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.mapper.OntologySourceMapper;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.OntologySourcePO;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by magneto on 17-5-23.
 */
@Repository("ontologySourceDao")
public class OntologySourceImpl implements OntologySourceDao {
    @Autowired
    private OntologySourceMapper ontologySourceMapper;

    @Override
    public boolean delete(int wsdsid) {
        if (ontologySourceMapper.delete(wsdsid)) {
            return true;
        }
        throw new JdbcException(JdbcErrorEnum.DELETE);
    }

    @Override
    public boolean delete(List<Integer> wsdsids) {
        return false;
    }

    @Override
    public boolean insert(OntologySourcePO ontologySourcePO) {
        return false;
    }

    @Override
    public boolean insertList(List<OntologySourcePO> ontologySourcePOs) {
        int affectedCount = ontologySourceMapper.insertList(ontologySourcePOs);
        if (affectedCount == ontologySourcePOs.size()) {
            return true;
        }
        throw new JdbcException(JdbcErrorEnum.INSERT);
    }
}
