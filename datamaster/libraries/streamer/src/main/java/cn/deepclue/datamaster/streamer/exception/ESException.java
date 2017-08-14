package cn.deepclue.datamaster.streamer.exception;

/**
 * Created by magneto on 17-4-7.
 */
public class ESException extends StreamerException {

    public ESException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public ESException(String message, String localizedMessage, Throwable cause) {
        super(message, localizedMessage, cause);
    }
}
