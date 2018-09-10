package app.bravo.zu.spiderx.core.parser.bean.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Field {
    String value() default "";
}
