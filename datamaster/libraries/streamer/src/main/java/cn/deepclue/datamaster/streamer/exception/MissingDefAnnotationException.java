package cn.deepclue.datamaster.streamer.exception;

/**
 * Created by xuzb on 05/04/2017.
 */
public class MissingDefAnnotationException extends TransformerException {
    public MissingDefAnnotationException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public MissingDefAnnotationException(String message, String localizedMessage, Throwable cause) {
        super(message, localizedMessage, cause);
    }
}
