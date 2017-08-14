package cn.deepclue.datamaster.streamer.io.rwstreamer;

import cn.deepclue.datamaster.streamer.config.KTopicConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.io.KafkaHelper;
import cn.deepclue.datamaster.streamer.io.reader.MySQLReader;
import cn.deepclue.datamaster.streamer.io.reader.Reader;
import cn.deepclue.datamaster.streamer.io.writer.KafkaWriter;
import cn.deepclue.datamaster.streamer.io.writer.Writer;

/**
 * Created by xuzb on 29/03/2017.
 */
public class MySQL2KafkaRWStreamer extends RWStreamer {

    private MySQLTableConfig source;
    private KTopicConfig sink;

    public MySQL2KafkaRWStreamer(MySQLTableConfig source, KTopicConfig sink) {
        super(source, sink);
    }

    public MySQL2KafkaRWStreamer() {
        // Do not add source and sink automatically.
    }

    @Override
    protected Reader newReader(Source source) {
        this.source = (MySQLTableConfig) source;
        return new MySQLReader(this.source);
    }

    @Override
    protected Writer newWriter(Sink sink) {
        this.sink = (KTopicConfig) sink;
        return new KafkaWriter(this.sink);
    }
}
