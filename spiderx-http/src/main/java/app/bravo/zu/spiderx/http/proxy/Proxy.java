package app.bravo.zu.spiderx.http.proxy;

import lombok.Data;

import java.net.InetSocketAddress;

/**
 * 代理信息
 *
 * @author riverzu
 */
@Data
public class Proxy {

    public final static String HTTPS = "https";

    public final static String HTTP = "http";

    /**
     * 代理类型，https/http
     */
    private String type;

    private String ip;

    private int port;

    private String username;

    private String password;

    /**
     * 最新一次请求时间
     */
    private long lastSuccessfulTime;

    /**
     * 请求成功耗时
     */
    private long successfulTotalTime;

    /**
     * 请求失败次数
     */
    private int failureTimes;

    /**
     * 请求成功次数
     */
    private int successfulTimes;

    /**
     * 请求成功平均耗时
     */
    private double successfulAverageTime;


    public Proxy(String type, String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.type = type;
    }


    public Proxy(String ip, int port) {
        this(HTTP, ip, port);
    }


    public String getHostName() {
        return type + "://" + ip;
    }

    public String getProxyStr() {
        return type + "://" + ip + ":" + port;
    }


    public java.net.Proxy toJavaNetProxy() {

        return new java.net.Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress(ip , port));
    }



}
