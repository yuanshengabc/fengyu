package cn.deepclue.demo.app.common.exception;

/**
 * AppException异常类对应的错误码
 *
 * Created by luoyong on 17-6-30.
 */
public enum AppErrorEnum implements ErrorCode {
    UNKOWN(1000, "未知错误"),
    DEMO_NOT_EXISTS(1001, "不存在对应的对象");

    private int code;
    private String message;

    AppErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
