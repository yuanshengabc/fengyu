package cn.deepclue.datamaster.streamer.exception;

/**
 * Created by xuzb on 18/04/2017.
 */
public class KafkaException extends StreamerException {
    public KafkaException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public KafkaException(String message, String localizedMessage, Throwable cause) {
        super(message, localizedMessage, cause);
    }
}
