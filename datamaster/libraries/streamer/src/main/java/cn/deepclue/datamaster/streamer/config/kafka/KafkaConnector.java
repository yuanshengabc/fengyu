package cn.deepclue.datamaster.streamer.config.kafka;

public abstract class KafkaConnector {
    private String name = "";
    private String connectorClass = "";
    private String connectionUrl = "";
    private String tasksMax = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = "name=" + name;
    }

    public String getConnectorClass() {
        return connectorClass;
    }

    public void setConnectorClass(String connectorClass) {
        this.connectorClass = "connector.class=" + connectorClass;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = "connection.url=" + connectionUrl;
    }

    public String getTasksMax() {
        return tasksMax;
    }

    public void setTasksMax(String tasksMax) {
        this.tasksMax = "tasks.max=" + tasksMax;
    }

    protected String toCommonProperty() {
        return this.name + "\n" + this.connectorClass + "\n" + this.connectionUrl + "\n" + this.tasksMax;
    }

    public abstract String toPropertyString();
}
