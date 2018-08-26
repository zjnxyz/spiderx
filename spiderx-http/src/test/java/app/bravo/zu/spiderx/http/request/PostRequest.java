package app.bravo.zu.spiderx.http.request;

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
            request.setParameters(parameters);
            return this;
        }

        public PostRequestBuilder headers(Map<String, String> headers) {
            request.setHeaders(headers);
            return this;
        }

        public PostRequestBuilder cookies(Map<String, String> cookies) {
            request.setCookies(cookies);
            return this;
        }

        public PostRequestBuilder requestBody(String body) {
            request.setRequestBody(body);
            return this;
        }

        public PostRequest build() {
            return request;
        }

    }
}
