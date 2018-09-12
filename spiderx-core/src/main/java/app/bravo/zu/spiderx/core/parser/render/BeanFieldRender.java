package app.bravo.zu.spiderx.core.parser.render;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import org.apache.commons.beanutils.BeanMap;

/**
 * b
 * @author riverzu
 */
public interface BeanFieldRender {

    /**
     * 渲染
     *
     * @param page page
     * @param beanMap beanMap
     * @param fieldDescribe 字段信息
     */
    default void render(Page page, BeanMap beanMap, FieldDescribe fieldDescribe) {
        if (fieldDescribe == null) {
            return;
        }
        doRender(page, beanMap, fieldDescribe);

    }

    /**
     * 进行渲染
     * @param page page
     * @param beanMap beanMap
     * @param fieldDescribe fd
     */
    void doRender(Page page, BeanMap beanMap, FieldDescribe fieldDescribe);

    /**
     * bean 注入
     *
     * @param clz  clz
     * @param page page
     * @param body body
     * @return object
     */
    default Object beanInject(Class<? extends SpiderBean> clz, Page page, String body) {
        Page subPage = page.clone(body);
        if (subPage == null) {
            return null;
        }
        BeanRender beanRender = BeanRenderFactory.instance().get(clz);
        if (beanRender == null) {
            return null;
        }
        return beanRender.inject(clz, subPage);
    }
}
