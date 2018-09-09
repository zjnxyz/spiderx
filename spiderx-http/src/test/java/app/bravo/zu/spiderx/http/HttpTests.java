package app.bravo.zu.spiderx.http;

import app.bravo.zu.spiderx.http.client.HttpClient;
import app.bravo.zu.spiderx.http.client.okhttp.OkHttpClient;
import app.bravo.zu.spiderx.http.cookie.CookieProvider;
import app.bravo.zu.spiderx.http.cookie.DefaultCookieProvider;
import app.bravo.zu.spiderx.http.request.GetRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Mono;

@Slf4j
public class HttpTests {

    public static void main(String [] args) {
        log.debug("hello world");
//        Site site = Site.builder().useCookie(true).build();
//        HttpClient client = new OkHttpClient(site);
//        GetRequest request = GetRequest.builder("http://www.kandy.cc/").build();
//        client.get(request).doOnError(System.err::println)
//                .onErrorReturn(HttpResponse.error())
//                .subscribe(System.out::println);
    }

    @Test
    public void testRequestBaiDu() {
        String domain = "www.baidu.com";
        Site site = Site.builder().domain(domain).useCookie(true)
                .cookieProvider(DefaultCookieProvider.instance(domain, "BAIDUID=7D68262F6E841CD700BEA825FDCB1AD9:FG=1"))
                .build();
        HttpClient client = new OkHttpClient(site);
        client.get(GetRequest.builder("https://www.baidu.com/").build()).doOnError(System.err::println)
                .onErrorReturn(HttpResponse.error())
                .subscribe(System.out::println);
    }

    @Test
    public void test1() {
        System.out.println("hello world");
    }
}
