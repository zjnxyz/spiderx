package app.bravo.zu.spiderx.http;

import app.bravo.zu.spiderx.http.cookie.CookieProvider;
import app.bravo.zu.spiderx.http.cookie.DefaultCookieProvider;
import app.bravo.zu.spiderx.http.proxy.Proxy;
import app.bravo.zu.spiderx.http.proxy.ProxySelector;
import app.bravo.zu.spiderx.http.ua.Ua;
import app.bravo.zu.spiderx.http.ua.UaSelector;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 需要爬取的网站信息
 *
 * @author riverzu
 */
@Builder
@Getter
public class Site implements Cloneable{

    @Setter
    private String domain;

    private volatile String userAgent;

    private String charset;

    private boolean useGzip;

    /**
     * 是否使用cookie
     */
    private boolean useCookie;

    /**
     * 是否使用代理
     */
    private boolean useProxy;

    /**
     * ua选择
     */
    private UaSelector uaSelector;

    /**
     * 代理
     */
    private volatile Proxy proxy;

    /**
     * 代理选择器
     */
    private ProxySelector proxySelector;

    /**
     * cookie 提供者
     */
    private volatile CookieProvider cookieProvider;

    /**
     * 休眠时间
     */
    private int sleepTime = 5000;

    /**
     * 超时时间
     */
    private int connectTimeout = 5000;

    /**
     * 重试次数
     */
    private int retryTimes = 0;

    /**
     * 代理检查状态
     */
    private final AtomicInteger proxyCheckStat = new AtomicInteger();

    /**
     * 获取ua
     *
     * @return string
     */
    public String getUserAgent() {
        if (StringUtils.isNotEmpty(userAgent)) {
            return userAgent;
        }

        synchronized (this) {
            if (StringUtils.isEmpty(userAgent)){
                Ua ua;
                if (uaSelector == null) {
                    ua = UaSelector.def().select();
                } else {
                    ua = uaSelector.select();
                }
                userAgent = ua.getValue();
            }
        }
        return userAgent;
    }

    /**
     * 获取代理
     *
     * @return proxy
     */
    public Proxy getProxy() {
        if (proxySelector == null){
            return proxy;
        }

        if (proxy == null || System.currentTimeMillis() - proxy.getLastSuccessfulTime() > 5*60*1000L){
            synchronized (this) {
                if (proxyCheckStat.compareAndSet(0, 1)) {
                    proxy = proxySelector.checkAndGet(proxy);
                    proxyCheckStat.compareAndSet(1,0);
                }
            }
        }

        return proxy;
    }

    public CookieProvider getCookieProvider() {
        if (cookieProvider == null) {
            synchronized (this) {
                if (cookieProvider == null){
                    cookieProvider = DefaultCookieProvider.instance();
                }
            }

        }
        return cookieProvider;
    }


}
