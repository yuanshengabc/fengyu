package cn.deepclue.demo.app.common.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在方法上加上该注解将不打印http请求日志
 *
 * 例如上传文件
 * <p>
 * Created by luoyong on 17-4-8.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpLogIgnore {
}
