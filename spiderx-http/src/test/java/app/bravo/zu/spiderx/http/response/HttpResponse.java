package app.bravo.zu.spiderx.http.response;

import java.io.ByteArrayInputStream;
import java.util.Map;

import lombok.Data;

/**
 * 类/接口注释
 *
 * @author jiangnan.zjn
 * @createDate 2018/8/26
 */
@Data
public class HttpResponse {

    /**
     * 流
     */
    private ByteArrayInputStream bodyRaw;

    /**
     * 文本
     */
    private String bodyText;

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

}
