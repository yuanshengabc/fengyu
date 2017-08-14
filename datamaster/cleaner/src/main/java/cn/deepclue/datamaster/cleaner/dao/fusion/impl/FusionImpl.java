package cn.deepclue.datamaster.cleaner.dao.fusion.impl;

import cn.deepclue.datamaster.cleaner.config.CleanerConfigurationProperties;
import cn.deepclue.datamaster.cleaner.dao.fusion.FusionDao;
import cn.deepclue.datamaster.cleaner.domain.bo.data.RecordSource;
import cn.deepclue.datamaster.fusion.FusionStore;
import cn.deepclue.datamaster.fusion.FusionStoreImpl;
import cn.deepclue.datamaster.fusion.config.FusionConfig;
import cn.deepclue.datamaster.fusion.domain.FieldEntropy;
import cn.deepclue.datamaster.fusion.domain.PreviewStats;
import cn.deepclue.datamaster.fusion.exception.FusionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magneto on 17-5-23.
 */
@Repository("fusionDao")
public class FusionImpl implements FusionDao {

    private Logger logger = LoggerFactory.getLogger(FusionImpl.class);

    private FusionStore fusionStore = new FusionStoreImpl();

    @Autowired
    private CleanerConfigurationProperties properties;

    @Override
    public List<FieldEntropy> getFieldEntropies(RecordSource recordSource) {
        FusionConfig fusionConfig = buildFusionConfig(recordSource);
        try {
            return fusionStore.getFieldEntropies(fusionConfig);
        } catch (FusionException e) {
            logger.info("Entropy file does not exists {}", e);
            return new ArrayList<>();
        }
    }

    @Override
    public PreviewStats getPreviewStats(RecordSource recordSource) {
        FusionConfig fusionConfig = buildFusionConfig(recordSource);
        try {
            return fusionStore.getPreviewStats(fusionConfig);
        } catch (FusionException e) {
            logger.info("Preview stats file does not exists {}", e);
            return null;
        }
    }

    @Override
    public Long getSingleFieldFusionNum(RecordSource recordSource) {
        FusionConfig fusionConfig = buildFusionConfig(recordSource);
        try {
            return fusionStore.getSingleFieldFusionNum(fusionConfig);
        } catch (FusionException e) {
            logger.info("Single field fusion number file does not exists {}", e);
            return null;
        }
    }

    @Override
    public Long getMultiFieldFusionNum(RecordSource recordSource) {
        FusionConfig fusionConfig = buildFusionConfig(recordSource);
        try {
            return fusionStore.getMultiFieldFusionNum(fusionConfig);
        } catch (FusionException e) {
            logger.info("Single field fusion number file does not exists {}", e);
            return null;
        }
    }

    @Override
    public boolean deleteEntropies(RecordSource recordSource) {
        FusionConfig fusionConfig = buildFusionConfig(recordSource);
        return fusionStore.deleteEntroipes(fusionConfig);
    }

    @Override
    public boolean deleteFusionStore(RecordSource recordSource) {
        FusionConfig fusionConfig = buildFusionConfig(recordSource);
        return fusionStore.deleteStore(fusionConfig);
    }

    private FusionConfig buildFusionConfig(RecordSource recordSource) {
        FusionConfig fusionConfig = new FusionConfig();
        fusionConfig.setFusionKey(recordSource.getFusionKey());
        fusionConfig.setHdfsConfig(properties.getHdfsConfig());

        return fusionConfig;
    }
}
