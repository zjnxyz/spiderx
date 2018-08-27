package app.bravo.zu.spiderx.http;

import app.bravo.zu.spiderx.http.client.HttpClient;
import app.bravo.zu.spiderx.http.client.okhttp.OkHttpClient;
import app.bravo.zu.spiderx.http.request.GetRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;

public class HttpTests {

    public static void main(String [] args) {
        Site site = Site.builder().useCookie(true).build();
        HttpClient client = new OkHttpClient(site);
        GetRequest request = GetRequest.builder("http://www.kandy.cc/").build();
        HttpResponse response = client.get(request);
        System.out.println(response);
    }
}
