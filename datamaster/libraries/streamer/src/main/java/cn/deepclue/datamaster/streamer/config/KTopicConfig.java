package cn.deepclue.datamaster.streamer.config;

/**
 * Created by xuzb on 07/04/2017.
 */
public class KTopicConfig implements Config {
    private String topic;
    private int partitions = 8;
    private int replicationFactor = 1;
    private KafkaConfig kconfig;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public KafkaConfig getKconfig() {
        return kconfig;
    }

    public void setKconfig(KafkaConfig kconfig) {
        this.kconfig = kconfig;
    }

    public String getZkServer() {
        return kconfig.getZkConfig().getZkUrl();
    }

    public String getSchemaRegServerString() {
        return kconfig.getSchemaRegServerString();
    }

    public int getPartitions() {
        return partitions;
    }

    public void setPartitions(int partitions) {
        this.partitions = partitions;
    }

    public int getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(int replicationFactor) {
        this.replicationFactor = replicationFactor;
    }
}
