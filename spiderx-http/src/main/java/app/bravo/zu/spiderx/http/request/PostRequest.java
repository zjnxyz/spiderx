package app.bravo.zu.spiderx.http.request;

import app.bravo.zu.spiderx.http.Site;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

import static app.bravo.zu.spiderx.http.request.HttpRequest.HttpMethod.POST;


/**
 * post 请求参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostRequest extends HttpRequest{

    private String requestBody;

    private PostRequest(String url) {
        super(url, POST);
    }

    public static PostRequestBuilder builder(String url) {
        return new PostRequestBuilder(url);
    }

    public static class PostRequestBuilder {

        private final PostRequest request;


        PostRequestBuilder(String url) {
            request = new PostRequest(url);
        }

        public PostRequestBuilder parameters(Map<String, String> parameters) {
            request.getParameters().putAll(parameters);
            return this;
        }

        public PostRequestBuilder headers(Map<String, String> headers) {
            request.getHeaders().putAll(headers);
            return this;
        }

        public PostRequestBuilder cookies(Map<String, String> cookies) {
            request.getCookies().putAll(cookies);
            return this;
        }

        public PostRequestBuilder extras(Map<String, Object> extras){
            request.getExtras().putAll(extras);
            return this;
        }

        public PostRequestBuilder requestBody(String body) {
            request.setRequestBody(body);
            return this;
        }

        public PostRequestBuilder site(Site site) {
            request.setSite(site);
            return this;
        }

        public PostRequest build() {
            return request;
        }

    }
}
