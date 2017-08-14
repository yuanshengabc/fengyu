package cn.deepclue.datamaster.fusion;

import cn.deepclue.datamaster.fusion.config.FusionConfig;
import cn.deepclue.datamaster.fusion.domain.FieldEntropy;
import cn.deepclue.datamaster.fusion.domain.PreviewStats;

import java.util.List;

/**
 * Created by xuzb on 17/05/2017.
 */
public interface FusionStore {
    List<FieldEntropy> getFieldEntropies(FusionConfig fusionConfig);

    PreviewStats getPreviewStats(FusionConfig fusionConfig);

    long getSingleFieldFusionNum(FusionConfig fusionConfig);

    boolean deleteEntroipes(FusionConfig fusionConfig);
    boolean deleteStore(FusionConfig fusionConfig);

    long getMultiFieldFusionNum(FusionConfig fusionConfig);
}
