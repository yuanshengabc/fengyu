package cn.deepclue.datamaster.testio.impl;

import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.io.reader.MySQLReader;
import cn.deepclue.datamaster.streamer.io.writer.MySQLWriter;
import cn.deepclue.datamaster.testio.MatrixPrint;
import cn.deepclue.datamaster.testio.base.ReadAndWriteBase;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lilei-mac on 2017/4/10.
 */
@Configuration
public class MySqlRWTest extends ReadAndWriteBase {

    public MySqlRWTest(){}

    public String testStart(){

        String tableName=MatrixPrint.getMysqlTableName("test_mysql_",ioProperties.getIoBean().getRecordNumber(),ioProperties.getIoBean().getRecordColumn());

        MySQLTableConfig config=new MySQLTableConfig();
        config.setTableName(tableName);
        config.setMysqlConfig(ioProperties.getMysqlImport());

        MySQLWriter writer=new MySQLWriter(config);
        MatrixPrint.printTestStart("Mysql Writer:");
        MatrixPrint.print("    Mysql tableName:  "+tableName);
        testWriter(writer);

        MySQLReader reader=new MySQLReader(config);
        MatrixPrint.printTestStart("Mysql Reader:");
        MatrixPrint.print("    Mysql tableName:  "+tableName);
        testReader(reader);

        return tableName;
    }
}