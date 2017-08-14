package cn.deepclue.datamaster.streamer.exception;

/**
 * Created by magneto on 17-6-15.
 */
public class RegexFilterException extends StreamerException {
    public RegexFilterException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public RegexFilterException(String message, String localizedMessage, Throwable cause) {
        super(message, localizedMessage, cause);
    }

}
