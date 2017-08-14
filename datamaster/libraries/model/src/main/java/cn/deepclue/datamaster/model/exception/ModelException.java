package cn.deepclue.datamaster.model.exception;

/**
 * Created by sunxingwen on 17-5-10.
 */
public class ModelException extends RuntimeException {
    private final String localizedMessage;

    public ModelException(String message, String localizedMessage) {
        super(message);
        this.localizedMessage = localizedMessage;
    }

    public ModelException(String message, String localizedMessage, Throwable cause) {
        super(message, cause);
        this.localizedMessage = localizedMessage;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
