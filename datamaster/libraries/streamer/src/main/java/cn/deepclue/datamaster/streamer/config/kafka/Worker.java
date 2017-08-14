package cn.deepclue.datamaster.streamer.config.kafka;

public class Worker {
    //Bootstrap Kafka servers
    private String bootstrapServers;

    //Converter for key and value
    private String keyConverter;
    private String keyConverterSchemaRegistryUrl;
    private String valueConverter;
    private String valueConverterSchemaRegistryUrl;

    //Internal converter
    private String internalKeyConverter;
    private String internalValueConverter;
    private String internalKeyConverterSchemasEnable;
    private String internalValueConverterSchemasEnable;

    //Local storage file for offset data
    private String offsetStorageFileFilename;

    /**
     * Define an instance of the worker.
     * @param bootstrapServers The servers of the kafka.
     */
    public Worker(String bootstrapServers) {
        setBootstrapServers(bootstrapServers);

        setKeyConverter("io.confluent.connect.avro.AvroConverter");
        setKeyConverterSchemaRegistryUrl("http://localhost:8081");
        setValueConverter("io.confluent.connect.avro.AvroConverter");
        setValueConverterSchemaRegistryUrl("http://localhost:8081");

        setInternalKeyConverter("org.apache.kafka.connect.json.JsonConverter");
        setInternalValueConverter("org.apache.kafka.connect.json.JsonConverter");
        setInternalKeyConverterSchemasEnable("false");
        setInternalValueConverterSchemasEnable("false");

        setOffsetStorageFileFilename("/tmp/connect.offsets");
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = "bootstrap.servers=" + bootstrapServers;
    }

    public void setKeyConverter(String keyConverter) {
        this.keyConverter = "key.converter=" + keyConverter;
    }

    public void setKeyConverterSchemaRegistryUrl(String keyConverterSchemaRegistryUrl) {
        this.keyConverterSchemaRegistryUrl = "key.converter.schema.registry.url=" + keyConverterSchemaRegistryUrl;
    }

    public void setValueConverter(String valueConverter) {
        this.valueConverter = "value.converter=" + valueConverter;
    }

    public void setValueConverterSchemaRegistryUrl(String valueConverterSchemaRegistryUrl) {
        this.valueConverterSchemaRegistryUrl = "value.converter.schema.registry.url=" + valueConverterSchemaRegistryUrl;
    }

    public void setInternalKeyConverter(String internalKeyConverter) {
        this.internalKeyConverter = "internal.key.converter=" + internalKeyConverter;
    }

    public void setInternalValueConverter(String internalValueConverter) {
        this.internalValueConverter = "internal.value.converter=" + internalValueConverter;
    }

    public void setInternalKeyConverterSchemasEnable(String internalKeyConverterSchemasEnable) {
        this.internalKeyConverterSchemasEnable = "internal.key.converter.schemas.enable=" + internalKeyConverterSchemasEnable;
    }

    public void setInternalValueConverterSchemasEnable(String internalValueConverterSchemasEnable) {
        this.internalValueConverterSchemasEnable = "internal.value.converter.schemas.enable=" + internalValueConverterSchemasEnable;
    }

    public void setOffsetStorageFileFilename(String offsetStorageFileFilename) {
        this.offsetStorageFileFilename = "offset.storage.file.filename=" + offsetStorageFileFilename;
    }

    /**
     * Get the content of worker properties.
     * @return The content of all properties.
     */
    public String toWorkerString() {
        return this.bootstrapServers + "\n"
                + this.keyConverter + "\n" + this.keyConverterSchemaRegistryUrl + "\n" + this.valueConverter + "\n" + this.valueConverterSchemaRegistryUrl + "\n"
                + this.internalKeyConverter + "\n" + this.internalValueConverter + "\n" + this.internalKeyConverterSchemasEnable + "\n" + this.internalValueConverterSchemasEnable + "\n"
                + this.offsetStorageFileFilename + "\n";
    }
}
