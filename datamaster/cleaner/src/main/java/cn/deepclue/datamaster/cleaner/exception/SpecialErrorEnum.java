package cn.deepclue.datamaster.cleaner.exception;

/**
 * Created by sunxingwen on 17-5-8.
 */
public enum SpecialErrorEnum implements ErrorCode {
    DUPLICATEKEYEXCEPTION(9001, "DuplicateKeyException", "重复键。"),
    STREAMEREXCEPTION(9002, "StreamerException", "Streamer异常。"),
    OTHER(9999, "unknown exception error", "未知的异常错误。");

    private Integer code;
    private String message;
    private String localizedMessage;

    SpecialErrorEnum(Integer code, String message, String localizedMessage) {
        this.code = code;
        this.message = message;
        this.localizedMessage = localizedMessage;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
