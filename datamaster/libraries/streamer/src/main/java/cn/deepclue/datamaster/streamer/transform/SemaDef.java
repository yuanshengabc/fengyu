package cn.deepclue.datamaster.streamer.transform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xuzb on 30/03/2017.
 */
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SemaDef {
    String name() default "";
    String semaName();
    String description() default "";
    String type();
    String defaultValue() default "";
    String domain() default "source";
    boolean require() default true;
    int minValue() default Integer.MIN_VALUE;
    int maxValue() default Integer.MAX_VALUE;
    int order() default 0;
}