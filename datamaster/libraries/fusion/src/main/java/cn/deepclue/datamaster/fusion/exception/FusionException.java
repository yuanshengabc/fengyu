package cn.deepclue.datamaster.fusion.exception;

/**
 * Created by xuzb on 23/05/2017.
 */
public class FusionException extends RuntimeException {
    private String localizedMessage;

    public FusionException(String message, String localizedMessage) {
        super(message);
        this.localizedMessage = localizedMessage;
    }

    public FusionException(String message, String localizedMessage, Throwable cause) {
        super(message, cause);
        this.localizedMessage = localizedMessage;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
