package app.bravo.zu.spiderx.core.parser.render;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.parser.bean.annotation.RequestParameter;
import app.bravo.zu.spiderx.core.utils.BeanUtils2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;

/**
 * 请求参数渲染
 *
 * @author riverzu
 */
@Slf4j
public class RequestParameterBeanFieldRender implements BeanFieldRender {

    @Override
    public void doRender(Page page, BeanMap beanMap, FieldDescribe fieldDescribe) {
        if (fieldDescribe == null) {
            log.warn("字段描述信息不能为空");
            return;
        }
        RequestParameter requestParameter = (RequestParameter) fieldDescribe.getAnnotation();
        String name = StringUtils.isEmpty(requestParameter.value()) ? fieldDescribe.getName()
                : requestParameter.value();
        String value = page.getParameters().get(name);
        if (StringUtils.isEmpty(value)){
            log.info("field={}, 请求参数中无该字段", name);
            return;
        }
        beanMap.put(name, BeanUtils2.convert(value, fieldDescribe.getType()));
    }

}
