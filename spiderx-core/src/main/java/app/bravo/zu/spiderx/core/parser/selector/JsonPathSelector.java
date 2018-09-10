package app.bravo.zu.spiderx.core.parser.selector;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JsonPath selector.<br>
 * Used to extract content from JSON.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @author tony
 */
public class JsonPathSelector implements Selector {

    private JsonPath jsonPath;

    public JsonPathSelector(String jsonPathStr) {
        this.jsonPath = JsonPath.compile(jsonPathStr);
    }

    @Override
    public String select(String text) {
        Object object = jsonPath.read(text);
        if (object == null) {
            return null;
        }
        if (object instanceof List) {
            List list = (List) object;
            if (CollectionUtils.isNotEmpty(list)) {
                return toString(list.iterator().next());
            }
        }
        return object.toString();
    }

    private String toString(Object object) {
        if (object instanceof Map) {
            return JSON.toJSONString(object);
        } else {
            return String.valueOf(object);
        }
    }

    @Override
    public List<String> selectList(String text) {
        List<String> list = new ArrayList<String>();
        Object object = jsonPath.read(text);
        if (object == null) {
            return list;
        }
        if (object instanceof List) {
            List<Object> items = (List<Object>) object;
            for (Object item : items) {
                list.add(toString(item));
            }
        } else {
            list.add(toString(object));
        }
        return list;
    }
}
