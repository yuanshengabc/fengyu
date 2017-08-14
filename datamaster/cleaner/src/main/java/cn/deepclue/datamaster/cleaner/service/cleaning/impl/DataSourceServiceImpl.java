package cn.deepclue.datamaster.cleaner.service.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.DataHouseDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.DataSourceDao;
import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.bo.data.DataTable;
import cn.deepclue.datamaster.cleaner.domain.vo.data.DataTableListVO;
import cn.deepclue.datamaster.cleaner.exception.JdbcErrorEnum;
import cn.deepclue.datamaster.cleaner.exception.JdbcException;
import cn.deepclue.datamaster.cleaner.service.cleaning.DataHouseService;
import cn.deepclue.datamaster.cleaner.service.cleaning.DataSourceService;
import cn.deepclue.datamaster.streamer.session.MySQLSession;
import cn.deepclue.datamaster.streamer.session.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 17/03/2017.
 */
@Service("dataSourceService")
public class DataSourceServiceImpl implements DataSourceService {
    private static Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);

    @Autowired
    private DataHouseDao dataHouseDao;

    @Autowired
    private DataSourceDao dataSourceDao;

    @Autowired
    private DataHouseService dataHouseService;

    public void setDataSourceDao(DataSourceDao dataSourceDao) {
        this.dataSourceDao = dataSourceDao;
    }

    @Override
    public List<DataSource> getImportedDataSources(int dhid, int page, int pageSize) {
        return dataSourceDao.getDataSourcesByDhid(dhid, page, pageSize);
    }

    @Override
    public Boolean removeDataSource(int dsid) {
        return dataSourceDao.deleteDataSource(dsid);
    }

    @Override
    public DataTableListVO fetchDataTables(int dhid, String dbname, boolean all, int page, int pageSize) {
        DataTableListVO rsVo = new DataTableListVO();
        List<DataTable> dataTables = new ArrayList<>();

        try {
            MySQLSession session = dataHouseService.getSession(dhid);
            if (session.canConnect()) {
                session.connect();
                List<TableInfo> pageTables = (!all) ? session.getDataTables(dbname, page * pageSize, pageSize) : session.getDataTables(dbname);
                for (TableInfo table : pageTables) {
                    DataTable dataTable = new DataTable();
                    dataTable.setDhid(dhid);
                    dataTable.setDbname(dbname);
                    dataTable.setDtname(table.getTableName());
                    dataTable.setNtotal(table.getTableRows());
                    dataTables.add(dataTable);
                }

                rsVo.setDataTables(dataTables);
                rsVo.setDsCount(session.getDataTableCount(dbname));
                session.close();
            }
            return rsVo;
        } catch (Exception e) {
            String warnMsg = "连接原始数据源失败，从当地数据库直接获取！";
            logger.warn(warnMsg, e);
            throw new JdbcException(JdbcErrorEnum.FETCH_TABLE_INFO);
        }
    }
}
