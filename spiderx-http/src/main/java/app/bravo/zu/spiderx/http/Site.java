package app.bravo.zu.spiderx.http;

import lombok.Builder;
import lombok.Data;

/**
 * 需要爬取的网站信息
 */
@Builder
@Data
public class Site implements Cloneable{

    /**
     * 默认 ua
     */
    public final static String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.699.0 Safari/534.24";

    private String domain;

    private String userAgent;

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


}
