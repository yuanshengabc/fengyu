package cn.deepclue.datamaster.cleaner.web.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.data.DataHouse;
import cn.deepclue.datamaster.cleaner.domain.bo.data.Database;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.service.cleaning.DataHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by xuzb on 16/03/2017.
 */
@RestController
public class DataHouseController {

    @Autowired
    private DataHouseService dataHouseService;

    public void setDataHouseService(DataHouseService dataHouseService) {
        this.dataHouseService = dataHouseService;
    }

    @RequestMapping(path = "/dataHouseConnection", method = RequestMethod.GET)
    public Boolean getDataHouseConnection(DataHouse dataHouse) {
        return dataHouseService.getDataHouseConnection(dataHouse);
    }

    @RequestMapping(path = "/dataHouses", method = RequestMethod.GET)
    public List<DataHouse> getDataHouses() {
        return dataHouseService.getDataHouses();
    }

    @RequestMapping(path = "/dataHouses", method = RequestMethod.POST)
    public DataHouse addDataHouse(@Valid DataHouse dataHouse) {
        return dataHouseService.addDataHouse(dataHouse);
    }

    @RequestMapping(path = "/dataHouses/{dhid}", method = RequestMethod.POST)
    public Boolean updateDataHouse(@PathVariable("dhid") int dhid, @Valid DataHouse dataHouse) {
        dataHouse.setDhid(dhid);
        return dataHouseService.updateDataHouse(dataHouse);
    }

    @RequestMapping(path = "/dataHouses/{dhid}", method = RequestMethod.DELETE)
    public Boolean removeDataHouse(@PathVariable("dhid") int dhid) {
        return dataHouseService.removeDataHouse(dhid);
    }

    @RequestMapping(path = "/dataHouses/{dhid}", method = RequestMethod.GET)
    public DataHouse getDataHouse(@PathVariable("dhid") int dhid) {
        return dataHouseService.getDataHouse(dhid);
    }

    @RequestMapping("/dataHouses/{dhid}/databases")
    public List<Database> fetchDatabases(@PathVariable("dhid") int dhid) {
        return dataHouseService.fetchDatabases(dhid);
    }

    @RequestMapping("/dataHouses/{dhid}/databases/{dbname}")
    public Database fetchDatabase(@PathVariable("dhid") int dhid, @PathVariable("dbname") String dbname) {
        return dataHouseService.fetchDatabase(dhid, dbname);
    }

    @RequestMapping("/recordSources/{rsid}")
    public RecordSource getRecordSource(@PathVariable("rsid") int rsid) {
        return dataHouseService.getRecordSource(rsid);
    }

}
