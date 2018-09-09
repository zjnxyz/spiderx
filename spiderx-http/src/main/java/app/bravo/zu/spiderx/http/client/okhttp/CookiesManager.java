package app.bravo.zu.spiderx.http.client.okhttp;

import app.bravo.zu.spiderx.http.cookie.CookieGroup;
import app.bravo.zu.spiderx.http.cookie.CookieProvider;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

/**
 *
 * @author riverzu
 */
@Slf4j
public class CookiesManager implements CookieJar {

    private CookieProvider cookieProvider;

    public CookiesManager(CookieProvider provider) {
        this.cookieProvider = provider;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
        log.info("cookie host={}", httpUrl.host());
        List<app.bravo.zu.spiderx.http.cookie.Cookie> list = cookies.stream().filter(Objects::nonNull)
                .map(t -> new app.bravo.zu.spiderx.http.cookie.Cookie(t.name(), t.value(), t.domain()))
                .collect(toList());
        cookieProvider.save(httpUrl.host(), list);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl httpUrl) {
        log.info("cookie host={}", httpUrl.host());
        CookieGroup cookieGroup = cookieProvider.load(httpUrl.host());
        if (cookieGroup == null || CollectionUtils.isEmpty(cookieGroup.getCookies())){
            return Collections.emptyList();
        }
        return cookieGroup.getCookies().stream().filter(Objects::nonNull).map(t -> {
            Cookie.Builder builder = new Cookie.Builder();
            builder.name(t.getName()).value(t.getValue())
                    .domain(StringUtils.isNotEmpty(t.getDomain()) ? t.getDomain() : cookieGroup.getDomain());
            return builder.build();
        }).collect(toList());
    }




}
