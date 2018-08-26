package app.bravo.zu.spiderx.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

import static app.bravo.zu.spiderx.http.request.HttpRequest.HttpMethod.GET;

/**
 * get 请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetRequest extends HttpRequest {

    private GetRequest(String url) {
        super(url, GET);
    }

    public static GetRequestBuilder builder (String url){
        return new GetRequestBuilder(url);
    }



    public static class GetRequestBuilder {

        private final GetRequest request;

        GetRequestBuilder (String url) {
            request = new GetRequest(url);
        }

        public GetRequestBuilder parameters(Map<String, String> parameters) {
            request.setParameters(parameters);
            return this;
        }

        public GetRequestBuilder headers(Map<String, String> headers) {
            request.setHeaders(headers);
            return this;
        }

        public GetRequestBuilder cookies(Map<String, String> cookies) {
            request.setCookies(cookies);
            return this;
        }


        public GetRequest build() {
            return request;
        }
    }
}
