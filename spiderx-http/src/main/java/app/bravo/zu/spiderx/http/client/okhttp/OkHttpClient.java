package app.bravo.zu.spiderx.http.client.okhttp;

import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.client.HttpClient;
import app.bravo.zu.spiderx.http.exception.HttpException;
import app.bravo.zu.spiderx.http.proxy.Proxy;
import app.bravo.zu.spiderx.http.request.HttpRequest;
import app.bravo.zu.spiderx.http.request.PostRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import static app.bravo.zu.spiderx.http.request.HttpRequest.HttpMethod.GET;
import static app.bravo.zu.spiderx.http.request.HttpRequest.HttpMethod.POST;

/**
 * okHttp客户端
 * @author riverzu
 */
@Slf4j
public class OkHttpClient implements HttpClient {

    private final static MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private final static String CONTENT_TYPE = "Content-Type";

    private final static String USER_AGENT = "User-Agent";

    private okhttp3.OkHttpClient.Builder okBuilder;

    private final Site site;

    public OkHttpClient(Site site) {
        this.site = site;
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
            log.error("ssl init fail.err={}", e.getMessage(), e);
        }
        okBuilder.followRedirects(true).retryOnConnectionFailure(true);
        if (site.isUseCookie()) {
            okBuilder.cookieJar(new CookiesManager(site.getCookieProvider()));
        }
        if (site.isUseProxy()) {
            Proxy proxy = site.getProxy();
            if (proxy != null){
                log.debug("proxy 信息：{}", proxy);
                okBuilder.proxy(proxy.toJavaNetProxy());
            }
        }

        if (site.getConnectTimeout() > 0){
            okBuilder.connectTimeout(site.getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(site.getConnectTimeout(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public Mono<HttpResponse> execute(HttpRequest request) {
        Request.Builder requestBuilder = OkHttpRequestGenerator.getOkHttpRequestBuilder(request, site);
        if (requestBuilder == null){
            return null;
        }
        return Mono.just(requestBuilder.build()).map(t -> {
            try {
                return okBuilder.build().newCall(t).execute();
            }catch (Exception e){
                log.error("远程请求异常，url="+request.getUrl(), e);
                throw new HttpException("远程请求异常", e);
            }
        }).map(t ->{
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setStatus(t.code());
            ResponseBody responseBody = t.body();
            httpResponse.setCharset(t.header(CONTENT_TYPE));
            //处理header信息
            t.headers().names().forEach(t1 -> httpResponse.header(t1, t.header(t1)));

            if (responseBody != null) {
                try {
                    httpResponse.setBodyText(responseBody.string());
                }catch (Exception e){
                    log.error("httpResponse 获取响应体异常", e);
                }
            }
            return httpResponse;
        });
    }



    private static class OkHttpRequestGenerator {

         static Request.Builder getOkHttpRequestBuilder(HttpRequest request, Site site) {
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

            if (!request.getHeaders().containsKey(USER_AGENT)) {
                request.getHeaders().put(USER_AGENT, site.getUserAgent());
            }

            if (MapUtils.isNotEmpty(request.getHeaders())) {
                // 设置请求头
                request.getHeaders().forEach(requestBuilder::addHeader);
            }
            return requestBuilder;
        }
    }

}
