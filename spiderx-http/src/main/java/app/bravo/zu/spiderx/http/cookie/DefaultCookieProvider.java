package app.bravo.zu.spiderx.http.cookie;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认cookieProvider
 *
 * @author riverzu
 */
public class DefaultCookieProvider implements CookieProvider {

    /**
     * cookie
     */
    private Map<String, CookieGroup> map = new ConcurrentHashMap<>();

    private DefaultCookieProvider() {

    }

    @Override
    public void save(String domain, List<Cookie> cookies) {
        CookieGroup cookieGroup = new CookieGroup(domain);
        cookieGroup.putCookies(cookies);
        map.put(domain, cookieGroup);
    }

    @Override
    public CookieGroup load(String domain) {
        return map.getOrDefault(domain, new CookieGroup(""));
    }

    public static DefaultCookieProvider instance(){
        return DefaultCookieProviderHolder.provider;
    }


    private static class DefaultCookieProviderHolder{
        static DefaultCookieProvider provider = new DefaultCookieProvider();
    }
}
