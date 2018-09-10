package app.bravo.zu.spiderx.core.parser.render;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import org.apache.commons.beanutils.BeanMap;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;

/**
 * bean渲染抽象实现
 *
 * @author riverzu
 */
public abstract class AbstractBeanRender implements BeanRender {

    @Override
    public SpiderBean inject(Class<? extends SpiderBean> clz, Page page) {
        try {
            SpiderBean bean = clz.newInstance();
            BeanMap beanMap = new BeanMap(bean);
            Set<Field> fields = ReflectionUtils.getAllFields(clz,
                    ReflectionUtils.withAnnotation(app.bravo.zu.spiderx.core.parser.bean.annotation.Field.class));

            fields.stream().filter(Objects::nonNull).forEach(t ->{
//                BeanFieldRenderFactory.instance().get()
            });

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }


        return null;
    }
}
