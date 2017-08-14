package cn.deepclue.datamaster.fusion.stdin;

import cn.deepclue.datamaster.fusion.config.FusionConfig;
import cn.deepclue.datamaster.fusion.domain.FieldEntropy;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 18/05/2017.
 */
public class SimilarityComputeInput extends JsonSerializable {
    private String fsid;
    private String address;
    private List<FieldWeight> fieldWeights;
    private Double threshold;
    private String specialField;
    private List<String> fusionKeys;
    private List<DBConfig> dbConfigs;

    public String getFsid() {
        return fsid;
    }

    public void setFsid(String fsid) {
        this.fsid = fsid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<FieldWeight> getFieldWeights() {
        return fieldWeights;
    }

    public void setFieldWeights(List<FieldWeight> fieldWeights) {
        this.fieldWeights = fieldWeights;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public String getSpecialField() {
        return specialField;
    }

    public void setSpecialField(String specialField) {
        this.specialField = specialField;
    }

    public List<String> getFusionKeys() {
        return fusionKeys;
    }

    public void setFusionKeys(List<String> fusionKeys) {
        this.fusionKeys = fusionKeys;
    }

    public static SimilarityComputeInput from(List<MySQLTableConfig> mysqlSources, List<FusionConfig> fusionSources, FusionConfig fusionConfig, List<FieldEntropy> entropies, Double threshold) {
        SimilarityComputeInput input = new SimilarityComputeInput();
        input.fsid = fusionConfig.getFusionKey();
        input.address = fusionConfig.getHdfsConfig().getServerString();
        input.fieldWeights = new ArrayList<>();
        for (FieldEntropy fieldEntropy : entropies) {
            if (fieldEntropy.isUniqued()) {
                input.specialField = fieldEntropy.getFieldName();
                continue;
            }

            FieldWeight fieldWeight = new FieldWeight();
            fieldWeight.setName(fieldEntropy.getFieldName());
            fieldWeight.setWeight(fieldEntropy.getEntropy());

            input.fieldWeights.add(fieldWeight);
        }

        input.fusionKeys = new ArrayList<>();
        for (FusionConfig fusionSource : fusionSources) {
            input.fusionKeys.add(fusionSource.getFusionKey());
        }

        input.dbConfigs = DBConfig.from(mysqlSources);

        input.threshold = threshold;

        return input;
    }
}
