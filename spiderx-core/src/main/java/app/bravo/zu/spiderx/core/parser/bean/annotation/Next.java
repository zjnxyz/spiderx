package app.bravo.zu.spiderx.core.parser.bean.annotation;

import java.lang.annotation.*;

/**
 *
 * 需要下一步抓取的
 * 
 * @author huchengyi
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Next {

    /**
     * 解决使用相对路径的问题
     *
     * @return string
     */
    String domain() default "";

}
