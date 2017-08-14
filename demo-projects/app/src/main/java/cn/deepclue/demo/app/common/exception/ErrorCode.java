package cn.deepclue.demo.app.common.exception;

/**
 * Created by luoyong on 17-6-26.
 */
public interface ErrorCode {
    /**
     * 错误码
     *
     * @return
     */
    Integer getCode();

    /**
     * 错误提示信息
     *
     * @return
     */
    String getMessage();
}
