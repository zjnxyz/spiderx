package app.bravo.zu.spiderx.http.proxy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 代理选择器
 *
 * @author riverzu
 */
public class ProxySelector {

    private final ProxyProvider provider;

    /**
     * 原子递增器
     */
    private final AtomicInteger pointer;

    /**
     * 代理集合
     */
    private List<Proxy> proxies;

    private ProxySelector(ProxyProvider provider) {
        this.provider = provider;
        this.pointer = new AtomicInteger(-1);
        init();
    }


    private void init() {
        proxies = provider.load();
    }

    /**
     * 代理选择
     *
     * @return proxy
     */
    public Proxy select() {
        return select(null);
    }


    /**
     * 根据代理类型选择
     *
     * @param proxyType 类型
     * @return Proxy
     */
    public Proxy select(String proxyType) {
        return proxies.get(incrForLoop());
    }

    /**
     * 检查代理是否活着
     * @return proxy
     */
    private boolean checkActive(Proxy proxy) {
        return provider.checkActive(proxy);
    }


    /**
     * 检查一个代理，如果代理无效时，返回一个新的有效代理
     * @param proxy proxy
     * @return proxy
     */
    public Proxy checkAndGet(Proxy proxy) {
        if (proxy == null) {
            return select();
        }

        if (!checkActive(proxy)) {
            return select(proxy.getType());
        }
        return proxy;
    }

    private int incrForLoop() {
        int p = pointer.incrementAndGet();
        int size = proxies.size();
        if (p < size) {
            return p;
        }
        while (!pointer.compareAndSet(p, p % size)) {
            p = pointer.get();
        }
        return p % size;
    }

    public static ProxySelector def(ProxyProvider provider) {
        return new ProxySelector(provider);
    }

    public static ProxySelector def() {
        return def(new DefaultProxyProvider());
    }


}
