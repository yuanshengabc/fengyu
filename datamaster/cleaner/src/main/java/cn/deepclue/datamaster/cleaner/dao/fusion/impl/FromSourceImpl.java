package cn.deepclue.datamaster.cleaner.dao.fusion.impl;

import cn.deepclue.datamaster.cleaner.dao.fusion.FromSourceDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.mapper.FromSourceMapper;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.FromSourcePO;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magneto on 17-6-6.
 */
@Repository("fromSourceDao")
public class FromSourceImpl implements FromSourceDao {
    @Autowired
    FromSourceMapper fromSourceMapper;

    @Override
    public List<FromSourcePO> getFromSources(Integer dmsid) {
        return fromSourceMapper.getFromSources(dmsid);
    }

    @Override
    public List<FromSourcePO> getAllFromSources() {
        return fromSourceMapper.getAllFromSources();
    }

    @Override
    public List<FromSourcePO> insertFromSources(List<FromSourcePO> fromSourcePOs) {
        List<FromSourcePO> rFromSourcePOs = new ArrayList<>();

        for (FromSourcePO fromSourcePO: fromSourcePOs) {
            if (!fromSourceMapper.insert(fromSourcePO)) {
                throw new JdbcException(JdbcErrorEnum.INSERT);
            }

            rFromSourcePOs.add(fromSourcePO);
        }

        return rFromSourcePOs;
    }
}
