package cn.deepclue.datamaster.streamer.io.reader;

import cn.deepclue.datamaster.streamer.config.KTopicConfig;

/**
 * Created by ggchangan on 17-7-3.
 */
public class KafkaStartOverReader extends KafkaReader {
    public KafkaStartOverReader(KTopicConfig config) {
        super(config);
    }

    @Override
    protected void doSubscribe() {
        super.doSubscribe();
        consumer.poll(1000);
        consumer.seekToBeginning(consumer.assignment());
    }
}
