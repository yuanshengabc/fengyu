package cn.deepclue.datamaster.cleaner.exception;

/**
 * Created by xuzb on 27/03/2017.
 */
public class CleanerException extends BaseException {
    public CleanerException(CleanerException e) {
        super(e.getMessage(), e.getLocalizedMessage(), e.getErrorCode());

    }
    public CleanerException(BizErrorEnum errorCode) {
        super(errorCode);
    }

    public CleanerException(String message, String localizedMessage, BizErrorEnum errorCode) {
        super(message, localizedMessage, errorCode);
    }

    public CleanerException(String message, String localizedMessage, Throwable cause, BizErrorEnum errorCode) {
        super(message, localizedMessage, cause, errorCode);
    }
}
