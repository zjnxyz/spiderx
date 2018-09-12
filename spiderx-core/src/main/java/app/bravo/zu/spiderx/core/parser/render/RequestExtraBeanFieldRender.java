package app.bravo.zu.spiderx.core.parser.render;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.parser.bean.annotation.RequestExtra;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;


/**
 * 请求额外数据渲染器
 *
 * @author riverzu
 */
@Slf4j
public class RequestExtraBeanFieldRender implements BeanFieldRender{

    @Override
    public void doRender(Page page, BeanMap beanMap, FieldDescribe fieldDescribe) {
        RequestExtra requestExtra = (RequestExtra) fieldDescribe.getAnnotation();
        String name = StringUtils.isEmpty(requestExtra.value()) ? fieldDescribe.getName()
                : requestExtra.value();
        Object value = page.getExtras().get(name);
        if (value == null) {
            log.info("field = {}, 额外参数中不存在", name);
            return;
        }
        beanMap.put(name, value);
    }
}
