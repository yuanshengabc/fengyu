package cn.deepclue.common.exception;


public class AppException extends BaseException {

    public AppException(AppErrorEnum errorCode) {
        super(errorCode);
    }

    public AppException(String message, AppErrorEnum errorCode) {
        super(message, errorCode);
    }

    public AppException(String message, Throwable cause, AppErrorEnum errorCode) {
        super(message, cause, errorCode);
    }
}
