package app.bravo.zu.spiderx.http.client;

import app.bravo.zu.spiderx.http.request.GetRequest;
import app.bravo.zu.spiderx.http.request.HttpRequest;
import app.bravo.zu.spiderx.http.request.PostRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import com.google.common.base.Strings;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author riverzu
 */
public interface HttpClient {


    /**
     * http + get 请求
     * @param request 请求参数
     * @return mono
     */
    default Mono<HttpResponse> get(GetRequest request) {
        return execute(request);
    }

    /**
     * post 请求
     *
     * @param request request
     * @return mono
     */
    default Mono<HttpResponse> post(PostRequest request) {
        return execute(request);
    }



    /**
     * 请求
     *
     * @param request 参数
     * @return response
     */
    Mono<HttpResponse> execute(HttpRequest request);

}
