package cn.deepclue.datamaster.cleaner.exception;

/**
 * Created by luoyong on 17-4-21.
 */
public class JdbcException extends BaseException {

    public JdbcException(JdbcErrorEnum errorCode) {
        super(errorCode);
    }

    public JdbcException(String message, String localizedMessage, JdbcErrorEnum errorCode) {
        super(message, localizedMessage, errorCode);
    }

    public JdbcException(String message, String localizedMessage, Throwable cause, JdbcErrorEnum errorCode) {
        super(message, localizedMessage, cause, errorCode);
    }
}
