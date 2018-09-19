package app.bravo.zu.spiderx.core.parser.render.json;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import app.bravo.zu.spiderx.core.parser.bean.annotation.JsonObject;
import app.bravo.zu.spiderx.core.parser.render.BeanFieldRender;
import app.bravo.zu.spiderx.core.parser.render.FieldDescribe;
import app.bravo.zu.spiderx.core.parser.selector.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static app.bravo.zu.spiderx.core.utils.ReflectUtils.getGenericClass;
import static app.bravo.zu.spiderx.core.utils.ReflectUtils.haveSuperType;
import static java.util.stream.Collectors.toList;

/**
 * json 对象渲染器
 *
 * @author riverzu
 */
@Slf4j
public class JsonObjectBeanFieldRender implements BeanFieldRender {

    @Override
    public void doRender(Page page, BeanMap beanMap, FieldDescribe fieldDescribe) {
        if (fieldDescribe.getType().isPrimitive()) {
            log.warn("JsonObject 不能用在基本类型 field={}, type={}", fieldDescribe.getName(),
                    fieldDescribe.getType());
            return;
        }
        JsonObject jsonObject = (JsonObject) fieldDescribe.getAnnotation();
        Class<?> type = fieldDescribe.getType();
        if (type.isArray()) {
            beanMap.put(fieldDescribe.getName(), beansInject((Class<? extends SpiderBean>) type.getComponentType(), page, jsonObject).toArray());
            return;
        } else if (haveSuperType(type, List.class)) {
            beanMap.put(fieldDescribe.getName(), beansInject(getGenericClass(fieldDescribe.getGenericType(), 0), page, jsonObject));
            return;
        }

        String value = page.getJson().jsonPath(jsonObject.path()).get();
        if (StringUtils.isEmpty(value)) {
            log.warn("jsonPath={}, 对应的value为空", jsonObject.path());
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("jsonPath={}, 对应的value={}", jsonObject.path(), value);
        }

        beanMap.put(fieldDescribe.getName(), new Json(value).toObject(fieldDescribe.getType()));
    }

    private List<Object> beansInject(Class<? extends SpiderBean> clz, Page page, JsonObject jsonObject) {
        List<String> values = page.getJson().jsonPath(jsonObject.path()).all();
        if (CollectionUtils.isEmpty(values)) {
            log.warn("jsonObject path={},value is null, body={}", jsonObject.path(), page.getResponse().getBodyText());
            return Collections.emptyList();
        }
        return values.stream().filter(Objects::nonNull).map(t -> new Json(t).toObject(clz)).collect(toList());
    }

}
