package cn.deepclue.datamaster.cleaner.dao.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.DataHouseDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.DataHouseMapper;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by xuzb on 27/03/2017.
 */
@Repository("dataHouseDao")
public class DataHouseImpl implements DataHouseDao {

    @Autowired
    private DataHouseMapper dataHouseMapper;

    @Override
    public DataHouse getDataHouse(int dhid) {
        return dataHouseMapper.getDataHouse(dhid);
    }

    @Override
    public List<DataHouse> getDataHouses() {
        int uid = 1;
        return dataHouseMapper.getDataHouses(uid);
    }

    @Override
    public DataHouse createDataHouse(DataHouse dataHouse) {
        int uid = 1;
        dataHouse.setUid(1);
        dataHouse.setDhid(null);
        if (dataHouseMapper.insertDataHouse(uid, dataHouse) && dataHouse.getDhid() != null) {
            return dataHouse;
        }

        throw new JdbcException(JdbcErrorEnum.CREATE_DATAHOUSE);
    }

    @Override
    public Boolean deleteDataHouse(int dhid) {
        DataHouse dataHouse = getDataHouse(dhid);
        int uid = 1;
        return (dataHouse != null && dataHouseMapper.deleteDataHouse(uid, dhid));
    }

    @Override
    public Boolean updateDataHouse(DataHouse dataHouse) {
        dataHouse.setUid(1);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return (dataHouseMapper.getDataHouse(dataHouse.getDhid()) != null &&
                dataHouseMapper.updateDataHouse(dataHouse, now));
    }
}
