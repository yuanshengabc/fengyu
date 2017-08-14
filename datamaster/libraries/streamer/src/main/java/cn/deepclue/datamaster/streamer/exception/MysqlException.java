package cn.deepclue.datamaster.streamer.exception;

/**
 * Created by magneto on 17-3-31.
 */
public class MysqlException extends StreamerException {

    public MysqlException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public MysqlException(String message, String localizedMessage, Throwable cause) {
        super(message, localizedMessage, cause);
    }
}
