package cn.deepclue.datamaster.cleaner.web.cleaning;

import cn.deepclue.datamaster.cleaner.domain.vo.data.DataTableListVO;
import cn.deepclue.datamaster.cleaner.service.cleaning.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by xuzb on 16/03/2017.
 */
@RestController
public class DataSourceController {
    @Autowired
    private DataSourceService dataSourceService;

    public void setDataSourceService(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @RequestMapping(path = "/dataSources/{dsid}", method = RequestMethod.DELETE)
    public Boolean removeDataSource(@PathVariable("dsid") int dsid) {
        return dataSourceService.removeDataSource(dsid);
    }

    @RequestMapping(value = "/dataSources", method = RequestMethod.GET)
    public DataTableListVO getDataTables(@RequestParam("dhid") int dhid,
                                         @RequestParam String dbname,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "-1") int pageSize) {
        return dataSourceService.fetchDataTables(dhid, dbname, false, page, pageSize);
    }

    @RequestMapping(value = "/allDataSources", method = RequestMethod.GET)
    public DataTableListVO getDataTables(@RequestParam("dhid") int dhid,
                                         @RequestParam String dbname) {
        return dataSourceService.fetchDataTables(dhid, dbname, true, -1, -1);
    }
}
