package cn.deepclue.datamaster.cleaner.dao.fusion;

import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.fusion.domain.FieldEntropy;
import cn.deepclue.datamaster.fusion.domain.PreviewStats;

import java.util.List;

/**
 * Created by magneto on 17-5-23.
 */
public interface FusionDao {

    List<FieldEntropy> getFieldEntropies(RecordSource recordSource);

    PreviewStats getPreviewStats(RecordSource recordSource);

    Long getSingleFieldFusionNum(RecordSource recordSource);
    Long getMultiFieldFusionNum(RecordSource recordSource);

    boolean deleteEntropies(RecordSource recordSource);

    boolean deleteFusionStore(RecordSource recordSource);
}
