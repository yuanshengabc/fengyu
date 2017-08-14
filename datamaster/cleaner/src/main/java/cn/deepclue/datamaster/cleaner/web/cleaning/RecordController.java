package cn.deepclue.datamaster.cleaner.web.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordList;
import cn.deepclue.datamaster.cleaner.domain.vo.data.RecordStatsRespVO;
import cn.deepclue.datamaster.cleaner.domain.vo.data.TopValueVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.RecordsReqVO;
import cn.deepclue.datamaster.cleaner.service.cleaning.RecordService;
import cn.deepclue.datamaster.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by xuzb on 17/03/2017.
 */
@RestController
public class RecordController {

    @Autowired
    private RecordService recordService;

    public void setRecordService(RecordService recordService) {
        this.recordService = recordService;
    }

    @RequestMapping(path = "/records", method = RequestMethod.POST)
    public RecordList getRecords(@RequestBody RecordsReqVO recordsReqVO) {
        return recordService.getRecords(recordsReqVO);
    }

    @RequestMapping(path = "/topValues", method = RequestMethod.GET)
    public List<TopValueVO> getTopValues(@RequestParam int rsid,
                                       @RequestParam String fieldName) {
        return recordService.getTopValues(rsid, fieldName);
    }

    @RequestMapping(path = "/stats", method = RequestMethod.GET)
    public RecordStatsRespVO stats(@RequestParam int rsid,
                           @RequestParam String fieldName) {
        return recordService.stats(rsid, fieldName);
    }
}
