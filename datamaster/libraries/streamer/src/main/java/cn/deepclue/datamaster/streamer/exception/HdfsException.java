package cn.deepclue.datamaster.streamer.exception;

public class HdfsException extends StreamerException {
    public HdfsException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public HdfsException(String message, String localizedMessage, Throwable cause) {
        super(message, localizedMessage, cause);
    }
}
