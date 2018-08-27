package app.bravo.zu.spiderx.http.client;

import app.bravo.zu.spiderx.http.request.GetRequest;
import app.bravo.zu.spiderx.http.request.HttpRequest;
import app.bravo.zu.spiderx.http.request.PostRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author riverzu
 */
public interface HttpClient {

    Pattern CHARSET_PATTERN = Pattern.compile("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)");


    /**
     * http + get 请求
     * @param request 请求参数
     * @return
     */
    default HttpResponse get(GetRequest request) {
        return execute(request);
    }

    default HttpResponse post(PostRequest request) {
        return execute(request);
    }



    /**
     * 请求
     * @param request 参数
     * @return response
     */
    HttpResponse execute(HttpRequest request);

    /**
     * 获取网页的字符编码
     * @param requestCharset 请求时编码
     * @param contentType ct
     * @return string
     */
    default String getCharset(String requestCharset, String contentType) {
        //先取contentType的字符集
        if (Strings.isNullOrEmpty(requestCharset) && Strings.isNullOrEmpty(contentType)){
            return "UTF-8";
        }
        if (Strings.isNullOrEmpty(contentType)){
            return requestCharset;
        }

        Matcher m = CHARSET_PATTERN.matcher(contentType);
        if (m.find()) {
            return m.group(1).trim().toUpperCase();
        }
        return "UTF-8";
    }


}
