package app.bravo.zu.spiderx.http.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求
 */
@Data
@AllArgsConstructor
public class HttpRequest implements Cloneable{

    private String url;

    /**
     * 请求方法
     */
    private HttpMethod method;

    /**
     * 请求参数
     */
    private Map<String, String> parameters;

    /**
     * cookies
     */
    private Map<String, String> cookies;

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


    public HttpRequest(String url, HttpMethod httpMethod) {
        this();
        this.url = url;
        this.method = httpMethod;
    }

    public HttpRequest() {
        this.parameters = new HashMap<>();
        this.headers = new HashMap<>();
        this.cookies = new HashMap<>();
        this.extras = new HashMap<>();
    }


     enum  HttpMethod {

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
         PUT
    }
}
