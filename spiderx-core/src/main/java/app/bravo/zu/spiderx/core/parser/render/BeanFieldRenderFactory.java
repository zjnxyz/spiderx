package app.bravo.zu.spiderx.core.parser.render;

import app.bravo.zu.spiderx.core.parser.bean.annotation.HtmlField;
import app.bravo.zu.spiderx.core.parser.bean.annotation.RequestExtra;
import app.bravo.zu.spiderx.core.parser.bean.annotation.RequestParameter;
import app.bravo.zu.spiderx.core.parser.render.html.HtmlFieldBeanFieldRender;

import java.util.HashMap;
import java.util.Map;

/**
 * bean field 工厂类
 *
 * @author riverzu
 */
public class BeanFieldRenderFactory {

    private final static Map<Class<?>,BeanFieldRender> BEAN_FIELD_RENDER_MAP = new HashMap<>();

    private BeanFieldRenderFactory() {
        BEAN_FIELD_RENDER_MAP.put(HtmlField.class, new HtmlFieldBeanFieldRender());
        BEAN_FIELD_RENDER_MAP.put(RequestExtra.class, new RequestExtraBeanFieldRender());
        BEAN_FIELD_RENDER_MAP.put(RequestParameter.class, new RequestParameterBeanFieldRender());
    }

    public BeanFieldRender get(Class<?> cls) {
        return BEAN_FIELD_RENDER_MAP.get(cls);
    }


    public static BeanFieldRenderFactory instance() {
        return BeanFieldRenderFactoryHolder.factory;
    }

    private static class BeanFieldRenderFactoryHolder {
        private final static BeanFieldRenderFactory factory = new BeanFieldRenderFactory();
    }

}
