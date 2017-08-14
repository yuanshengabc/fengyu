package cn.deepclue.datamaster.streamer.io.reader;

import cn.deepclue.datamaster.streamer.StreamerIntegTestProperties;
import cn.deepclue.datamaster.streamer.io.reader.MySQLReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by luoyong on 17-4-15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MySQLReaderTests {

    @Autowired
    private StreamerIntegTestProperties properties;

    private MySQLReader reader;
    @Before
    public void setUp() {
//        MySQLConfig mySQLConfig = properties.getMysqlConfig();
//        MySQLTableConfig tableConfig = new MySQLTableConfig();
//        tableConfig.setMysqlConfig(mySQLConfig);
//        tableConfig.setTableName("date_test");
//        reader = new MySQLReader(tableConfig);
//        reader.open();
    }

    @Test
    public void read() {
//        reader.readSchema();
//        while (reader.hasNext()) {
//            Record record = reader.readRecord();
//            System.out.println(record.getValues().size());
//        }
    }
}
