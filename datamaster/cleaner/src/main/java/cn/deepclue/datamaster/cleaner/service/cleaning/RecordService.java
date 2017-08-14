package cn.deepclue.datamaster.cleaner.service.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordList;
import cn.deepclue.datamaster.cleaner.domain.vo.data.RecordStatsRespVO;
import cn.deepclue.datamaster.cleaner.domain.vo.data.TopValueVO;
import cn.deepclue.datamaster.cleaner.domain.vo.workspace.cleaning.RecordsReqVO;
import cn.deepclue.datamaster.model.Record;

import java.util.List;

/**
 * Created by xuzb on 17/03/2017.
 */
public interface RecordService {
    /**
     * Get data records.
     * @return list of data records.
     */
    RecordList getRecords(RecordsReqVO recordsReqVO);

    /**
     * Get top values of a data source.
     * @param rsid target data source.
     * @param fieldName aggregating field name
     * @return list of top values.
     */
    List<TopValueVO> getTopValues(int rsid, String fieldName);

    /**
     * statistic field info includes empty, nonempty, integrity, distinct, total, richness
     * @param rsid
     * @param fieldName
     * @return
     */
    RecordStatsRespVO stats(int rsid, String fieldName);
}
