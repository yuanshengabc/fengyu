package cn.deepclue.datamaster.streamer.io.rwstreamer;

import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.io.reader.KafkaReader;
import cn.deepclue.datamaster.streamer.io.reader.KafkaStartOverReader;
import cn.deepclue.datamaster.streamer.io.reader.Reader;
import cn.deepclue.datamaster.streamer.io.writer.MySQLWriter;
import cn.deepclue.datamaster.streamer.io.writer.Writer;

/**
 * Created by xuzb on 08/04/2017.
 */
public class StartOverKafka2MySQLRWStreamer extends Kafka2MySQLRWStreamer {
    @Override
    protected Reader newReader(Source source) {
        KafkaReader reader = new KafkaStartOverReader((KTopicConfig) source);
        reader.setGroupIdSuffix("start-over-to-mysql");

        return reader;
    }
}
