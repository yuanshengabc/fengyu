package cn.deepclue.datamaster.cleaning.job;

import cn.deepclue.datamaster.streamer.Streamer;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.job.StreamerJob;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.io.rwstreamer.Kafka2MySQLRWStreamer;
import cn.deepclue.datamaster.streamer.transform.Rewriter;

/**
 * Created by xuzb on 19/03/2017.
 */
public class Export2MySQLJob extends StreamerJob {
    public Export2MySQLJob() {}

    public Export2MySQLJob(int jId, KTopicConfig source, MySQLTableConfig sink, Rewriter rewriter) {
        super(jId, source, sink);

        setRewriter(rewriter);
    }

    @Override public String toString() {
        return "Export2MySQLJob{}";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return KTopicConfig.class;
    }

    @Override
    public Class<? extends Sink> getSinkClass() {
        return MySQLTableConfig.class;
    }

    @Override
    public Streamer newStreamer() {
        Kafka2MySQLRWStreamer kafka2MySQLRWStreamer = new Kafka2MySQLRWStreamer();
        kafka2MySQLRWStreamer.setRewriter(getRewriter());
        return kafka2MySQLRWStreamer;
    }

    @Override
    public String getGroupName() {
        return "EXPORT";
    }
}
