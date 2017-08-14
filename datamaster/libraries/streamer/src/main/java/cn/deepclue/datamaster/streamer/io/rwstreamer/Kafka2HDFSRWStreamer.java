package cn.deepclue.datamaster.streamer.io.rwstreamer;

import cn.deepclue.datamaster.streamer.config.HDFSFileConfig;
import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.io.reader.KafkaReader;
import cn.deepclue.datamaster.streamer.io.reader.Reader;
import cn.deepclue.datamaster.streamer.io.writer.HDFSWriter;
import cn.deepclue.datamaster.streamer.io.writer.Writer;

public class Kafka2HDFSRWStreamer extends RWStreamer {
    public Kafka2HDFSRWStreamer(KTopicConfig source, HDFSFileConfig sink) {
        super(source, sink);
    }

    public Kafka2HDFSRWStreamer() {
        // Do not add source and sink automatically.
    }

    @Override
    protected Reader newReader(Source source) {
        KafkaReader reader = new KafkaReader((KTopicConfig) source);
        reader.setGroupIdSuffix("to-hdfs");

        return reader;
    }

    @Override
    protected Writer newWriter(Sink sink) {
        return new HDFSWriter((HDFSFileConfig) sink);
    }
}
