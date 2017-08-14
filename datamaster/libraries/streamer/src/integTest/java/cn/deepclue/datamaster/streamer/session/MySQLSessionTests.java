package cn.deepclue.datamaster.streamer.session;

import cn.deepclue.datamaster.model.schema.RSSchema;
import cn.deepclue.datamaster.streamer.StreamerIntegTestProperties;
import cn.deepclue.datamaster.streamer.config.MySQLConfig;
import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by luoyong on 17-4-15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(value = {"classpath:sql/schema.sql","classpath:sql/data.sql"})
public class MySQLSessionTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private StreamerIntegTestProperties properties;

    private MySQLSession session;

    @Before
    public void setup() {
        MySQLConfig mySQLConfig = properties.getMysqlConfig();
        MySQLTableConfig tableConfig = new MySQLTableConfig();
        tableConfig.setMysqlConfig(mySQLConfig);
        tableConfig.setTableName("mysql_reader_writer_test");
        session = new MySQLSession(tableConfig);
        session.connect();
    }

    @After
    @Transactional
    public void cleanup() {
        session.execute("drop table if exists mysql_reader_writer_test");
        session.close();
    }

    @Test
    public void readTableSchema() {
        RSSchema schema = session.readTableSchema();
        assertThat(schema.getFields().size() > 0);
    }

    @Test
    public void getDataTableCount() {
        assertThat(session.getDataTableCount(properties.getMysqlConfig().getDatabase()) >0);
    }


    @Test
    public void getDataTables() {
        assertThat(session.getDataTables(properties.getMysqlConfig().getDatabase()).size() >0);
    }

    @Test
    public void getTableRows() {
        assertThat(session.getTableRows() == 2);
    }

    @Test
    public void canConnect() {
        MySQLTableConfig config = new MySQLTableConfig();
        MySQLConfig mysqlConfig = new MySQLConfig();
        String ip = "172.24.8.115";
        mysqlConfig.setIp(ip);
        mysqlConfig.setPort(3306);
        mysqlConfig.setUsername("datamaster");
        mysqlConfig.setPassword("datA123!@#");
        mysqlConfig.setDatabase("datamaster");
        config.setMysqlConfig(mysqlConfig);
        MySQLSession session = new MySQLSession(config);
        boolean ret = session.canConnect();
        assertThat(ret).isTrue();
    }
}
