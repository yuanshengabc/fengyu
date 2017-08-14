package cn.deepclue.datamaster.cleaner.dao.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;

import java.util.List;

/**
 * Created by xuzb on 16/03/2017.
 */
public interface DataHouseDao {
    DataHouse getDataHouse(int dhid);

    List<DataHouse> getDataHouses();

    DataHouse createDataHouse(DataHouse dataHouse);

    Boolean deleteDataHouse(int dhid);

    Boolean updateDataHouse(DataHouse dataHouse);
}
