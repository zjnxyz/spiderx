package app.bravo.zu.spiderx.http.request;

import lombok.Data;

/**
 * get 请求
 */
@Data
public class GetRequest extends HttpRequest {

    private GetRequest(String url) {
        super.setUrl(url);
        super.setMethod(HttpMethod.GET);
    }



    public static class GetRequestBuilder {

        private final GetRequest request;

        public GetRequestBuilder (String url) {
            request = new GetRequest(url);
        }

        public GetRequest build() {
            return request;
        }
    }
}
