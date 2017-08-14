package cn.deepclue.datamaster.cleaner.exception;

/**
 * Created by sunxingwen on 17-5-8.
 */
public class SpecialException extends BaseException {
    public SpecialException(SpecialErrorEnum errorCode) {
        super(errorCode);
    }

    public SpecialException(String message, String localizedMessage, SpecialErrorEnum errorCode) {
        super(message, localizedMessage, errorCode);
    }

    public SpecialException(String message, String localizedMessage, Throwable cause, SpecialErrorEnum errorCode) {
        super(message, localizedMessage, cause, errorCode);
    }
}
