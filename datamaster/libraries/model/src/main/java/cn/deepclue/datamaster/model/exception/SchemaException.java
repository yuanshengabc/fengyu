package cn.deepclue.datamaster.model.exception;

/**
 * Created by sunxingwen on 17-5-10.
 */
public class SchemaException extends ModelException {
    public SchemaException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public SchemaException(String message, String localizedMessage, Throwable cause) {
        super(message, localizedMessage, cause);
    }
}
