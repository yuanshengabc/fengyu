package cn.deepclue.datamaster.testio;

import cn.deepclue.datamaster.testio.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by lilei-mac on 2017/4/10.
 */
@SpringBootApplication
public class MainTest {

    @Autowired
    private KafkaRWTest kafkaRWTest;

    @Autowired
    private KafkaReaderTest kafkaReaderTest;

    @Autowired
    private KafkaWriterTest kafkaWriterTest;

    @Autowired
    private MySqlRWTest mySqlRWTest;

    @Autowired
    private ESWriterTest esWriterTest;

    @Autowired
    private HDFSWriterTest hdfsWriterTest;

    @Autowired
    private IOStreamSerial ioStreamSerial;

    @Autowired
    private MysqlReaderTest mysqlReaderTest;

    @Autowired
    private IOStreamerParallel ioStreamerTest;

    @Autowired
    private IOStreamKafka2ES ioStreamK2ESs;

    @Autowired
    private IOStreamKafka2HDFS ioStreamKafka2HDFS;

    public static void main(String[] args) {

        ApplicationContext ctx = SpringApplication.run(MainTest.class, args);
        MainTest main = ctx.getBean(MainTest.class);
        main.test(args);
    }

    public void test(String[] args) {

        MatrixPrint.printTestStart("IO test start:");

        if (null == args) {
            MatrixPrint.print("Main function without parameters!");
            return;
        }
        if ("all".equals(args[0])) {
            kafkaRWTest.testStart();
            String tableName = mySqlRWTest.testStart();
            esWriterTest.testStart();
            hdfsWriterTest.testStart();
            ioStreamSerial.testStart(tableName);
        } else if ("ESWriter".equals(args[0])) {
            esWriterTest.testStart();
        } else if ("HDFSWriter".equals(args[0])) {
            hdfsWriterTest.testStart();
        } else if ("IOStreamer".equals(args[0])) {
            ioStreamerTest.testStart("person");
        } else if ("IOStreamerKafka2ES".equals(args[0])) {
            ioStreamK2ESs.testStart("person");
        } else if ("IOStreamerKafka2HDFS".equals(args[0])) {
            ioStreamKafka2HDFS.testStart("person");
        } else if ("IOStreamerSerial".equals(args[0])) {
            ioStreamSerial.testStart("person");
        } else if ("KafkaRW".equals(args[0])) {
            kafkaRWTest.testStart();
        } else if ("KafkaReader".equalsIgnoreCase(args[0])) {
            kafkaReaderTest.testStart();
        } else if ("KafkaWriter".equalsIgnoreCase(args[0])) {
            kafkaWriterTest.testStart();
        } else if ("MysqlReader".equals(args[0])) {
            mysqlReaderTest.testStart();
        } else if ("MysqlRW".equals(args[0])) {
            mySqlRWTest.testStart();
        } else {
            MatrixPrint.print("The parameter of main error!");
            return;
        }

        MatrixPrint.printTestEnd();
    }
}
