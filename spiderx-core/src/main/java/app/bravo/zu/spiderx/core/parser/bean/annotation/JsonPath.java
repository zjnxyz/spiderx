package app.bravo.zu.spiderx.core.parser.bean.annotation;

import java.lang.annotation.*;

/**
 * json path 抽取
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Element
public @interface JsonPath {

    String path();

}
