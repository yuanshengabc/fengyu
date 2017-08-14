package cn.deepclue.datamaster.streamer.config.kafka.sink;

import cn.deepclue.datamaster.streamer.config.kafka.source.MySQLConnectorSource;

import java.util.ArrayList;
import java.util.List;

public class KafkaSink implements Sink {
    private String topicPrefix = "";
    private String bootstrapServers;

    /**
     * Define an instance of KafkaSink.
     * @param topicPrefix      The prefix of topic.
     * @param bootstrapServers The servers of kafka.
     */
    public KafkaSink(String topicPrefix, String bootstrapServers) {
        setTopicPrefix(topicPrefix);//"a_"
        setBootstrapServers(bootstrapServers);
    }

    public String getTopicPrefix() {
        return topicPrefix;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = "topic.prefix=" + topicPrefix;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getTopicName(MySQLConnectorSource source) {
        if ("".equals(source.getTableWhitelist())) {
            return "";
        }

        String[] tables = source.getTableWhitelist().substring(source.getTableWhitelist().indexOf("=") + 1).split(",");

        List<String> list = new ArrayList<>();

        for (String str : tables) {
            list.add(this.topicPrefix.substring(this.topicPrefix.indexOf('=') + 1) + str);
        }

        return list.toString();
    }
}
