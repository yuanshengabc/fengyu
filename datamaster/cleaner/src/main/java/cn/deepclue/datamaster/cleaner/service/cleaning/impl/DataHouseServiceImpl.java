package cn.deepclue.datamaster.cleaner.service.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.DataHouseDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.DatabaseDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordSourceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.bo.data.Database;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import cn.deepclue.datamaster.cleaner.service.cleaning.DataHouseService;
import cn.deepclue.datamaster.streamer.config.MySQLConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.session.MySQLSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 17/03/2017.
 */
@Service("dataHouseService")
public class DataHouseServiceImpl implements DataHouseService {
    private static Logger logger = LoggerFactory.getLogger(DataHouseServiceImpl.class);

    @Autowired
    private DataHouseDao dataHouseDao;

    @Autowired
    private DatabaseDao databaseDao;

    @Autowired
    private RecordSourceDao recordSourceDao;

    public void setDataHouseDao(DataHouseDao dataHouseDao) {
        this.dataHouseDao = dataHouseDao;
    }

    public void setDatabaseDao(DatabaseDao databaseDao) {
        this.databaseDao = databaseDao;
    }

    @Override
    public DataHouse addDataHouse(DataHouse dataHouse) {
        return dataHouseDao.createDataHouse(dataHouse);
    }

    @Override
    public Boolean updateDataHouse(DataHouse dataHouse) {
        return dataHouseDao.updateDataHouse(dataHouse);
    }

    @Override
    public Boolean removeDataHouse(int dhid) {
        return dataHouseDao.deleteDataHouse(dhid);
    }

    @Override
    public List<DataHouse> getDataHouses() {
        return dataHouseDao.getDataHouses();
    }

    /**
     * 策略：从远程数据源获取信息，获取成功则更新本地库，获取失败则从本地库获取
     *
     * @param dhid remote data house id
     * @return 远程数据源的数据库信息
     */
    @Override
    public List<Database> fetchDatabases(int dhid) {
        List<Database> databases = new ArrayList<>();
        try {
            MySQLSession mysql = getSession(dhid);
            if (mysql.canConnect()) {
                mysql.connect();
                databases = fetchDatabasesRemote(mysql, dhid);
                mysql.close();
            }
        } catch (Exception e) {
            String warnMsg = "连接原始数据源失败，从当地数据库直接获取！";
            logger.warn(warnMsg, e);
            throw new JdbcException(JdbcErrorEnum.FETCH_DB_INFO);
        }

        return databases;
    }

    @Override
    public Database fetchDatabase(int dhid, String dbname) {
        return databaseDao.getDatabase(dhid, dbname);
    }

    @Override
    public RecordSource getRecordSource(int rsid) {
        return recordSourceDao.getRecordSource(rsid);
    }

    @Override
    public DataHouse getDataHouse(int dhid) {
        return dataHouseDao.getDataHouse(dhid);
    }

    @Override
    public Boolean getDataHouseConnection(DataHouse dataHouse) {
        MySQLSession mysql = getSession(dataHouse);
        return mysql.canConnect();
    }

    private List<Database> fetchDatabasesRemote(MySQLSession session, int dhid) {
        List<String> databaseStrs = session.getDatabases();

        List<Database> databases = new ArrayList<>();
        for (String databaseStr : databaseStrs) {
            Database database = new Database();
            database.setDhid(dhid);
            database.setName(databaseStr);
            databases.add(database);
        }

        return databases;
    }


    @Override
    public MySQLSession getSession(int dhid) {
        return getSession(getDataHouse(dhid));
    }

    private MySQLSession getSession(DataHouse dataHouse) {
        MySQLTableConfig config = new MySQLTableConfig();
        MySQLConfig mysqlConfig = new MySQLConfig();
        BeanUtils.copyProperties(dataHouse, mysqlConfig);
        config.setMysqlConfig(mysqlConfig);
        return new MySQLSession(config);
    }
}
