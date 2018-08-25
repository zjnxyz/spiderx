package app.bravo.zu.spiderx.http.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 请求
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpRequest {

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

    private String refer;

    /**
     * 编码
     */
    private String charset;


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
