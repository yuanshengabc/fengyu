package cn.deepclue.datamaster.fusion;

import cn.deepclue.datamaster.fusion.config.FusionConfig;
import cn.deepclue.datamaster.fusion.domain.FieldEntropy;
import cn.deepclue.datamaster.fusion.domain.IntervalNumber;
import cn.deepclue.datamaster.fusion.domain.PreviewStats;
import cn.deepclue.datamaster.streamer.config.HDFSConfig;
import cn.deepclue.datamaster.streamer.io.HDFSClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by xuzb on 23/05/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FusionStoreImplTests {

    @Autowired
    private FusionIntegTestProperties properties;

    private FusionConfig fusionConfig;

    FusionStore fusionStore;

    @Before
    public void setUp() {
        HDFSConfig hdfsConfig = properties.getHdfsConfig();
        fusionConfig = new FusionConfig();
        fusionConfig.setFusionKey("1");
        fusionConfig.setHdfsConfig(hdfsConfig);

        HDFSClient hdfsClient = new HDFSClient(hdfsConfig);

        try {
            hdfsClient.open();
            hdfsClient.write(FusionStoreImpl.getEntropyFilepath(fusionConfig), "f1\n0.93\nf2\n0.82");
            hdfsClient.write(FusionStoreImpl.getSimilarityPairFilepath(fusionConfig), "0.50:0.60:100\n0.60:0.70:200");
            hdfsClient.write(FusionStoreImpl.getSingleFieldFusionNumberFilepath(fusionConfig), "2000");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            hdfsClient.close();
        }

        fusionStore = new FusionStoreImpl();
    }

    @Test
    public void getFieldEntropies() {
        List<FieldEntropy> fieldEntropyList = fusionStore.getFieldEntropies(fusionConfig);
        assertEquals(fieldEntropyList.size(), 2);

        FieldEntropy fieldEntropy1 = fieldEntropyList.get(0);
        assertEquals(fieldEntropy1.getFieldName(), "f1");
        assertEquals(fieldEntropy1.getEntropy(), 0.93d, 0.00001d);

        FieldEntropy fieldEntropy2 = fieldEntropyList.get(1);
        assertEquals(fieldEntropy2.getFieldName(), "f2");
        assertEquals(fieldEntropy2.getEntropy(), 0.82d, 0.00001d);
    }

    @Test
    public void getPreviewStats() {
        PreviewStats previewStats = fusionStore.getPreviewStats(fusionConfig);

        List<IntervalNumber> intervalNumbers = previewStats.getIntervalNumbers();
        assertEquals(intervalNumbers.size(), 2);

        IntervalNumber intervalNumber1 = intervalNumbers.get(0);
        assertEquals(intervalNumber1.getStart(), 0.50d, 0.000001d);
        assertEquals(intervalNumber1.getEnd(), 0.60d, 0.000001d);
        assertEquals(intervalNumber1.getNumber(), 100);

        IntervalNumber intervalNumber2 = intervalNumbers.get(1);
        assertEquals(intervalNumber2.getStart(), 0.60d, 0.000001d);
        assertEquals(intervalNumber2.getEnd(), 0.70d, 0.000001d);
        assertEquals(intervalNumber2.getNumber(), 200);
    }

    @Test
    public void getSingleFieldFusionNum() {
        long num = fusionStore.getSingleFieldFusionNum(fusionConfig);

        assertEquals(num, 2000);
    }

    @Test
    public void deleteStore() {
        assertTrue(fusionStore.deleteStore(fusionConfig));
    }
}
