package app.bravo.zu.spiderx.http.request;

import app.bravo.zu.spiderx.http.Site;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求
 * @author riverzu
 */
@Data
@AllArgsConstructor
public class HttpRequest implements Cloneable{

    /**
     * json
     */
    public final static String JSON_MEDIA_TYPE = "application/json";

    /**
     * 表单
     */
    public final static String FORM_MEDIA_TYPE = "application/x-www-form-urlencoded";

    public final static String HTML_CONTENT_TYPE = "text/html; charset=utf-8";

    /**
     * Referer
     */
    private final static String HEADER_REFERER = "Referer";

    private String url;

    /**
     * 网站信息
     */
    private Site site;

    /**
     * 请求方法
     */
    private HttpMethod method;

    /**
     * 请求参数
     */
    private Map<String, String> parameters;


    private Map<String, String> headers;

    /**
     * 额外透传字段
     */
    private Map<String,Object> extras;


    private String refer;

    /**
     * 编码
     */
    private String charset;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public HttpRequest(String url, HttpMethod httpMethod) {
        this();
        this.url = url;
        this.method = httpMethod;
    }

    public HttpRequest() {
        this.parameters = new HashMap<>();
        this.headers = new HashMap<>();
        this.extras = new HashMap<>();
    }

    /**
     * 设置参数
     *
     * @param key key
     * @param value value
     */
    public void parameter(String key, String value) {
        this.parameters.put(key, value);
    }

    /**
     * 设置header 信息
     * @param name name
     * @param value value
     */
    public void header(String name, String value) {
        this.headers.put(name, value);
    }

    /**
     * 设置referer地址
     *
     * @param referer referer
     */
    public void referer(String referer) {
        header(HEADER_REFERER, referer);
    }


    public enum  HttpMethod {

        /**
         * GET
         */
        GET,

        /**
         * post
         */
        POST,

        /**
         * put
         */
        PUT,

        /**
         * 只请求头信息
         */
        HEAD
    }


}


