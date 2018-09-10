package app.bravo.zu.spiderx.core.parser.render;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;

/**
 * 渲染器，将响应结果渲染成对应的bean
 *
 * @author riverzu
 */
public interface BeanRender {

    /**
     * 注入
     * @param clz clz
     * @param page page
     * @return bean
     */
    SpiderBean inject(Class<? extends SpiderBean> clz, Page page);

}
