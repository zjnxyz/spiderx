package app.bravo.zu.spiderx.http.client.okhttp;

import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.client.HttpClient;
import app.bravo.zu.spiderx.http.request.HttpRequest;
import app.bravo.zu.spiderx.http.request.PostRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import com.google.common.base.Joiner;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static app.bravo.zu.spiderx.http.request.HttpRequest.HttpMethod.GET;
import static app.bravo.zu.spiderx.http.request.HttpRequest.HttpMethod.POST;

/**
 * okHttp客户端
 * @author riverzu
 */
public class OkHttpClient implements HttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpClient.class);

    private final static MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private okhttp3.OkHttpClient.Builder okBuilder;

    public OkHttpClient(Site site) {
        okBuilder = new okhttp3.OkHttpClient.Builder();
        try {
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[] {};
                }
            };
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    x509TrustManager
            };
            HostnameVerifier hostnameVerifier = (hostname, session) -> true;
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            okBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager).hostnameVerifier(hostnameVerifier);
        } catch (Exception e) {
            LOGGER.error("ssl init fail.err={}", e.getMessage(), e);
        }
        okBuilder.followRedirects(true).retryOnConnectionFailure(true);
        if (site.isUseCookie()) {
            okBuilder.cookieJar(new CookiesManager(site.getCookieProvider()));
        }
        if (site.isUseProxy()) {
            //TODO 代理
        }

        if (site.getConnectTimeout() > 0){
            okBuilder.connectTimeout(site.getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(site.getConnectTimeout(), TimeUnit.MILLISECONDS);
        }

    }

    @Override
    public HttpResponse execute(HttpRequest request) {
        Request.Builder requestBuilder = OkHttpRequestGenerator.getOkHttpRequestBuilder(request);
        if (requestBuilder == null){
            return null;
        }

        okhttp3.OkHttpClient client = okBuilder.build();
        try {
            Response response = client.newCall(requestBuilder.build()).execute();
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setStatus(response.code());
            ResponseBody responseBody = response.body();
            httpResponse.setCharset(response.header("Content-Type"));
            if (responseBody != null) {
                httpResponse.setBodyText(responseBody.string());
            }
            //处理header信息
            response.headers().names().forEach(t -> httpResponse.header(t, response.header(t)));
            return httpResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    private static class OkHttpRequestGenerator {

         static Request.Builder getOkHttpRequestBuilder(HttpRequest request) {
            Request.Builder requestBuilder = new Request.Builder();
            if (request.getMethod() == GET) {
                HttpUrl httpUrl = HttpUrl.parse(request.getUrl());
                if (httpUrl == null){
                    return null;
                }
                HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
                if (MapUtils.isNotEmpty(request.getParameters())) {
                    request.getParameters().forEach(httpUrlBuilder::addQueryParameter);
                }
                requestBuilder.url(httpUrlBuilder.build());
            } else if (request.getMethod() == POST) {
                RequestBody body;
                PostRequest postRequest = (PostRequest) request;
                if (!StringUtils.isEmpty(postRequest.getRequestBody())) {
                    body = RequestBody.create(JSON_MEDIA_TYPE, postRequest.getRequestBody());
                } else {
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    if (MapUtils.isNotEmpty(request.getParameters())){
                        request.getParameters().forEach(formBuilder::addEncoded);
                    }
                    body = formBuilder.build();
                }
                requestBuilder.post(body);
                requestBuilder.url(request.getUrl());
            }

            if (MapUtils.isNotEmpty(request.getHeaders())) {
                // 设置请求头
                request.getHeaders().forEach(requestBuilder::addHeader);
            }
            return requestBuilder;
        }
    }

}
