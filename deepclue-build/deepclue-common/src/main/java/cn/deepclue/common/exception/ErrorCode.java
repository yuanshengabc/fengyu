package cn.deepclue.common.exception;


public interface ErrorCode {
    /**
     * 错误码
     */
    Integer getCode();

    /**
     * 错误提示信息
     */
    String getMessage();
}
