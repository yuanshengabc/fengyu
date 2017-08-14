package cn.deepclue.datamaster.streamer.config.kafka.source;

import java.util.List;

public class KafkaSource implements Source {
    private String topics = "";
    private String bootstrapServers;

    /**
     * Define an instance of KafkaSource.
     * @param topics           The list of topics which will be exported.
     * @param bootstrapServers The servers of kafka.
     */
    public KafkaSource(List<String> topics, String bootstrapServers) {
        setTopics(topics);
        setBootstrapServers(bootstrapServers);
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        StringBuilder topicList = new StringBuilder();

        for (String str : topics) {
            topicList.append(str).append(',');
        }

        this.topics = "topics=" + topicList.substring(0, topicList.length() - 1);
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }
}
