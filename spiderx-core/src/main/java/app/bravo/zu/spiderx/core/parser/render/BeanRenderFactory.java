package app.bravo.zu.spiderx.core.parser.render;


import app.bravo.zu.spiderx.core.parser.bean.HtmlBean;
import app.bravo.zu.spiderx.core.parser.bean.JsonBean;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import app.bravo.zu.spiderx.core.parser.render.html.HtmlBeanRender;
import app.bravo.zu.spiderx.core.parser.render.json.JsonBeanRender;

import java.util.HashMap;
import java.util.Map;


/**
 * BeanRender 工厂类
 *
 * @author riverzu
 */
public class BeanRenderFactory {

    private final static Map<Class<? extends SpiderBean>, BeanRender>  BEAN_RENDER_MAP = new HashMap<>();

    private BeanRenderFactory() {
        BEAN_RENDER_MAP.put(HtmlBean.class, new HtmlBeanRender());
        BEAN_RENDER_MAP.put(JsonBean.class, new JsonBeanRender());
    }

    public BeanRender get(Class<? extends SpiderBean> clz) {
        Class<? extends SpiderBean> key = BEAN_RENDER_MAP.keySet().stream()
                .filter(t -> t.isAssignableFrom(clz)).findFirst()
                .orElse(null);
        if (key == null) {
            return null;
        }
        return BEAN_RENDER_MAP.get(key);
    }

    public static BeanRenderFactory instance() {
        return BeanRenderFactoryHolder.FACTORY;
    }

    private static class BeanRenderFactoryHolder {
        private final static BeanRenderFactory FACTORY = new BeanRenderFactory();
    }
}
