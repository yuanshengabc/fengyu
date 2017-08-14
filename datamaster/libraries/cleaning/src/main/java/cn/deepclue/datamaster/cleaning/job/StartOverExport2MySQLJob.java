package cn.deepclue.datamaster.cleaning.job;

import cn.deepclue.datamaster.streamer.Streamer;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.io.rwstreamer.Kafka2MySQLRWStreamer;
import cn.deepclue.datamaster.streamer.io.rwstreamer.StartOverKafka2MySQLRWStreamer;
import cn.deepclue.datamaster.streamer.transform.Rewriter;

/**
 * Created by xuzb on 19/03/2017.
 */
public class StartOverExport2MySQLJob extends Export2MySQLJob{

    public StartOverExport2MySQLJob() {
    }

    public StartOverExport2MySQLJob(Integer tid, KTopicConfig kafkaSource, MySQLTableConfig mySQLSink, Rewriter rewriter) {
        super(tid, kafkaSource, mySQLSink, rewriter);
    }

    @Override public String toString() {
        return "StartOverExport2MySQLJob{}";
    }

    @Override
    public Streamer newStreamer() {
        Kafka2MySQLRWStreamer kafka2MySQLRWStreamer = new StartOverKafka2MySQLRWStreamer();
        kafka2MySQLRWStreamer.setRewriter(getRewriter());
        return kafka2MySQLRWStreamer;
    }

    @Override
    public String getGroupName() {
        return "EXPORT";
    }
}
