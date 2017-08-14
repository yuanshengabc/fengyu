package cn.deepclue.datamaster.fusion;

import cn.deepclue.datamaster.fusion.config.FusionConfig;
import cn.deepclue.datamaster.fusion.domain.FieldEntropy;
import cn.deepclue.datamaster.fusion.domain.IntervalNumber;
import cn.deepclue.datamaster.fusion.domain.PreviewStats;
import cn.deepclue.datamaster.fusion.exception.FusionException;
import cn.deepclue.datamaster.streamer.io.HDFSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 17/05/2017.
 */
public class FusionStoreImpl implements FusionStore {

    private static final Logger logger = LoggerFactory.getLogger(FusionStoreImpl.class);

    private static final String FUSION_ROOT_DIR = "/fusion/";

    public static String getSimilarityPairFilepath(FusionConfig fusionConfig) {
        return FUSION_ROOT_DIR + fusionConfig.getFusionKey() + "/similar_pair_distribution/part-00000";
    }

    public static String getEntropyFilepath(FusionConfig fusionConfig) {
        return FUSION_ROOT_DIR + fusionConfig.getFusionKey() + "/weight/part-00000";
    }

    public static String getSingleFieldFusionNumberFilepath(FusionConfig fusionConfig) {
        return FUSION_ROOT_DIR + fusionConfig.getFusionKey() + "/single_field_fusion_num/part-00000";
    }

    public static String getMultiFieldFusionNumberFilepath(FusionConfig fusionConfig) {
        return FUSION_ROOT_DIR + fusionConfig.getFusionKey() + "/object_count/part-00000";
    }

    public static String getFusionConfigDir(FusionConfig fusionConfig) {
        return FUSION_ROOT_DIR + fusionConfig.getFusionKey();
    }

    @Override
    public List<FieldEntropy> getFieldEntropies(FusionConfig fusionConfig) {
        HDFSClient hdfsClient = new HDFSClient(fusionConfig.getHdfsConfig());
        InputStream inputStream = null;
        try {
            hdfsClient.open();
            inputStream = hdfsClient.readAsStream(getEntropyFilepath(fusionConfig));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            FieldEntropy fieldEntropy = new FieldEntropy();
            List<FieldEntropy> fieldEntropies = new ArrayList<>();

            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if ("".equals(line)) {
                    throw new FusionException("Invalid entropy file format", "无效的权值格式");
                }
                ++count;

                if (count % 2 != 0) {
                    fieldEntropy = new FieldEntropy();
                    fieldEntropy.setFieldName(line);
                } else {
                    fieldEntropy.setEntropy(Double.parseDouble(line));
                    fieldEntropies.add(fieldEntropy);
                }
            }

            fieldEntropies.sort((o1, o2) -> {
                double le = o1.getEntropy();
                double re = o2.getEntropy();
                if (le - re < 0.00000001d) {
                    return 0;
                } else if (le > re) {
                    return -1;
                } else {
                    return 1;
                }
            });

            return fieldEntropies;
        } catch (IOException e) {
            logger.info("Failed to read entropy file {}", e);
            throw new FusionException("Failed to read entropy file.", "无法读取权值文件.");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.info("Failed to close input stream {}", e);
                }
            }
            hdfsClient.close();
        }
    }

    @Override
    public PreviewStats getPreviewStats(FusionConfig fusionConfig) {
        HDFSClient hdfsClient = new HDFSClient(fusionConfig.getHdfsConfig());
        InputStream inputStream = null;
        try {
            hdfsClient.open();

            inputStream = hdfsClient.readAsStream(getSimilarityPairFilepath(fusionConfig));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            PreviewStats previewStats = new PreviewStats();

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(":");
                if ("".equals(line) || values.length != 3) {
                    throw new FusionException("Invalid preview stats file format", "无效的数据量预估文件格式");
                }

                IntervalNumber intervalNumber = new IntervalNumber();
                intervalNumber.setStart(Double.parseDouble(values[0]));
                intervalNumber.setEnd(Double.parseDouble(values[1]));
                intervalNumber.setNumber(Long.parseLong(values[2]));

                previewStats.addIntervalNumber(intervalNumber);
            }

            return previewStats;
        } catch (IOException e) {
            logger.info("Failed to read preview stats file {}", e);
            throw new FusionException("Failed to read preview stats file.", "无法读取数据量预估文件.");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.info("Failed to close input stream {}", e);
                }
            }
            hdfsClient.close();
        }
    }

    @Override
    public long getSingleFieldFusionNum(FusionConfig fusionConfig) {
        HDFSClient hdfsClient = new HDFSClient(fusionConfig.getHdfsConfig());
        InputStream inputStream = null;
        try {
            hdfsClient.open();

            inputStream = hdfsClient.readAsStream(getSingleFieldFusionNumberFilepath(fusionConfig));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return Long.parseLong(bufferedReader.readLine());
        } catch (IOException e) {
            logger.info("Failed to read single field fusion num file {}", e);
            throw new FusionException("Failed to read single field fusion num file.", "无法读取单码址数据量文件.");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.info("Failed to close input stream {}", e);
                }
            }
            hdfsClient.close();
        }
    }

    @Override
    public boolean deleteEntroipes(FusionConfig fusionConfig) {
        HDFSClient hdfsClient = new HDFSClient(fusionConfig.getHdfsConfig());
        try {
            hdfsClient.open();
            String entropiesPath = getEntropyFilepath(fusionConfig);
            return hdfsClient.deleteFile(entropiesPath);
        } catch (IOException e) {
            logger.info("Failed to delete field entropies file {}", e);
        } finally {
            hdfsClient.close();
        }

        return false;
    }

    @Override
    public boolean deleteStore(FusionConfig fusionConfig) {
        HDFSClient hdfsClient = new HDFSClient(fusionConfig.getHdfsConfig());

        try {
            hdfsClient.open();
            String fusionConfigDir = getFusionConfigDir(fusionConfig);
            return hdfsClient.deleteDir(fusionConfigDir);
        } catch (IOException e) {
            logger.info("Failed to delete fusion config store {}", e);
        } finally {
            hdfsClient.close();
        }

        return false;
    }

    @Override
    public long getMultiFieldFusionNum(FusionConfig fusionConfig) {
        HDFSClient hdfsClient = new HDFSClient(fusionConfig.getHdfsConfig());
        InputStream inputStream = null;
        try {
            hdfsClient.open();

            inputStream = hdfsClient.readAsStream(getMultiFieldFusionNumberFilepath(fusionConfig));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return Long.parseLong(bufferedReader.readLine());
        } catch (IOException e) {
            logger.info("Failed to read single field fusion num file {}", e);
            throw new FusionException("Failed to read single field fusion num file.", "无法读取单码址数据量文件.");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.info("Failed to close input stream {}", e);
                }
            }
            hdfsClient.close();
        }
    }
}
