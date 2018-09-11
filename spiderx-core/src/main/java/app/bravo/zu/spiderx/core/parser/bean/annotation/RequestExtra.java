package app.bravo.zu.spiderx.core.parser.bean.annotation;

import java.lang.annotation.*;

/**
 * 请求中的额外数据
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Element
@Inherited
public @interface RequestExtra {

    String value() default "";

}
