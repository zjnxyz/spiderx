package app.bravo.zu.spiderx.http.client;

import app.bravo.zu.spiderx.http.request.GetRequest;
import app.bravo.zu.spiderx.http.request.HttpRequest;
import app.bravo.zu.spiderx.http.request.PostRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;

public interface HttpClient {


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

}
