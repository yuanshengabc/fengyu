package cn.deepclue.datamaster.testio.impl;

import cn.deepclue.datamaster.streamer.config.HDFSConfig;
import cn.deepclue.datamaster.streamer.config.HDFSFileConfig;
import cn.deepclue.datamaster.streamer.io.writer.HDFSWriter;
import cn.deepclue.datamaster.testio.MatrixPrint;
import cn.deepclue.datamaster.testio.base.ReadAndWriteBase;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HDFSWriterTest extends ReadAndWriteBase {

    private final String filePath;

    public HDFSWriterTest() {
        filePath = MatrixPrint.getESIndex(MatrixPrint.getFilePath(MatrixPrint.HDFS_IO_FILEPATH_HEAD));
    }

    public void testStart() {
        HDFSConfig hdfsConfig = ioProperties.getHdfsConfig();
        HDFSFileConfig hdfsFileConfig = new HDFSFileConfig();
        hdfsFileConfig.setHdfsConfig(hdfsConfig);
        hdfsFileConfig.setFilePath(filePath);

        HDFSWriter hdfsWriter = new HDFSWriter(hdfsFileConfig);

        MatrixPrint.printTestStart("HDFSWriter:");
        MatrixPrint.print("HDFS file path:  " + filePath);
        testWriter(hdfsWriter);
    }
}
