package cn.deepclue.datamaster.cleaner.dao.fusion.impl;

import cn.deepclue.datamaster.cleaner.dao.fusion.DatamasterSourceDao;
import cn.deepclue.datamaster.cleaner.dao.fusion.mapper.DatamasterSourceMapper;
import cn.deepclue.datamaster.cleaner.domain.po.fusion.DatamasterSourcePO;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunxingwen on 17-5-18.
 */
@Service("datamasterSourceDao")
public class DatamasterSourceImpl implements DatamasterSourceDao {
    @Autowired
    private DatamasterSourceMapper datamasterSourceMapper;

    @Override
    public Integer getDatamasterSourceCount() {
        return datamasterSourceMapper.getDatamasterSourceCount();
    }

    @Override
    public List<DatamasterSourcePO> getDatamasterSources(int page, int pageSize) {
        return datamasterSourceMapper.getDatamasterSources(page * pageSize, pageSize);
    }

    @Override
    public DatamasterSourcePO insertDatamasterSource(DatamasterSourcePO datamasterSourcePO) {
        if (datamasterSourceMapper.insertDatamasterSource(datamasterSourcePO)) {
            return datamasterSourcePO;
        }
        throw new JdbcException(JdbcErrorEnum.INSERT);
    }

    @Override
    public boolean deleteDatamasterSources(int dsid) {
        if (datamasterSourceMapper.deleteDatamasterSource(dsid)) {
            return true;
        }
        throw new JdbcException(JdbcErrorEnum.DELETE);
    }

    @Override
    public DatamasterSourcePO getDatamasterSourceByDsid(int dsid) {
        return datamasterSourceMapper.getDatamasterSourceByDsid(dsid);
    }

    @Override
    public List<DatamasterSourcePO> getDatamasterSourcesByDsids(List<Integer> dsids) {
        return datamasterSourceMapper.getDatamasterSourcesByDsids(dsids);
    }

    @Override
    public List<DatamasterSourcePO> getDatamasterSourcesByRsid(int rsid) {
        return datamasterSourceMapper.getDatamasterSourceByRsid(rsid);
    }

    @Override
    public List<DatamasterSourcePO> getDatamasterSources() {
        return datamasterSourceMapper.getAllDatamasterSources();
    }
}
