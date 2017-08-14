package cn.deepclue.datamaster.testio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lilei-mac on 2017/4/13.
 */
public class MatrixPrint {
    private static Logger logger = LoggerFactory.getLogger(MatrixPrint.class);

    public static final String KAFKA_TOPIC_HEAD = "kafkaRWTest";

    public static final String ES_INDEX_HEAD = "index_";
    public static final String ES_TYPE_HEAD = "type_";

    public static final String KAFKA_IO_TOPIC_HEAD = "k";
    public static final String KAFKA_IO_K2K_TOPIC_HEAD = "k2k";
    public static final String ES_IO_INDEX_HEAD = "io_index_";
    public static final String ES_IO_TYPE_HEAD = "io_type_";

    public static final String HDFS_IO_FILEPATH_HEAD = "/io/file_";

    public static final String YML = "yml";

    public static void printTestStart(String name) {

        logger.info("");
        logger.info(name);
    }

    public static void print(String str) {

        logger.info(str);
    }

    public static void printDetails(int recordNum, int recordCol) {

        logger.info("    record number:  " + recordNum + "  ,record column:  " + recordCol);
    }

    public static void printResultTime(long startTime) {

        logger.info("    time consuming:  " + String.valueOf(getResultTime(startTime)) + "  ms");
    }

    public static void printReslut(String name, long num, long startTime) {

        logger.info("    " + name + "  result:  " + div(num, getResultTime(startTime)) * 1000 + "  times per second.");
    }

    public static void printRealRecordNum(long num) {

        logger.info("    real record number:  " + num);
    }

    public static long getCurrentTimeLong() {

        return System.currentTimeMillis();
    }

    public static String getKafkaTopic(String topicStr) {

        return topicStr + getRandomStr();
    }

    public static String getMysqlTableName(String head, int recordNum, int recordCol) {

        return head + recordNum + "_" + recordCol + "_" + getRandomStr();
    }

    public static String getESIndex(String indexStr) {

        return indexStr + getRandomStr();
    }

    public static String getESType(String typeStr) {

        return typeStr + getRandomStr();
    }

    public static String getFilePath(String filePathStr) {
        return filePathStr + getRandomStr();
    }

    public static void printTestEnd() {

        logger.info("");
        logger.info("IO test end.");
    }

    private static String getRandomStr() {

        return String.valueOf(new Date().getTime());
    }

    private static long getResultTime(long startTime) {

        return System.currentTimeMillis() - startTime;
    }

    private static double div(long v1, long v2) {

        BigDecimal b1 = new BigDecimal(Long.toString(v1));
        BigDecimal b2 = new BigDecimal(Long.toString(v2));
        return b1.divide(b2, 3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}