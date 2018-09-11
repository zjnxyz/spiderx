package app.bravo.zu.spiderx.core.parser.render;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import app.bravo.zu.spiderx.core.parser.bean.annotation.Element;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

/**
 * 渲染器，将响应结果渲染成对应的bean
 *
 * @author riverzu
 */
public interface BeanRender {

    Class<? extends Annotation> BEAN_BASE_ANNOTATIONS = Element.class;

    /**
     * 注入
     * @param clz clz
     * @param page page
     * @return bean
     */
    SpiderBean inject(Class<? extends SpiderBean> clz, Page page);


    /**
     * 是否有对应annotation
     *
     * @param annotation 对应 annotation
     * @return boolean
     */
    default boolean hasAnnotation(Annotation annotation) {
        return annotation.annotationType().getName().equals(BEAN_BASE_ANNOTATIONS.getName());
    }

    /**
     * 判断子注解中是否包含 BEAN_BASE_ANNOTATIONS
     *
     * @param annotation annotation
     * @return boolean
     */
    default boolean hasMetaAnnotation(Annotation annotation) {
        Annotation[] metas = annotation.annotationType().getAnnotations();
        Optional<Annotation> result = Arrays.stream(metas).filter(this::hasAnnotation).findFirst();
        return result.isPresent();
    }

}
