package cn.deepclue.datamaster.cleaner.exception;

/**
 * Created by luoyong on 17-4-24.
 */
public abstract class BaseException extends RuntimeException implements ErrorCode {
    private final String localizedMessage;
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.localizedMessage = errorCode.getLocalizedMessage();
        this.errorCode = errorCode;
    }

    public BaseException(String message, String localizedMessage, ErrorCode errorCode) {
        super(message);
        this.localizedMessage = localizedMessage;
        this.errorCode = errorCode;
    }

    public BaseException(String message, String localizedMessage, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.localizedMessage = localizedMessage;
        this.errorCode = errorCode;
    }

    @Override
    public Integer getCode() {
        return errorCode != null ? errorCode.getCode() : null;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
