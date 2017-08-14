package cn.deepclue.datamaster.fusion.stdin;

import cn.deepclue.datamaster.fusion.config.FusionConfig;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzb on 18/05/2017.
 */
public class EntropyComputeInput extends JsonSerializable {
    private String fsid;
    private String address;
    private List<String> fields;
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

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getFusionKeys() {
        return fusionKeys;
    }

    public void setFusionKeys(List<String> fusionKeys) {
        this.fusionKeys = fusionKeys;
    }

    public List<DBConfig> getDbConfigs() {
        return dbConfigs;
    }

    public void setDbConfigs(List<DBConfig> dbConfigs) {
        this.dbConfigs = dbConfigs;
    }

    public static EntropyComputeInput from(List<FusionConfig> fusionSources, List<MySQLTableConfig> tableConfigs, FusionConfig fusionConfig, List<PropertyType> propertyTypes) {
        EntropyComputeInput input = new EntropyComputeInput();
        input.address = fusionConfig.getHdfsConfig().getServerString();
        input.fsid = fusionConfig.getFusionKey();
        input.fields = new ArrayList<>();
        for (PropertyType propertyType : propertyTypes) {
            input.fields.add(propertyType.getName());
        }

        input.fusionKeys = new ArrayList<>();
        for (FusionConfig fusionSource : fusionSources) {
            input.fusionKeys.add(fusionSource.getFusionKey());
        }

        input.dbConfigs = DBConfig.from(tableConfigs);
        return input;
    }
}
