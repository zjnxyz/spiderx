package app.bravo.zu.spiderx.core.parser.bean.annotation;

import java.lang.annotation.*;

/**
 * 请求参数
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Element
public @interface RequestParameter {

	String value() default "";
	
}
