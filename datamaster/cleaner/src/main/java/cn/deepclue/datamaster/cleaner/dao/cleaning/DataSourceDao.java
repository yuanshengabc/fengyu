package cn.deepclue.datamaster.cleaner.dao.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataSourceBO;
import cn.deepclue.datamaster.model.schema.RSSchema;

import java.util.List;

/**
 * Created by xuzb on 16/03/2017.
 */
public interface DataSourceDao {
    boolean insertDataSource(DataSource dataSource);

    boolean updateDataSource(DataSource dataSource);

    boolean deleteDataSource(int dsid);

    RSSchema fetchSchema(DataHouse dataHouse, DataSource dataSource);

    DataSource getDataSource(int dsid);

    List<DataSource> getDataSourcesByDhid(int dhid, int page, int pageSize);

    DataSource getDataSource(int dhid, String dbname, String dtname);

    DataSourceBO getDataSourceBO(int dsid);
}
