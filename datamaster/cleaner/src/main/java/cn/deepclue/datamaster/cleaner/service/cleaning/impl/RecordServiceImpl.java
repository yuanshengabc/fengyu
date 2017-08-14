package cn.deepclue.datamaster.cleaner.service.cleaning.impl;

import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordDao;
import cn.deepclue.datamaster.cleaner.dao.cleaning.RecordSourceDao;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordList;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.vo.data.RecordStatsRespVO;
import cn.deepclue.datamaster.cleaner.domain.vo.data.TopValueVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.FilterVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.RecordsReqVO;
import cn.deepclue.datamaster.cleaner.service.cleaning.RecordService;
import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xuzb on 17/03/2017.
 */
@Service("recordService")
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordDao recordDao;

    @Autowired
    private RecordSourceDao recordSourceDao;

    public void setRecordDao(RecordDao recordDao) {
        this.recordDao = recordDao;
    }

    public void setRecordSourceDao(RecordSourceDao recordSourceDao) {
        this.recordSourceDao = recordSourceDao;
    }

    @Override
    public RecordList getRecords(RecordsReqVO recordsReqVO) {
        RecordSource recordSource = recordSourceDao.getRecordSource(recordsReqVO.getRsid());
        RSSchema rsSchema = recordSourceDao.getRSSchema(recordsReqVO.getRsid());
        return recordDao.getRecords(recordSource, rsSchema,
                recordsReqVO.getPage(), recordsReqVO.getPageSize(),
                recordsReqVO.getOrderBy(), recordsReqVO.isAsc(), FilterVO.toFilters(recordsReqVO.getFilters()));
    }

    @Override
    public List<TopValueVO> getTopValues(int rsid, String fieldName) {
        RecordSource recordSource = recordSourceDao.getRecordSource(rsid);
        return recordDao.getTopValues(recordSource, fieldName);
    }

    @Override public RecordStatsRespVO stats(int rsid, String fieldName) {
        RecordSource recordSource = recordSourceDao.getRecordSource(rsid);
        RecordStatsRespVO recordStatsRespVO = new RecordStatsRespVO();
        int total = recordDao.countTotal(recordSource);
        if (total != 0) {
            int empty = recordDao.countEmpty(recordSource, fieldName);
            int nonempty = total>=empty?total - empty:0;
            int distinct = recordDao.countDistinct(recordSource, fieldName);
            double integrity = (double) nonempty / total;
            double richness = (double) distinct /total;
            int repeat = (total >= distinct)?total-distinct:0;

            recordStatsRespVO.setEmpty(empty);
            recordStatsRespVO.setTotal(total);
            recordStatsRespVO.setNonempty(nonempty);
            recordStatsRespVO.setIntegrity(integrity);
            recordStatsRespVO.setDistinct(distinct);
            recordStatsRespVO.setRichness(richness);
            recordStatsRespVO.setRepeat(repeat);
        }

        return recordStatsRespVO;
    }
}
