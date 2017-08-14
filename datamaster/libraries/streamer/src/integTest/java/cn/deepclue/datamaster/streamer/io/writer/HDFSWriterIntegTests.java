package cn.deepclue.datamaster.streamer.io.writer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.StreamerIntegTestProperties;
import cn.deepclue.datamaster.streamer.config.HDFSFileConfig;
import cn.deepclue.datamaster.streamer.io.HDFSClient;
import cn.deepclue.datamaster.streamer.io.reader.HDFSReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HDFSWriterIntegTests {
    @Autowired
    private StreamerIntegTestProperties properties;

    private final String filePath = "/cleaning/hdfswriter_integtests";

    private HDFSWriter hdfsWriter;
    private HDFSReader hdfsReader;
    private HDFSClient hdfsClient;

    private RSSchema rsSchema;
    private Record record1;
    private Record record2;

    private void loadStandalone() {
        HDFSFileConfig hdfsFileConfig = new HDFSFileConfig();
        hdfsFileConfig.setHdfsConfig(properties.getStandaloneHDFSConfig());
        hdfsFileConfig.setFilePath(filePath);
        hdfsWriter = new HDFSWriter(hdfsFileConfig);
        hdfsReader = new HDFSReader(hdfsFileConfig);
        hdfsClient = new HDFSClient(properties.getStandaloneHDFSConfig());
    }

    private void loadCluster() {
        HDFSFileConfig hdfsFileConfig = new HDFSFileConfig();
        hdfsFileConfig.setHdfsConfig(properties.getClusterHDFSConfig());
        hdfsFileConfig.setFilePath(filePath);
        hdfsWriter = new HDFSWriter(hdfsFileConfig);
        hdfsReader = new HDFSReader(hdfsFileConfig);
        hdfsClient = new HDFSClient(properties.getClusterHDFSConfig());
    }

    private void init() {
        rsSchema = new RSSchema();
        rsSchema.setName("test");
        rsSchema.addField(new RSField("int_field", BaseType.INT));
        rsSchema.addField(new RSField("long_field", BaseType.LONG));
        rsSchema.addField(new RSField("float_field", BaseType.FLOAT));
        rsSchema.addField(new RSField("double_field", BaseType.DOUBLE));
        rsSchema.addField(new RSField("text_field", BaseType.TEXT));
        rsSchema.addField(new RSField("date_field", BaseType.DATE));

        record1 = new Record(rsSchema);
        record1.addValue(1);
        record1.addValue(2L);
        record1.addValue(0.26f);
        record1.addValue(Math.random());
        record1.addValue("test text");
        record1.addValue(new Date());

        record2 = new Record(rsSchema);
        record2.addValue(2);
        record2.addValue(50L);
        record2.addValue(4.26f);
        record2.addValue(Math.random());
        record2.addValue("test text");
        record2.addValue(new Date());
    }

    private void verify() {
        try {
            hdfsWriter.open();

            hdfsWriter.writeSchema(rsSchema);
            hdfsWriter.writeRecord(record1);
            hdfsWriter.writeRecord(record2);
        } finally {
            hdfsWriter.close();
        }

        try {
            hdfsReader.open();
            assertThat(hdfsReader.readSchema().count()).isEqualTo(rsSchema.count());
            assertThat(hdfsReader.hasNext()).isTrue();
            assertThat(hdfsReader.readRecord().size()).isEqualTo(record1.size());
            assertThat(hdfsReader.hasNext()).isTrue();
            assertThat(hdfsReader.readRecord().size()).isEqualTo(record2.size());
            assertThat(hdfsReader.hasNext()).isFalse();
        } finally {
            hdfsReader.close();
        }
    }

    private void tearDown() {
        try {
            hdfsClient.open();
            hdfsClient.deleteFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            hdfsClient.close();
        }
    }

    @Test
    public void testStandalone() {
        loadStandalone();
        init();
        verify();
        tearDown();
    }

    @Test
    public void testCluster() {
        loadCluster();
        init();
        verify();
        tearDown();
    }
}
