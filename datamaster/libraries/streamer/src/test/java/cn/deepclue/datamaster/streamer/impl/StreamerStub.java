package cn.deepclue.datamaster.streamer.impl;

import cn.deepclue.datamaster.streamer.Streamer;
import cn.deepclue.datamaster.streamer.config.kafka.sink.Sink;
import cn.deepclue.datamaster.streamer.config.kafka.source.Source;
import cn.deepclue.datamaster.streamer.transform.transformer.Transformer;

/**
 * Created by xuzb on 09/03/2017.
 */
public class StreamerStub implements Streamer {
    @Override
    public boolean start() {
        try {
            // We just sleep 10s to simulate transport process
            Thread.sleep(10000);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

    }

    @Override
    public void addSource(Source source) {
        // Leave empty for stub impl
    }

    @Override
    public void addSink(Sink sink) {
        // Leave empty for stub impl
    }

    @Override
    public void registerTransform(Transformer transformer) {
        // Leave empty for stub impl
    }
}
