package cn.deepclue.datamaster.fusion.job;

import cn.deepclue.datamaster.fusion.config.FusionConfig;
import cn.deepclue.datamaster.fusion.domain.FieldEntropy;
import cn.deepclue.datamaster.fusion.stdin.Shell;
import cn.deepclue.datamaster.fusion.stdin.SimilarityComputeInput;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.job.Job;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.streamer.util.JsonUtils;

import java.util.List;

/**
 * Created by xuzb on 03/05/2017.
 */
public class SimilarityComputeJob extends Job {
    private FusionConfig fusionConfig;
    private List<FieldEntropy> entropies;
    private Double threshold;
    private List<FusionConfig> fusionSources;
    List<MySQLTableConfig> mysqlSources;

    public SimilarityComputeJob() {}

    public SimilarityComputeJob(int jId, List<MySQLTableConfig> mysqlSources, List<FusionConfig> fusionSources, FusionConfig fusionConfig, List<FieldEntropy> entropies, Double threshold) {
        super(jId);
        this.mysqlSources = mysqlSources;
        this.fusionConfig = fusionConfig;
        this.entropies = entropies;
        this.threshold = threshold;
        this.fusionSources = fusionSources;
    }

    @Override
    public boolean run() {
        SimilarityComputeInput input = SimilarityComputeInput.from(mysqlSources, fusionSources, fusionConfig, entropies, threshold);
        String inputString = input.toJson();
        return Shell.runWithStdin("/opt/datamaster/bin/fusion/calculate-similar-pair", inputString) == 0;
    }

    @Override
    public String serialize() {
        return JsonUtils.toJson(this);
    }

    @Override
    public void deserialize(String jsonMap) {
        SimilarityComputeJob job = JsonUtils.fromJson(jsonMap, SimilarityComputeJob.class);
        this.fusionConfig = job.fusionConfig;
        this.entropies = job.entropies;
        this.threshold = job.threshold;
        this.fusionSources = job.fusionSources;
        this.mysqlSources = job.mysqlSources;
    }
}
