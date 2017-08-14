package cn.deepclue.datamaster.fusion.job;

import cn.deepclue.datamaster.fusion.stdin.EntropyComputeInput;
import cn.deepclue.datamaster.fusion.stdin.Shell;
import cn.deepclue.datamaster.model.ontology.PropertyType;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.job.Job;
import cn.deepclue.datamaster.fusion.config.FusionConfig;
import cn.deepclue.datamaster.streamer.util.JsonUtils;

import java.util.List;

/**
 * Created by xuzb on 03/05/2017.
 */
public class EntropyComputeJob extends Job {
    private FusionConfig fusionConfig;
    private List<PropertyType> propertyTypes;
    private List<FusionConfig> fusionSources;
    private List<MySQLTableConfig> mysqlSources;

    public EntropyComputeJob() {}

    public EntropyComputeJob(int jId, FusionConfig fusionConfig, List<PropertyType> propertyTypes, List<FusionConfig> fusionSources, List<MySQLTableConfig> mysqlSources) {
        super(jId);
        this.fusionConfig = fusionConfig;
        this.propertyTypes = propertyTypes;
        this.fusionSources = fusionSources;
        this.mysqlSources = mysqlSources;
    }

    @Override
    public boolean run() {
        EntropyComputeInput input = EntropyComputeInput.from(fusionSources, mysqlSources, fusionConfig, propertyTypes);
        String inputString = input.toJson();
        return Shell.runWithStdin("/opt/datamaster/bin/fusion/calculate-weight", inputString) == 0;
    }

    @Override
    public String serialize() {
        return JsonUtils.toJson(this);
    }

    @Override
    public void deserialize(String jsonMap) {
        EntropyComputeJob job = JsonUtils.fromJson(jsonMap, EntropyComputeJob.class);
        this.fusionConfig = job.fusionConfig;
        this.propertyTypes = job.propertyTypes;
        this.fusionSources = job.fusionSources;
        this.mysqlSources = job.mysqlSources;
    }
}
