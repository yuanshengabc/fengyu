package cn.deepclue.datamaster.cleaner.dao.fusion.impl;

import cn.deepclue.datamaster.cleaner.dao.fusion.EntropyPropertyDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.mapper.EntropyPropertyMapper;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.EntropyPropertyPO;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ggchangan on 17-5-23.
 */
@Repository("entropyPropertyDao")
public class EntropyPropertyImpl implements EntropyPropertyDao {
    @Autowired
    EntropyPropertyMapper entropyPropertyMapper;

    @Override
    public boolean delete(int fwsid, List<Integer> ptids) {
        return false;
    }

    @Override
    public Integer delete(int fwsid) {
        return entropyPropertyMapper.deleteByFwsid(fwsid);
    }

    @Override
    @Transactional
    public List<EntropyPropertyPO> insert(List<EntropyPropertyPO> entropyPropertyPOs) {
        List<EntropyPropertyPO> newEntropyPOs = new ArrayList<>();

        for (EntropyPropertyPO entropyPropertyPO : entropyPropertyPOs) {
            entropyPropertyPO.setWsptid(null);
            if (entropyPropertyMapper.insertObjectType(entropyPropertyPO) && entropyPropertyPO.getWsptid() != null) {
                newEntropyPOs.add(entropyPropertyPO);
            } else {
                throw new JdbcException(JdbcErrorEnum.INSERT);
            }
        }

        return newEntropyPOs;
    }

    @Override
    public List<EntropyPropertyPO> getAll(int fwsid) {
        return entropyPropertyMapper.getAll(fwsid);
    }
}
