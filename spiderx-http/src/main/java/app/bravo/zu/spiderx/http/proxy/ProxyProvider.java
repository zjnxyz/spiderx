package app.bravo.zu.spiderx.http.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

/**
 * 代理提供者
 *
 * @author riverzu
 */
public interface ProxyProvider {

    /**
     * 加载代理数据
     *
     * @return list
     */
    List<Proxy> load();

    /**
     * 检查代理的可用性
     *
     * @param proxy 代理
     * @return boolean
     */
    default boolean checkActive(Proxy proxy){
        if (proxy == null){
            return false;
        }
        try(Socket socket = new Socket()){
            InetSocketAddress address = new InetSocketAddress(proxy.getHostName(), proxy.getPort());
            socket.connect(address, 3000);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
