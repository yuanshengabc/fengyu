package cn.deepclue.datamaster.streamer.config.kafka.sink;

import cn.deepclue.datamaster.streamer.config.kafka.KafkaConnector;

import java.util.Map;

/**
 * ES Sink for Kafka Connector
 */

public class ESConnectorSink extends KafkaConnector implements Sink {
    private String batchSize = "";//Default: 2000
    private String maxBufferedRecords = "";//Default: 20000
    private String lingerMs = "";//Default: 1
    private String flushTimeoutMs = "";//Default: 10000
    private String maxInFlightRequests = "";//Default: 5
    private String maxRetries = "";//Default: 5
    private String retryBackoffMs = "";//Default: 100

    private String typeName = "";
    private String keyIgnore = "";//Default: false
    private String schemaIgnore = "";//Default: false
    private String topicIndexMap = "";//Default: “”
    private String topicKeyIgnore = "";//Default: “”
    private String topicSchemaIgnore = "";//Default: “”

    /**
     * Define an instance of ESConnectorSink.
     * @param name          The name of this sink connector.
     * @param connectionUrl The url of ES.
     */
    public ESConnectorSink(String name, String connectionUrl) {
        setName(name);
        setConnectionUrl(connectionUrl);//"http://localhost:9200"
        setConnectorClass("io.confluent.connect.elasticsearch.ElasticsearchSinkConnector");
        setTasksMax("1");

        setKeyIgnore("true");
        setTypeName("kafka-connect");
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = "batch.size=" + batchSize;
    }

    public void setMaxBufferedRecords(String maxBufferedRecords) {
        this.maxBufferedRecords = "max.buffered.records=" + maxBufferedRecords;
    }

    public void setLingerMs(String lingerMs) {
        this.lingerMs = "linger.ms=" + lingerMs;
    }

    public void setFlushTimeoutMs(String flushTimeoutMs) {
        this.flushTimeoutMs = "flush.timeout.ms=" + flushTimeoutMs;
    }

    public void setMaxInFlightRequests(String maxInFlightRequests) {
        this.maxInFlightRequests = "max.in.flight.requests=" + maxInFlightRequests;
    }

    public void setMaxRetries(String maxRetries) {
        this.maxRetries = "max.retries=" + maxRetries;
    }

    public void setRetryBackoffMs(String retryBackoffMs) {
        this.retryBackoffMs = "retry.backoff.ms=" + retryBackoffMs;
    }

    public void setTypeName(String typeName) {
        this.typeName = "type.name=" + typeName;
    }

    public void setKeyIgnore(String keyIgnore) {
        this.keyIgnore = "key.ignore=" + keyIgnore;
    }

    public void setSchemaIgnore(String schemaIgnore) {
        this.schemaIgnore = "schema.ignore=" + schemaIgnore;
    }

    public void setTopicIndexMap(Map<String, String> topicIndexMap) {
        StringBuilder str = new StringBuilder();
        str.append("topic.index.map=");

        for (Map.Entry entry : topicIndexMap.entrySet()) {
            str.append(entry.getKey()).append(':').append(entry.getValue()).append(',');
        }

        this.topicIndexMap = str.substring(0, str.length() - 1);
    }

    public void setTopicKeyIgnore(String topicKeyIgnore) {
        this.topicKeyIgnore = "topic.key.ignore=" + topicKeyIgnore;
    }

    public void setTopicSchemaIgnore(String topicSchemaIgnore) {
        this.topicSchemaIgnore = "topic.schema.ignore=" + topicSchemaIgnore;
    }

    /**
     * Get the content of properties.
     * @return The content of all properties.
     */
    @Override
    public String toPropertyString() {
        return toCommonProperty() + "\n"
                + this.batchSize + "\n" + this.maxBufferedRecords + "\n" + this.lingerMs + "\n"
                + this.flushTimeoutMs + "\n" + this.maxInFlightRequests + "\n" + this.maxRetries + "\n" + this.retryBackoffMs + "\n"
                + this.typeName + "\n" + this.keyIgnore + "\n" + this.schemaIgnore + "\n"
                + this.topicIndexMap + "\n" + this.topicKeyIgnore + "\n" + this.topicSchemaIgnore + "\n";
    }
}
