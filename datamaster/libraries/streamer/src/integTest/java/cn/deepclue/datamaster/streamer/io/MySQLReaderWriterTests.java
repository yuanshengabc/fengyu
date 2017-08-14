package cn.deepclue.datamaster.streamer.io;

import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.StreamerIntegTestProperties;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.io.reader.MySQLReader;
import cn.deepclue.datamaster.streamer.io.writer.MySQLWriter;
import cn.deepclue.datamaster.streamer.session.MySQLSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by luoyong on 17-4-6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(value = {"classpath:sql/schema.sql","classpath:sql/data.sql"})
public class MySQLReaderWriterTests {

    @Autowired
    private StreamerIntegTestProperties properties;

    private MySQLReader reader;
    private MySQLWriter writer;
    private MySQLSession writerSession;
    private MySQLSession readerSession;

    @Before
    public void setUp() {
        MySQLTableConfig sourceConfig = new MySQLTableConfig();
        sourceConfig.setMysqlConfig(properties.getMysqlConfig());
        sourceConfig.setTableName("mysql_reader_writer_test");
        reader = new MySQLReader(sourceConfig);
        readerSession = new MySQLSession(sourceConfig);

        MySQLTableConfig targetConfig = new MySQLTableConfig();
        targetConfig.setMysqlConfig(properties.getMysqlConfig());
        targetConfig.setTableName("mysql_reader_writer_test_1");
        writer = new MySQLWriter(targetConfig);
        writerSession = new MySQLSession(targetConfig);

        readerSession.connect();
        writerSession.connect();
        reader.open();
        writer.open();
    }

    @After
    public void afterTest() {
        writerSession.execute("drop table if exists mysql_reader_writer_test_1");
        writerSession.execute("drop table if exists mysql_reader_writer_test");
        reader.close();
        readerSession.close();
        writerSession.close();
    }

    @Test
    public void mysqlReaderTest() throws SQLException, InterruptedException {
        RSSchema schema = reader.readSchema();
        writer.writeSchema(schema);
        while (reader.hasNext()) {
            writer.writeRecord(reader.readRecord());
        }
        writer.close();

        ResultSet writerRs = writerSession.select(schema.getFields());
        ResultSet readerRs = readerSession.select(schema.getFields());
        while (readerRs.next()) {
            writerRs.next();
            assertThat(Objects.equals(readerRs.getInt("uid"), writerRs.getInt("uid")));
            assertThat(Objects.equals(readerRs.getDate("created_on"), writerRs.getDate("created_on")));
            assertThat(Objects.equals(readerRs.getString("username"), writerRs.getString("username")));
            assertThat(Objects.equals(readerRs.getString("ip"), writerRs.getString("ip")));
            assertThat(Objects.equals(readerRs.getString("password"), writerRs.getString("password")));
        }
        writerRs.close();
        readerRs.close();
    }


}
