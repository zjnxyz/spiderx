package app.bravo.zu.spiderx.core.parser.render;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import com.google.common.base.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections4.CollectionUtils;
import org.reflections.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * bean渲染抽象实现
 *
 * @author riverzu
 */
@Slf4j
public abstract class AbstractBeanRender implements BeanRender {

    @Override
    public SpiderBean inject(Class<? extends SpiderBean> clz, Page page) {
        try {
            SpiderBean bean = clz.newInstance();
            BeanMap beanMap = new BeanMap(bean);
            Set<Field> fields = ReflectionUtils.getAllFields(clz, typeFilter);
            if (CollectionUtils.isEmpty(fields)) {
                return bean;
            }

            fields.stream().filter(Objects::nonNull).forEach(t ->{
                Annotation[] annotations = t.getDeclaredAnnotations();
                if (annotations != null && annotations.length > 0) {
                    Arrays.stream(annotations).filter(t1 -> hasAnnotation(t1) || hasMetaAnnotation(t1))
                            .forEach(t1 -> {
                                BeanFieldRender fieldRender = BeanFieldRenderFactory.instance()
                                        .get(t1.annotationType());
                                if (fieldRender != null) {
                                    FieldDescribe fd = new FieldDescribe(t, t1);
                                    fieldRender.render(page, beanMap, fd);
                                }
                            });
                }
            });
            return bean;
        } catch (InstantiationException | IllegalAccessException e) {
            log.warn("解析spiderBean出错了", e);
        }
        return null;
    }

    /**
     * 类型过滤
     */
    private Predicate<Field> typeFilter = s -> {
        Annotation[] annotations = s.getDeclaredAnnotations();
        if (annotations == null || annotations.length == 0) {
            return false;
        }
        Optional<Annotation> result = Arrays.stream(annotations)
                .filter(t -> hasAnnotation(t) || hasMetaAnnotation(t))
                .findFirst();
        return result.isPresent();
    };

}
