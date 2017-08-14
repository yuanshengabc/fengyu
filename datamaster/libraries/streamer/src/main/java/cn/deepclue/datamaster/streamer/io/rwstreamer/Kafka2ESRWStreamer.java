package cn.deepclue.datamaster.streamer.io.rwstreamer;

import cn.deepclue.datamaster.streamer.config.ESTypeConfig;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.io.reader.KafkaReader;
import cn.deepclue.datamaster.streamer.io.reader.Reader;
import cn.deepclue.datamaster.streamer.io.writer.ESWriter;
import cn.deepclue.datamaster.streamer.io.writer.Writer;

/**
 * Created by xuzb on 07/04/2017.
 */
public class Kafka2ESRWStreamer extends RWStreamer {
    public Kafka2ESRWStreamer(KTopicConfig source, ESTypeConfig sink) {
        super(source, sink);
    }

    public Kafka2ESRWStreamer() {
        // Do not add source and sink automatically.
    }

    @Override
    protected Reader newReader(Source source) {
        KafkaReader reader = new KafkaReader((KTopicConfig) source);
        reader.setGroupIdSuffix("to-es");

        return reader;
    }

    @Override
    protected Writer newWriter(Sink sink) {
        return new ESWriter((ESTypeConfig) sink);
    }
}
