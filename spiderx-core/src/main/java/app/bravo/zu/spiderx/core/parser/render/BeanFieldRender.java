package app.bravo.zu.spiderx.core.parser.render;

import app.bravo.zu.spiderx.core.Page;
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
}
