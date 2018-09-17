package app.bravo.zu.spiderx.core.parser;


import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.SpiderContext;
import app.bravo.zu.spiderx.core.Task;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import app.bravo.zu.spiderx.core.parser.bean.annotation.Next;
import app.bravo.zu.spiderx.core.parser.render.BeanRender;
import app.bravo.zu.spiderx.core.parser.render.BeanRenderFactory;
import com.google.common.base.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import static app.bravo.zu.spiderx.core.utils.ReflectUtils.getGenericClass;
import static app.bravo.zu.spiderx.core.utils.ReflectUtils.haveSuperType;
import static java.util.stream.Collectors.toList;
import static org.reflections.ReflectionUtils.getAllFields;

/**
 * 结果解析器
 *
 * @author riverzu
 */
@Slf4j
public class Parser {

    private Parser() {

    }


    /**
     * 解析响应数据
     *
     * @param clz  目标对象
     * @param page page
     * @param ctx  上下文
     * @param <T>  T
     * @return spiderBean
     */
    public <T extends SpiderBean> T parse(Class<T> clz, Page page, SpiderContext ctx) {
        BeanRender beanRender = BeanRenderFactory.instance().get(clz);
        if (beanRender == null) {
            log.warn("class={}, 找不到对应的BeanRender", clz);
            return null;
        }
        SpiderBean bean = beanRender.inject(clz, page);
        if (bean == null) {
            log.warn("class={], 渲染失败，page={}", clz, page);
            return null;
        }
        requests(bean, page.getTask(), ctx);
        return (T) bean;
    }


    /**
     * 解析next 请求
     *
     * @param bean bean
     * @param task task
     * @param ctx  ctx
     */
    private void requests(SpiderBean bean, Task task, SpiderContext ctx) {
        Set<Field> fields = getAllFields(bean.getClass(), typeFilter);
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        BeanMap beanMap = new BeanMap(bean);
        fields.stream().filter(Objects::nonNull).forEach(t -> {
            if (hasNextAnnotation(t)) {
                //加入到上下文中
                ctx.getQueue().into(resolveUrl(beanMap, t, task));
            } else {
                resolveBeanUrl(beanMap, t, task, ctx);
            }
        });
    }

    private void resolveBeanUrl(BeanMap beanMap, Field field, Task task, SpiderContext ctx) {
        Class<?> type = field.getType();
        Object o = beanMap.get(field.getName());
        if (o == null) {
            log.info("field={}, 值为空", field.getName());
            return;
        }

        if (type.isArray()) {
            SpiderBean[] arr = (SpiderBean[]) o;
            Arrays.stream(arr).filter(Objects::nonNull).forEach(t -> requests(t, task, ctx));
        } else if (haveSuperType(type, List.class)) {
            List list = (List) o;
            list.stream().filter(Objects::nonNull).forEach(t -> requests((SpiderBean) t, task, ctx));
        } else {
            requests((SpiderBean) o, task, ctx);

        }
    }

    private List<Task> resolveUrl(BeanMap beanMap, Field field, Task task) {
        Class<?> type = field.getType();
        Object o = beanMap.get(field.getName());
        if (o == null) {
            return Collections.emptyList();
        }

        List<Object> list;
        if (type.isArray()) {
            Object[] arr = (Object[]) o;
            list = Arrays.asList(arr);
        } else if (haveSuperType(type, List.class)) {
            List l = (List) o;
            list = new ArrayList<>(l.size());
            list.addAll(l);
        } else {
            list = Collections.singletonList(o);
        }
        return list.stream().filter(Objects::nonNull).map(Object::toString)
                .peek(t -> log.info("下一页：{}", t))
                .map(task::clone).filter(Objects::nonNull).collect(toList());
    }


    private boolean hasNextAnnotation(Field field) {
        Next next = field.getAnnotation(Next.class);
        return next != null;
    }

    /**
     * 类型过滤器
     */
    private Predicate<Field> typeFilter = field -> {
        if (hasNextAnnotation(field)) {
            return true;
        }
        Class<?> type = field.getType();
        return haveSuperType(type, SpiderBean.class)
                || type.isArray() && haveSuperType(type.getComponentType(), SpiderBean.class)
                || haveSuperType(type, List.class)
                && haveSuperType(getGenericClass(field.getGenericType(), 0), SpiderBean.class);
    };

    public static Parser instance() {
        return ParserHolder.PARSER;
    }

    private static class ParserHolder {
        private static Parser PARSER = new Parser();
    }
}
