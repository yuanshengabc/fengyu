package cn.deepclue.datamaster.cleaner.web.fusion;

import cn.deepclue.datamaster.cleaner.domain.vo.fusion.DatamasterSourceListVO;
import cn.deepclue.datamaster.cleaner.service.fusion.DatamasterSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DatamasterSourceController {
    @Autowired
    private DatamasterSourceService datamasterSourceService;

    @RequestMapping(value = "/datamasterSources", method = RequestMethod.GET)
    public DatamasterSourceListVO getDatamasterSources(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int pageSize) {
        return datamasterSourceService.getDatamasterSources(page, pageSize);
    }

    @RequestMapping(path = "/datamasterSources/{dsid}", method = RequestMethod.DELETE)
    public boolean deleteDatamasterSource(@PathVariable("dsid") int dsid) {
        return datamasterSourceService.deleteDatamasterSource(dsid);
    }

}
