package app.bravo.zu.spiderx.http.proxy;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的代理提供者实现
 *
 * @author riverzu
 */
public class DefaultProxyProvider implements ProxyProvider{

    @Override
    public List<Proxy> load() {
        return new ArrayList<>();
    }
}
