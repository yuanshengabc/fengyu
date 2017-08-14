package cn.deepclue.datamaster.cleaner.exception;

/**
 * Created by luoyong on 17-4-21.
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

    /**
     * 本地化错误提示信息
     *
     * @return
     */
    String getLocalizedMessage();
}
