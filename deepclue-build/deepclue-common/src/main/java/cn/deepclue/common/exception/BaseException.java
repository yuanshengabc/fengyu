package cn.deepclue.common.exception;

/**
 * 自定义异常超类
 */
public abstract class BaseException extends RuntimeException implements ErrorCode {
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    @Override
    public Integer getCode() {
        return errorCode != null ? errorCode.getCode() : null;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
