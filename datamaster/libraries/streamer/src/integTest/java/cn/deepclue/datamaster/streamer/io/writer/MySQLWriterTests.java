package cn.deepclue.datamaster.streamer.io.writer;

import cn.deepclue.datamaster.model.Record;
import cn.deepclue.datamaster.model.schema.BaseType;
import cn.deepclue.datamaster.model.schema.RSField;
import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.StreamerIntegTestProperties;
import cn.deepclue.datamaster.streamer.config.MySQLConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * Created by luoyong on 17-4-21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MySQLWriterTests {

    @Autowired
    private StreamerIntegTestProperties properties;

    private MySQLWriter mySQLWriter;

    @Before
    public void setUp() {
        MySQLConfig mySQLConfig = properties.getMysqlConfig();
        MySQLTableConfig tableConfig = new MySQLTableConfig();
        tableConfig.setMysqlConfig(mySQLConfig);
        tableConfig.setTableName("mysql_writer_test_table");
        mySQLWriter = new MySQLWriter(tableConfig);
        mySQLWriter.open();
    }

    @Test
   // @Transactional
    public void writerSchemaAndRecord() {
        RSSchema schema = new RSSchema();
        schema.addField(new RSField("int_field", BaseType.INT));
        schema.addField(new RSField("long_field", BaseType.LONG));
        schema.addField(new RSField("float_field", BaseType.FLOAT));
        schema.addField(new RSField("double_field", BaseType.DOUBLE));
        schema.addField(new RSField("text_field", BaseType.TEXT));
        schema.addField(new RSField("date_field", BaseType.DATE));
        mySQLWriter.writeSchema(schema);

        Record record1 = new Record(schema);
        record1.addValue(1);
        record1.addValue(2L);
        record1.addValue(0.26f);
        record1.addValue(Math.random());
        record1.addValue("test text");
        record1.addValue(new Date());

        mySQLWriter.writeRecord(record1);


        Record record2 = new Record(schema);
        record2.addValue(2);
        record2.addValue(50L);
        record2.addValue(4.26f);
        record2.addValue(Math.random());
        record2.addValue("test text");
        record2.addValue(new Date());
        mySQLWriter.writeRecord(record2);

        mySQLWriter.close();

    }

    @Test
    // @Transactional
    public void writerSchema() {
        RSSchema schema = new RSSchema();
        schema.addField(new RSField("11", BaseType.INT));
        schema.addField(new RSField("11_", BaseType.LONG));
        schema.addField(new RSField("_11", BaseType.FLOAT));
        schema.addField(new RSField("11$_", BaseType.DOUBLE));
        schema.addField(new RSField("11bb", BaseType.TEXT));
        mySQLWriter.writeSchema(schema);

        mySQLWriter.close();

    }
}
