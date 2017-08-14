package cn.deepclue.datamaster.streamer.config.kafka.source;

import cn.deepclue.datamaster.streamer.config.kafka.KafkaConnector;

import java.util.List;

/**
 * MySQL Source for Kafka Connector
 */
public class MySQLConnectorSource extends KafkaConnector implements Source {
    //Database
    private String tableWhitelist = "";//eg:users,products,transactions

    //Connection
    private String pollIntervalMs = "";//Default:5000
    private String batchMaxRows = "";//Default:100

    //Mode
    private String mode = "";
    private String incrementingColumnName = "";

    /**
     * Define a new instance of MySQLConnectorSource.
     * @param name                   The unique name of the Connector.
     * @param connectionUrl          The url of MySQL.
     * @param incrementingColumnName The column name to detect new rows.
     */
    public MySQLConnectorSource(String name, String connectionUrl, String incrementingColumnName) {
        setName(name);
        setConnectionUrl(connectionUrl);
        setConnectorClass("io.confluent.connect.jdbc.JdbcSourceConnector");
        setTasksMax("1");

        setMode("incrementing");
        setIncrementingColumnName(incrementingColumnName);//"id"
    }

    public void setPollIntervalMs(String pollIntervalMs) {
        this.pollIntervalMs = "poll.interval.ms=" + pollIntervalMs;
    }

    public void setBatchMaxRows(String batchMaxRows) {
        this.batchMaxRows = "batch.max.rows=" + batchMaxRows;
    }

    public void setMode(String mode) {
        this.mode = "mode=" + mode;
    }

    public void setIncrementingColumnName(String incrementingColumnName) {
        this.incrementingColumnName = "incrementing.column.name=" + incrementingColumnName;
    }

    public String getTableWhitelist() {
        return tableWhitelist;
    }

    /**
     * Set white list of the tables.
     * @param tableWhitelist The list of tables which will be exported only.
     */
    public void setTableWhitelist(List<String> tableWhitelist) {
        StringBuilder tableList = new StringBuilder();

        for (String str : tableWhitelist) {
            tableList.append(str).append(',');
        }

        this.tableWhitelist = "table.whitelist=" + tableList.toString().subSequence(0, tableList.length() - 1);
    }

    /**
     * Get the content of properties.
     * @return The content of all properties.
     */
    @Override
    public String toPropertyString() {
        return toCommonProperty() + "\n"
                + this.tableWhitelist + "\n"
                + this.pollIntervalMs + "\n" + this.batchMaxRows + "\n"
                + this.mode + "\n" + this.incrementingColumnName + "\n";
    }
}
