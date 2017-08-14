package cn.deepclue.datamaster.cleaner.dao.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.DataSourceDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.mapper.DataSourceMapper;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataSourceBO;
import cn.deepclue.datamaster.cleaner.exception.BizErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.CleanerException;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.MySQLConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.io.reader.MySQLReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by magneto on 17-3-30.
 */
@Repository("dataSourceDao")
public class DataSourceImpl implements DataSourceDao {
    @Autowired
    private DataSourceMapper dataSourceMapper;
    private final byte[] lock = new byte[0];

    /**
     * 数据源不存在则插入数据源，返回true，同时设置dsid
     * 数据源存在则设置dsid，返回false
     *
     * @param dataSource 数据源
     * @return 是否新增
     */
    @Override
    public boolean insertDataSource(DataSource dataSource) {
        synchronized (lock) {
            DataSource tempDataSource = dataSourceMapper.getDataSource(dataSource.getDhid(), dataSource.getDbname(), dataSource.getDtname());
            if (tempDataSource == null) {
                if (dataSourceMapper.insertDataSource(dataSource)) {
                    return true;
                }

                throw new JdbcException(JdbcErrorEnum.INSERT_DATASOURCE);
            } else {
                dataSource.setDsid(tempDataSource.getDsid());
                return false;
            }
        }
    }

    @Override
    public boolean updateDataSource(DataSource dataSource) {
        if (dataSourceMapper.updateDataSource(dataSource)) {
            return true;
        }

        throw new JdbcException(JdbcErrorEnum.UPDATE);
    }

    @Override
    public boolean deleteDataSource(int dsid) {
        DataSource dataSource = getDataSource(dsid);
        return (dataSource != null && dataSourceMapper.deleteDataSource(dsid));
    }

    @Override
    public RSSchema fetchSchema(DataHouse dataHouse, DataSource dataSource) {
        MySQLConfig mySQLConfig = new MySQLConfig();
        mySQLConfig.setDatabase(dataSource.getDbname());
        mySQLConfig.setIp(dataHouse.getIp());
        mySQLConfig.setPort(dataHouse.getPort());
        mySQLConfig.setUsername(dataHouse.getUsername());
        mySQLConfig.setPassword(dataHouse.getPassword());

        MySQLTableConfig mySQLTableConfig = new MySQLTableConfig();
        mySQLTableConfig.setTableName(dataSource.getDtname());
        mySQLTableConfig.setMysqlConfig(mySQLConfig);

        MySQLReader reader = null;
        try {
            reader = new MySQLReader(mySQLTableConfig);
            reader.open();
            RSSchema schema = reader.readSchema();
            if (schema.count() == 0) {
                throw new CleanerException("Schema is empty in table: " + dataSource.getDtname(),
                        "表" + dataSource.getDtname() + "中未设置模型。", BizErrorEnum.SCHEMA_IS_EMPTY);
            }

            return schema;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    @Override
    public DataSource getDataSource(int dsid) {
        return dataSourceMapper.getDataSourceByDsid(dsid);
    }

    @Override
    public List<DataSource> getDataSourcesByDhid(int dhid, int page, int pageSize) {
        return dataSourceMapper.getDataSourcesByDhid(dhid, page * pageSize, pageSize);
    }

    @Override
    public DataSource getDataSource(int dhid, String dbname, String dtname) {
        return dataSourceMapper.getDataSource(dhid, dbname, dtname);
    }

    @Override
    public DataSourceBO getDataSourceBO(int dsid) {
        return dataSourceMapper.getDataSourceBO(dsid);
    }
}
