package cn.deepclue.datamaster.cleaner.service.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.bo.data.Database;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.streamer.session.MySQLSession;

import java.util.List;

/**
 * Created by xuzb on 14/03/2017.
 */
public interface DataHouseService {
    /**
     * Add and manage a new data house.
     * @param dataHouse target remote data house.
     * @return True if success else false.
     */
    DataHouse addDataHouse(DataHouse dataHouse);

    /**
     * Add and manage a new data house.
     * @param dataHouse target remote data house.
     * @return True if success else false.
     */
    Boolean updateDataHouse(DataHouse dataHouse);

    /**
     * Remove data house from managed list.
     * @param dhid target data house id.
     * @return True if success else false.
     */
    Boolean removeDataHouse(int dhid);

    /**
     * Get managed data house list.
     * @return list of data houses
     */
    List<DataHouse> getDataHouses();

    /**
     * Fetch data source list from remote data house (database).
     * @param dhid remote data house.
     * @return List of data sources
     */
    List<Database> fetchDatabases(int dhid);

    /**
     * @param dhid
     * @param dbname
     * @return database fetched
     */
    Database fetchDatabase(int dhid, String dbname);


    /**
     * @param rsid
     * @return record source fetched
     */
    RecordSource getRecordSource(int rsid);

    /**
     * Get data house by dhid
     * @param dhid data house id
     * @return dhid
     */
    DataHouse getDataHouse(int dhid);

    /**
     * whether connection can be built of dataHouse
     * @param dataHouse
     * @return true if can be built else false
     */
    Boolean getDataHouseConnection(DataHouse dataHouse);

    /**
     * get mysql session
     * @param dhid
     * @return
     */
    MySQLSession getSession(int dhid);
}
