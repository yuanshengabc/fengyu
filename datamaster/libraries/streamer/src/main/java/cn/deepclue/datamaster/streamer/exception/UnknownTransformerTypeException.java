package cn.deepclue.datamaster.streamer.exception;

/**
 * Created by xuzb on 07/04/2017.
 */
public class UnknownTransformerTypeException extends TransformerException {
    public UnknownTransformerTypeException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public UnknownTransformerTypeException(String message, String localizedMessage, Throwable cause) {
        super(message, localizedMessage, cause);
    }
}
