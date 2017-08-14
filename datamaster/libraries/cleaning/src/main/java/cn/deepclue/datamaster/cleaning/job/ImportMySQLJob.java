package cn.deepclue.datamaster.cleaning.job;

import cn.deepclue.datamaster.streamer.Streamer;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.job.StreamerJob;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.io.rwstreamer.MySQL2KafkaRWStreamer;

/**
 * Created by xuzb on 17/03/2017.
 */
public class ImportMySQLJob extends StreamerJob {
    public ImportMySQLJob() {}

    public ImportMySQLJob(int jId, MySQLTableConfig source, KTopicConfig sink) {
        super(jId, source, sink);
    }

    @Override public String toString() {
        return "ImportMySQLJob{}";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return MySQLTableConfig.class;
    }

    @Override
    public Class<? extends Sink> getSinkClass() {
        return KTopicConfig.class;
    }

    @Override
    public Streamer newStreamer() {
        return new MySQL2KafkaRWStreamer();
    }

    @Override
    public String getGroupName() {
        return "IMPORT";
    }
}
