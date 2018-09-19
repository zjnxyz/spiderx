package app.bravo.zu.spiderx.core.parser.render.json;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import app.bravo.zu.spiderx.core.parser.bean.annotation.JsonPath;
import app.bravo.zu.spiderx.core.parser.render.BeanFieldRender;
import app.bravo.zu.spiderx.core.parser.render.FieldDescribe;
import app.bravo.zu.spiderx.core.utils.BeanUtils2;
import app.bravo.zu.spiderx.core.utils.ReflectUtils;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static app.bravo.zu.spiderx.core.utils.BeanUtils2.convert;
import static app.bravo.zu.spiderx.core.utils.ReflectUtils.haveSuperType;
import static java.util.stream.Collectors.toList;

/**
 * json path 渲染器
 *
 * @author riverzu
 */
public class JsonPathBeanFieldRender implements BeanFieldRender {

    @Override
    public void doRender(Page page, BeanMap beanMap, FieldDescribe fieldDescribe) {
        JsonPath jsonPath = (JsonPath) fieldDescribe.getAnnotation();
        Class<?> type = fieldDescribe.getType();
        if (type.isArray()) {
            if (ReflectUtils.haveSuperType(type.getComponentType(), SpiderBean.class)) {
                beanMap.put(fieldDescribe.getName(),
                        beansInject((Class<? extends SpiderBean>) type.getComponentType(), page, jsonPath).toArray());
            } else {
                //数组
                beanMap.put(fieldDescribe.getName(), resolveList(page, jsonPath, type).toArray());
            }
        } else if (haveSuperType(type, List.class)) {
            Class genericClass = ReflectUtils.getGenericClass(fieldDescribe.getGenericType(), 0);
            if (ReflectUtils.haveSuperType(genericClass, SpiderBean.class)) {
                //处理对象
                beanMap.put(fieldDescribe.getName(),
                        beansInject(genericClass, page, jsonPath));
            } else {
                //集合
                beanMap.put(fieldDescribe.getName(), resolveList(page, jsonPath, type));
            }
        } else {
            //单个值
            String value = page.getJson().jsonPath(jsonPath.path()).get();
            if (StringUtils.isEmpty(value)) {
                return;
            }
            if (ReflectUtils.haveSuperType(type, SpiderBean.class)) {
                beanMap.put(fieldDescribe.getName(), beanInject((Class<? extends SpiderBean>) type, page, value));
            } else {
                beanMap.put(fieldDescribe.getName(), BeanUtils2.convert(value, type));
            }
        }
    }


    private List<Object> resolveList(Page page, JsonPath jsonPath, Class<?> type) {
        List<String> values = page.getJson().jsonPath(jsonPath.path()).all();
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        return values.stream().filter(StringUtils::isNotEmpty)
                .map(t -> convert(t, type))
                .collect(toList());
    }


    /**
     * bean 集合注入
     *
     * @param clz      clz
     * @param page     page
     * @param jsonPath jsonPath
     * @return list
     */
    private List<Object> beansInject(Class<? extends SpiderBean> clz, Page page, JsonPath jsonPath) {
        List<String> values = page.getJson().jsonPath(jsonPath.path()).all();
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }

        return values.stream().filter(StringUtils::isNotEmpty)
                .map(t -> beanInject(clz, page, t)).filter(Objects::nonNull)
                .collect(toList());
    }
}
