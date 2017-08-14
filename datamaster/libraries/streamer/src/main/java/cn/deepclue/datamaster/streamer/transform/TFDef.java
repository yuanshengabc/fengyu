package cn.deepclue.datamaster.streamer.transform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xuzb on 30/03/2017.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TFDef {
    String name() default "";
    String semaName();
    String summary();
    String description() default "";
    String alertMsg() default "";
    boolean alert() default false;
    int order() default 0;
}