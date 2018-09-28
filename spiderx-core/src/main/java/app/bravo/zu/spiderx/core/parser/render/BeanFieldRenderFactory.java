package app.bravo.zu.spiderx.core.parser.render;

import app.bravo.zu.spiderx.core.parser.bean.annotation.*;
import app.bravo.zu.spiderx.core.parser.render.html.HtmlFieldBeanFieldRender;
import app.bravo.zu.spiderx.core.parser.render.json.JsonObjectBeanFieldRender;
import app.bravo.zu.spiderx.core.parser.render.json.JsonPathBeanFieldRender;

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
        BEAN_FIELD_RENDER_MAP.put(JsonPath.class, new JsonPathBeanFieldRender());
        BEAN_FIELD_RENDER_MAP.put(JsonObject.class, new JsonObjectBeanFieldRender());
        BEAN_FIELD_RENDER_MAP.put(Media.class, new MediaBeanFieldRender());
    }

    public BeanFieldRender get(Class<?> cls) {
        return BEAN_FIELD_RENDER_MAP.get(cls);
    }


    public static BeanFieldRenderFactory instance() {
        return BeanFieldRenderFactoryHolder.FACTORY;
    }

    private static class BeanFieldRenderFactoryHolder {
        private final static BeanFieldRenderFactory FACTORY = new BeanFieldRenderFactory();
    }

}
