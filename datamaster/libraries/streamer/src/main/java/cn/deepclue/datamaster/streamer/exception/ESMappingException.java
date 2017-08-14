package cn.deepclue.datamaster.streamer.exception;

/**
 * Created by magneto on 17-4-6.
 */
public class ESMappingException extends ESException {

    public ESMappingException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public ESMappingException(String message, String localizedMessage, Throwable cause) {
        super(message, localizedMessage, cause);
    }
}
