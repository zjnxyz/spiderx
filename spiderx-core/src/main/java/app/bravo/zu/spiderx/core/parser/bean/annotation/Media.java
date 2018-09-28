package app.bravo.zu.spiderx.core.parser.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 媒体
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/28
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Element
public @interface Media {

    /**
     * json的path
     *
     * @return string
     */
    String path();

    /**
     * 保存位置
     *
     * @return string
     */
    Location location() default Location.LOCAL;

    /**
     * 图片对应的域名，解决网站用相对地址的问题
     *
     * @return string
     */
    String imageDomain() default "";

    /**
     * 保存路径
     *
     * @return string
     */
    String savePath() default "";



    enum Location {

        /**
         * 本地
         */
        LOCAL,

        /**
         * 新浪图片
         */
        SINA_IMG;

    }
}
