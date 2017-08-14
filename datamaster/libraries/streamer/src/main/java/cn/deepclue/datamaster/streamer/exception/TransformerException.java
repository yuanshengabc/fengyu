package cn.deepclue.datamaster.streamer.exception;

/**
 * Created by xuzb on 14/04/2017.
 */
public class TransformerException extends StreamerException {
    public TransformerException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public TransformerException(String message, String localizedMessage, Throwable cause) {
        super(message, localizedMessage, cause);
    }
}
