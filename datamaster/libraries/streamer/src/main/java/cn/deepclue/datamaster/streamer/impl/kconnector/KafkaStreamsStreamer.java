package cn.deepclue.datamaster.streamer.impl.kconnector;

import cn.deepclue.datamaster.streamer.Streamer;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;

public class KafkaStreamsStreamer implements Streamer {

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public void addSource(Source source) {

    }

    @Override
    public void addSink(Sink sink) {

    }

    @Override
    public void registerTransform(Transformer transformer) {

    }
}
