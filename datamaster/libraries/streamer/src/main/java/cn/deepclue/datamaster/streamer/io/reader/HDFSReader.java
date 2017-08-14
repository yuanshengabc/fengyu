package cn.deepclue.datamaster.streamer.io.reader;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.HDFSFileConfig;
import cn.deepclue.datamaster.streamer.exception.HdfsException;
import cn.deepclue.datamaster.streamer.io.HDFSClient;
import cn.deepclue.datamaster.streamer.io.RecordConverter;
import cn.deepclue.datamaster.streamer.io.SchemaConverter;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.hadoop.fs.AvroFSInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HDFSReader implements Reader {
    private static final Logger logger = LoggerFactory.getLogger(HDFSReader.class);

    private HDFSClient hdfsClient;
    private HDFSFileConfig hdfsFileConfig;
    private RSSchema rsSchema;
    private DataFileReader<GenericRecord> dataFileReader = null;

    public HDFSReader(HDFSFileConfig hdfsFileConfig) {
        this.hdfsFileConfig = hdfsFileConfig;
        hdfsClient = new HDFSClient(hdfsFileConfig.getHdfsConfig());
    }

    @Override
    public RSSchema readSchema() {
        try {
            DatumReader<GenericRecord> reader = new GenericDatumReader<>();

            AvroFSInput avroFSInput = hdfsClient.readAsInput(hdfsFileConfig.getFilePath());

            dataFileReader = new DataFileReader<>(avroFSInput, reader);

            Schema avroSchema = dataFileReader.getSchema();
            rsSchema = SchemaConverter.fromAvroSchema(avroSchema);
        } catch (IOException e) {
            logger.error("Failed to read schema.", e);
            throw new HdfsException("Failed to read schema.", "读取模式失败。", e);
        }
        return rsSchema;
    }

    @Override
    public boolean hasNext() {
        return dataFileReader.hasNext();
    }

    @Override
    public Record readRecord() {
        GenericRecord avroRecord = dataFileReader.next();
        return RecordConverter.fromAvroRecord(avroRecord, rsSchema);
    }

    @Override
    public void open() {
        try {
            hdfsClient.open();
        } catch (IOException e) {
            logger.error("Failed to open hdfs client.", e);
            throw new HdfsException("Failed to open hdfs client.", "打开hdfs客户端失败。", e);
        }
    }

    @Override
    public void close() {
        if (dataFileReader != null) {
            try {
                dataFileReader.close();
            } catch (IOException e) {
                logger.info("Failed to close data file reader {}", e);
            }
        }
        hdfsClient.close();
    }
}
