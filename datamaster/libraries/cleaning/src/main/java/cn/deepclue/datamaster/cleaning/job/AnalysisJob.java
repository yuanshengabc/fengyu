package cn.deepclue.datamaster.cleaning.job;

import cn.deepclue.datamaster.streamer.Streamer;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.job.StreamerJob;
import cn.deepclue.datamaster.streamer.config.ESTypeConfig;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.io.rwstreamer.Kafka2ESRWStreamer;

import java.util.Map;

/**
 * Created by xuzb on 19/03/2017.
 */
public class AnalysisJob extends StreamerJob {
    public AnalysisJob() {}

    public AnalysisJob(int jId, KTopicConfig source, ESTypeConfig sink) {
        super(jId, source, sink);
    }

    @Override public String toString() {
        return "AnalysisJob{}";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return KTopicConfig.class;
    }

    @Override
    public Class<? extends Sink> getSinkClass() {
        return ESTypeConfig.class;
    }

    @Override
    public Streamer newStreamer() {
        return new Kafka2ESRWStreamer();
    }

    @Override
    public String getGroupName() {
        return "ANALYSIS";
    }
}
