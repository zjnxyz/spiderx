package app.bravo.zu.spiderx.http.request;

import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.cookie.CookieProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

import static app.bravo.zu.spiderx.http.request.HttpRequest.HttpMethod.GET;

/**
 *
 * get 请求
 * @author riverzu
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
            request.getParameters().putAll(parameters);
            return this;
        }

        public GetRequestBuilder headers(Map<String, String> headers) {
            request.getHeaders().putAll(headers);
            return this;
        }

        public GetRequestBuilder extras(Map<String, Object> extras){
            request.getExtras().putAll(extras);
            return this;
        }

        public GetRequestBuilder site(Site site) {
            request.setSite(site);
            return this;
        }


        public GetRequest build() {
            return request;
        }
    }
}
