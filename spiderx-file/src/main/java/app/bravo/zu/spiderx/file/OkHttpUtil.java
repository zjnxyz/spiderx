package app.bravo.zu.spiderx.file;

import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.client.HttpClient;
import app.bravo.zu.spiderx.http.client.okhttp.OkHttpClient;

/**
 * okHttp 客户端
 *
 * @author riverzu
 */
public class OkHttpUtil {

    private OkHttpClient client;

    private OkHttpUtil() {
        client = new OkHttpClient(Site.builder().connectTimeout(30000).build());
    }

    /**
     * 获取请求客户端
     *
     * @return HttpClient
     */
    static HttpClient getClient() {
        return OkHttpUtilHolder.UTIL.client;
    }

    private static class OkHttpUtilHolder {
        private static OkHttpUtil UTIL = new OkHttpUtil();
    }
}
