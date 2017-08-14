package cn.deepclue.datamaster.cleaner.service.cleaning;

import cn.deepclue.datamaster.cleaner.domain.po.data.DataSource;
import cn.deepclue.datamaster.cleaner.domain.vo.data.DataTableListVO;

import java.util.List;

/**
 * Created by xuzb on 15/03/2017.
 */
public interface DataSourceService {
    /**
     * Get imported data source list of the data house, including importing and imported.
     *
     * @param dhid     data house id.
     * @param page     page index starting from 0.
     * @param pageSize page size.
     * @return list of imported data sources
     */
    List<DataSource> getImportedDataSources(int dhid, int page, int pageSize);


    /**
     * Remove data source by data source id.
     *
     * @param dsid target data source id.
     * @return data source
     */
    Boolean removeDataSource(int dsid);

    /**
     * all data sources with same dhid
     */
    DataTableListVO fetchDataTables(int dhid, String dbname, boolean all, int page, int pageSize);
}
