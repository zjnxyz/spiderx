package app.bravo.zu.spiderx.core.parser.bean.annotation;

import java.lang.annotation.*;

/**
 * json 对象
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Element
public @interface JsonObject {

    String path();

}
