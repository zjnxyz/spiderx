package app.bravo.zu.spiderx.http.client.okhttp;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author riverzu
 */
public class CookiesManager implements CookieJar {

    private final Map<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

    @Override
    public synchronized void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
        cookieStore.put(httpUrl.host(),cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl httpUrl) {
        List<Cookie> cookies = cookieStore.get(httpUrl.host());
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }


    public static CookiesManager instance(){
        return CookiesManagerHolder.cookiesManager;
    }


    private static class CookiesManagerHolder{
        public static CookiesManager cookiesManager = new CookiesManager();
    }



}
