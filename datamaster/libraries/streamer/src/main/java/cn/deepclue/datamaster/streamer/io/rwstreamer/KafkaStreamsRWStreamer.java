package cn.deepclue.datamaster.streamer.io.rwstreamer;

import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.io.KafkaHelper;
import cn.deepclue.datamaster.streamer.io.reader.KafkaReader;
import cn.deepclue.datamaster.streamer.io.reader.Reader;
import cn.deepclue.datamaster.streamer.io.writer.KafkaWriter;
import cn.deepclue.datamaster.streamer.io.writer.Writer;

/**
 * Created by xuzb on 30/03/2017.
 */
public class KafkaStreamsRWStreamer extends RWStreamer {
    private KTopicConfig sink;

    public KafkaStreamsRWStreamer(KTopicConfig source, KTopicConfig sink) {
        super(source, sink);
    }

    public KafkaStreamsRWStreamer() {
        // Do not add source and sink automatically.
    }


    @Override
    protected Reader newReader(Source source) {
        KafkaReader kafkaReader = new KafkaReader((KTopicConfig) source);
        kafkaReader.setGroupIdSuffix("-kafka-streams");
        return kafkaReader;
    }

    @Override
    protected Writer newWriter(Sink sink) {
        this.sink = (KTopicConfig) sink;
        return new KafkaWriter((KTopicConfig) sink);
    }
}
