package cn.deepclue.datamaster.streamer.io;

import cn.deepclue.datamaster.streamer.config.HDFSConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Created by xuzb on 23/05/2017.
 */
public class HDFSClient {

    private static final Logger logger = LoggerFactory.getLogger(HDFSClient.class);

    private FileSystem fileSystem = null;
    private Configuration conf;
    private boolean opened;

    public HDFSClient(HDFSConfig config) {
        conf = new Configuration();
        conf.set("fs.defaultFS", config.getServerString());

        // FIXME: Hadoop user name is a global option.
        System.setProperty("HADOOP_USER_NAME", config.getUsername());

        opened = false;
    }

    public boolean open() throws IOException {
        if (opened) {
            return false;
        }

        fileSystem = FileSystem.get(conf);

        opened = true;

        return true;
    }

    public FSDataInputStream readAsStream(String filePath) throws IOException {
        Path path = new Path(filePath);
        if (!fileSystem.exists(path)) {
            throw new IOException("File not exists: " + filePath);
        }

        return fileSystem.open(path);
    }

    public AvroFSInput readAsInput(String filePath) throws IOException {
        Path path = new Path(filePath);
        if (!fileSystem.exists(path)) {
            throw new IOException("File not exists: " + filePath);
        }

        return new AvroFSInput(fileSystem.open(path), fileSystem.getFileStatus(path).getLen());
    }

    public FSDataOutputStream writeAsStream(String filePath) throws IOException {
        Path path = new Path(filePath);
        if (fileSystem.exists(path)) {
            logger.info("File " + filePath + " already exists and will be override.");
        }
        return fileSystem.create(path);
    }

    public void write(String filePath, String content) throws IOException {
        Path path = new Path(filePath);
        if (fileSystem.exists(path)) {
            logger.info("File " + filePath + " already exists and will be override.");
        }

        // Create a new file and write data to it.
        FSDataOutputStream out = null;
        try {
            fileSystem.mkdirs(path.getParent());
            out = fileSystem.create(path);
            out.write(content.getBytes());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public boolean deleteDir(String dirpath) throws IOException {
        Path path = new Path(dirpath);
        return fileSystem.exists(path) && fileSystem.delete(path, true);
    }

    public boolean deleteFile(String filepath) throws IOException {
        Path path = new Path(filepath);
        return fileSystem.exists(path) && fileSystem.delete(path, false);
    }

    public void close() {
        if (fileSystem != null) {
            try {
                fileSystem.close();
            } catch (IOException e) {
                logger.info("Failed to close file system {}", e);
            }
        }

        opened = false;
    }
}
