package cn.deepclue.datamaster.streamer.config.kafka.sink;

import cn.deepclue.datamaster.streamer.config.kafka.KafkaConnector;
import cn.deepclue.datamaster.streamer.config.kafka.source.KafkaSource;

import java.util.ArrayList;
import java.util.List;

/**
 * MySQL Sink for Kafka Connector
 */
public class MySQLConnectorSink extends KafkaConnector implements Sink {
    //Writes
    private String insertMode = "";//Default:insert
    private String batchSize = "";//Default:3000

    //DataMapping
    private String tableNameFormat = "";//May contain ‘${topic}’ as a placeholder for the originating topic name,eg kafka_${topic}.
    private String pkMode = "";
    private String pkFields = "";
    private String filedsWhitelist = "";

    //DDL Support
    private String autoCreate = "";//Default:false
    private String autoEvolve = "";//Default:false

    //Retries
    private String maxRetries = "";//Default:10
    private String retryBackoffMs = "";//Default:3000

    /**
     * Define an instance of MySQLConnectorSink
     * @param name            The name of this new sink connector.
     * @param connectionUrl   The url of MySQL.
     * @param tableNameFormat The name format of table.
     */
    public MySQLConnectorSink(String name, String connectionUrl, String tableNameFormat) {
        setName(name);
        setConnectionUrl(connectionUrl);
        setConnectorClass("io.confluent.connect.jdbc.JdbcSinkConnector");
        setTasksMax("1");

        setTableNameFormat(tableNameFormat);//"b_${topic}"
        setAutoCreate("true");
    }

    public void setInsertMode(String insertMode) {
        this.insertMode = "insert.mode=" + insertMode;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = "batch.size=" + batchSize;
    }

    public void setTableNameFormat(String tableNameFormat) {
        this.tableNameFormat = "table.name.format=" + tableNameFormat;
    }

    public void setPkMode(String pkMode) {
        this.pkMode = "pk.mode=" + pkMode;
    }

    public void setPkFields(String pkFields) {
        this.pkFields = "pk.fields=" + pkFields;
    }

    public void setFiledsWhitelist(String filedsWhitelist) {
        this.filedsWhitelist = "fileds.whitelist=" + filedsWhitelist;
    }

    public void setAutoCreate(String autoCreate) {
        this.autoCreate = "auto.create=" + autoCreate;
    }

    public void setAutoEvolve(String autoEvolve) {
        this.autoEvolve = "auto.evolve=" + autoEvolve;
    }

    public void setMaxRetries(String maxRetries) {
        this.maxRetries = "max.retries=" + maxRetries;
    }

    public void setRetryBackoffMs(String retryBackoffMs) {
        this.retryBackoffMs = "retry.backoff.ms=" + retryBackoffMs;
    }

    public String getTableName(KafkaSource source) {
        String[] topics = source.getTopics().substring(source.getTopics().indexOf('=') + 1).split(",");

        List<String> list = new ArrayList<>();

        for (String str : topics) {
            list.add(tableNameFormat.substring(tableNameFormat.indexOf('=') + 1).replace("${topic}", str));
        }

        return list.toString();
    }

    /**
     * Get the content of the connector properties.
     * @return The content of all properties.
     */
    @Override
    public String toPropertyString() {
        return toCommonProperty() + "\n"
                + this.insertMode + "\n" + this.batchSize + "\n"
                + this.tableNameFormat + "\n" + this.pkMode + "\n" + this.pkFields + "\n" + this.filedsWhitelist + "\n"
                + this.autoCreate + "\n" + this.autoEvolve + "\n"
                + this.maxRetries + "\n" + this.retryBackoffMs + "\n";
    }
}
