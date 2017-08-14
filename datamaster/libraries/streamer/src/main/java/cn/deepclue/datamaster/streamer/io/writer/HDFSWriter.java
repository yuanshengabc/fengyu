package cn.deepclue.datamaster.streamer.io.writer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.config.HDFSFileConfig;
import cn.deepclue.datamaster.streamer.exception.HdfsException;
import cn.deepclue.datamaster.streamer.io.HDFSClient;
import cn.deepclue.datamaster.streamer.io.RecordConverter;
import cn.deepclue.datamaster.streamer.io.SchemaConverter;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HDFSWriter implements Writer {
    private static final Logger logger = LoggerFactory.getLogger(HDFSWriter.class);

    private HDFSClient hdfsClient;
    private HDFSFileConfig hdfsFileConfig;
    private RSSchema rsSchema;
    private Schema avroSchema;
    private DataFileWriter<GenericRecord> dataFileWriter = null;

    public HDFSWriter(HDFSFileConfig hdfsFileConfig) {
        this.hdfsFileConfig = hdfsFileConfig;
        hdfsClient = new HDFSClient(hdfsFileConfig.getHdfsConfig());
    }

    @Override
    public void writeSchema(RSSchema rsSchema) {
        try {
            this.rsSchema = rsSchema;
            this.avroSchema = SchemaConverter.toAvroSchemaByBuilder(rsSchema);

            DatumWriter<GenericRecord> writer = new GenericDatumWriter<>(this.avroSchema);
            dataFileWriter = new DataFileWriter<>(writer);

            FSDataOutputStream outputStream = hdfsClient.writeAsStream(hdfsFileConfig.getFilePath());
            dataFileWriter.create(this.avroSchema, outputStream);
        } catch (IOException e) {
            logger.error("Failed to write schema.", e);
            throw new HdfsException("Failed to write schema.", "写入模式失败。", e);
        }
    }

    @Override
    public void writeRecord(Record record) {
        try {
            GenericRecord avroRecord = RecordConverter.toAvroRecord(record, rsSchema, avroSchema);
            dataFileWriter.append(avroRecord);
        } catch (IOException e) {
            logger.error("Failed to append record.", e);
            throw new HdfsException("Failed to append record.", "追加记录失败。", e);
        }
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
        if (dataFileWriter != null) {
            try {
                dataFileWriter.close();
            } catch (IOException e) {
                logger.info("Failed to close data file writer {}", e);
            }
        }
        hdfsClient.close();
    }
}
