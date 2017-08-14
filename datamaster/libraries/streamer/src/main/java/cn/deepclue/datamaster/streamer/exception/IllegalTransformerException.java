package cn.deepclue.datamaster.streamer.exception;

/**
 * Created by xuzb on 20/04/2017.
 */
public class IllegalTransformerException extends TransformerException {
    public IllegalTransformerException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public IllegalTransformerException(String message, String localizedMessage, Throwable cause) {
        super(message, localizedMessage, cause);
    }
}
