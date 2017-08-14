package cn.deepclue.datamaster.cleaner.dao.cleaning;

import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordList;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.cleaner.domain.vo.data.TopValueVO;
import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.transform.filter.Filter;

import java.util.List;

/**
 * Created by xuzb on 17/03/2017.
 */
public interface RecordDao {
    RecordList getRecords(RecordSource recordSource, RSSchema rsSchema, int page, int pageSize, String orderBy, boolean asc, List<Filter> filters);

    List<TopValueVO> getTopValues(RecordSource recordSource, String fieldName);

    int countTotal(RecordSource recordSource);

    int countEmpty(RecordSource recordSource, String fieldName);

    int countDistinct(RecordSource recordSource, String fieldName);

    boolean deleteIndex(String indexName);
}
