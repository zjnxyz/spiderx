package app.bravo.zu.spiderx.core.parser.bean.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Element
public @interface HtmlField {

	/**
	 * xpath
	 * 
	 * @return 元素选择器
	 */
	String xPath();
}
