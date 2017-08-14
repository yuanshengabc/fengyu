package cn.deepclue.datamaster.streamer.exception;

public class StreamerException extends RuntimeException {
    private String localizedMessage;

    public StreamerException(String message, String localizedMessage) {
        super(message);
        this.localizedMessage = localizedMessage;
    }

    public StreamerException(String message, String localizedMessage, Throwable cause) {
        super(message, cause);
        this.localizedMessage = localizedMessage;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
