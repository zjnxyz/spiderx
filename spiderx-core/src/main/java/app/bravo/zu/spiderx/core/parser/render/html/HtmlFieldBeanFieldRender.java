package app.bravo.zu.spiderx.core.parser.render.html;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import app.bravo.zu.spiderx.core.parser.bean.annotation.HtmlField;
import app.bravo.zu.spiderx.core.parser.render.BeanFieldRender;
import app.bravo.zu.spiderx.core.parser.render.BeanRender;
import app.bravo.zu.spiderx.core.parser.render.BeanRenderFactory;
import app.bravo.zu.spiderx.core.parser.render.FieldDescribe;
import app.bravo.zu.spiderx.core.utils.BeanUtils2;
import app.bravo.zu.spiderx.core.utils.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

import static app.bravo.zu.spiderx.core.utils.ReflectUtils.haveSuperType;
import static java.util.stream.Collectors.toList;

/**
 * 单个html字段注入
 *
 * @author riverzu
 */
@Slf4j
public class HtmlFieldBeanFieldRender implements BeanFieldRender {

    @Override
    public void doRender(Page page, BeanMap beanMap, FieldDescribe fieldDescribe) {
        HtmlField htmlField = (HtmlField) fieldDescribe.getAnnotation();
        Class<?> type = fieldDescribe.getType();

        if (type.isArray()) {
            if (ReflectUtils.haveSuperType(type.getComponentType(), SpiderBean.class)) {
                beanMap.put(fieldDescribe.getName(),
                        beansInject((Class<? extends SpiderBean>) type, page, htmlField).toArray());
            }else {
                //数组
                beanMap.put(fieldDescribe.getName(), resolveList(page, htmlField, type).toArray());
            }
        } else if (haveSuperType(type, List.class)) {
            Class genericClass = ReflectUtils.getGenericClass(fieldDescribe.getGenericType(), 0);
            if (ReflectUtils.haveSuperType(genericClass, SpiderBean.class)) {
                //处理对象
                beanMap.put(fieldDescribe.getName(),
                        beansInject((Class<? extends SpiderBean>) type, page, htmlField));
            } else {
                //集合
                beanMap.put(fieldDescribe.getName(), resolveList(page, htmlField, type));
            }

        } else {
            //单个值
            String value = page.getHtml().xpath(htmlField.xPath()).get();
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

    private List<Object> resolveList(Page page, HtmlField htmlField, Class<?> type) {
        List<String> values = page.getHtml().xpath(htmlField.xPath()).all();
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }

        return values.stream().filter(StringUtils::isNotEmpty)
                .map(t -> BeanUtils2.convert(t, type))
                .collect(toList());
    }

    private Object beanInject(Class<? extends SpiderBean> clz, Page page, String body) {
        Page subPage = page.clone(body);
        if (subPage == null) {
            log.warn("page clone 失败");
            return null;
        }
        BeanRender beanRender = BeanRenderFactory.instance().get(clz);
        if (beanRender == null) {
            log.error("clz={}, 未找到对应的beanRender", clz);
            return null;
        }
        return beanRender.inject(clz, subPage);
    }

    private List<Object> beansInject(Class<? extends SpiderBean> clz, Page page, HtmlField htmlField) {
        List<String> values = page.getHtml().xpath(htmlField.xPath()).all();
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }

        return values.stream().filter(StringUtils::isNotEmpty)
                .map(t -> beanInject(clz, page, t))
                .collect(toList());
    }
}
