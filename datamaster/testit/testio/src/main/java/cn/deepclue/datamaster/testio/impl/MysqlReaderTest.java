package cn.deepclue.datamaster.testio.impl;

import cn.deepclue.datamaster.streamer.config.MySQLTableConfig;
import cn.deepclue.datamaster.streamer.io.reader.MySQLReader;
import cn.deepclue.datamaster.testio.MatrixPrint;
import cn.deepclue.datamaster.testio.base.ReadAndWriteBase;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lilei-mac on 2017/4/19.
 */
@Configuration
public class MysqlReaderTest extends ReadAndWriteBase {

    public void testStart(){

        MySQLTableConfig config=new MySQLTableConfig();
        config.setTableName(ioProperties.getIoBean().getImportTableName());
        config.setMysqlConfig(ioProperties.getMysqlImport());

        MySQLReader reader=new MySQLReader(config);
        MatrixPrint.printTestStart("Mysql Reader:");
        MatrixPrint.print("    Mysql tableName:  "+ioProperties.getIoBean().getImportTableName());
        testReader(reader);
    }
}
