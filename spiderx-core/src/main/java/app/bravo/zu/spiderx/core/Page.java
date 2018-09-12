package app.bravo.zu.spiderx.core;

import app.bravo.zu.spiderx.core.parser.selector.Html;
import app.bravo.zu.spiderx.core.parser.selector.Json;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;

/**
 * 请求返回值
 * @author riverzu
 */
@Data
@Builder
public class Page implements Cloneable{

    /**
     * 请求
     */
    private Task task;


    /**
     * 响应状态
     */
    private int status;

    /**
     * 响应
     */
    private HttpResponse response;

    private Html html;

    private Json json;


    public Html getHtml() {
        if (StringUtils.isEmpty(response.getBodyText())){
            return null;
        }
        if (html == null){
            this.html = Html.create(response.getBodyText());
        }
        return html;
    }

    public Json getJson() {
        if (StringUtils.isEmpty(response.getBodyText())){
            return null;
        }
        if (json == null) {
            json = new Json(response.getBodyText());
        }
        return json;
    }

    /**
     * 获取请求参数
     *
     * @return map
     */
    public Map<String, String> getParameters() {
        return task.getRequest().getParameters();
    }

    public Map<String, Object> getExtras() {
        Map<String, Object> extras = task.getExtras();
        if (MapUtils.isEmpty(extras)) {
            return Collections.emptyMap();
        }
        return extras;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Page clone(String bodyText) {
        try {
            Page page = (Page) this.clone();
            HttpResponse response = (HttpResponse) page.getResponse().clone();
            response.setBodyText(bodyText);
            page.setResponse(response);
            page.setHtml(null);
            page.setJson(null);
            return page;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 错误页面响应
     * @param task 请求任务
     * @return page
     */
    public static Page error(Task task) {
        return Page.builder().task(task).status(400).build();
    }
}
