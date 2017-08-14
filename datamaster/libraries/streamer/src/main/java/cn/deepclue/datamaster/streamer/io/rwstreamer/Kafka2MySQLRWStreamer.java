package cn.deepclue.datamaster.streamer.io.rwstreamer;

import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.io.reader.KafkaReader;
import cn.deepclue.datamaster.streamer.io.reader.Reader;
import cn.deepclue.datamaster.streamer.io.writer.MySQLWriter;
import cn.deepclue.datamaster.streamer.io.writer.Writer;

/**
 * Created by xuzb on 08/04/2017.
 */
public class Kafka2MySQLRWStreamer extends RWStreamer {
    public Kafka2MySQLRWStreamer(KTopicConfig source, MySQLTableConfig sink) {
        super(source, sink);
    }

    public Kafka2MySQLRWStreamer() {
        // Do not add source and sink automatically.
    }

    @Override
    protected Reader newReader(Source source) {
        KafkaReader reader = new KafkaReader((KTopicConfig) source);
        reader.setGroupIdSuffix("to-mysql");

        return reader;
    }

    @Override
    protected Writer newWriter(Sink sink) {
        return new MySQLWriter((MySQLTableConfig) sink);
    }
}
