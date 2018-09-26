package app.bravo.zu.spiderx.http.response;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 类/接口注释
 *
 * @author jiangnan.zjn
 * @createDate 2018/8/26
 */
@Data
public class HttpResponse implements Cloneable {

    public HttpResponse () {
        headers = new HashMap<>();
    }

    /**
     * 文本
     */
    private String bodyText;

    /**
     * 字节body
     */
    private Byte[] bodyBytes;

    /**
     * 响应头
     */
    private Map<String, String> headers;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 编码
     */
    private String charset;

    /**
     * 状态
     */
    private int status;

    public HttpResponse header(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public String getBodyText() {
        if (StringUtils.isEmpty(bodyText)) {
            return null;
        }
        //去掉空格
        return bodyText.trim();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * 失败响应
     *
     * @return response
     */
    public static HttpResponse error() {
        HttpResponse response = new HttpResponse();
        response.setStatus(404);
        return response;
    }

}
