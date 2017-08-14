package cn.deepclue.datamaster.streamer;

import cn.deepclue.datamaster.streamer.config.ESConfig;
import cn.deepclue.datamaster.streamer.config.KafkaConfig;

/**
 * Created by ggchangan on 17-7-19.
 */
public class Env {
    public static StreamerIntegTestProperties properties;


    public static KafkaConfig kafkaStandAloneConfig() {
        return properties.getStandaloneKafkaConfig();
    }

    public static KafkaConfig kafkaClusterConfig() {
        return properties.getClusterKafkaConfig();
    }

    public static ESConfig esConfig() {
        return properties.getEsconfig();
    }

    //TODO add more
}
