package app.bravo.zu.spiderx.http.client.okhttp;


import okhttp3.OkHttpClient;

/**
 * 类/接口注释
 *
 * @author jiangnan.zjn@alibaba-inc.com
 * @createDate 2018/7/13
 */
public class OkHttpClientBuilderProvider {

    private static class OkHttpclientBuilderProviderHolder {
        static OkHttpClientBuilderBox okHttpClientBuilderBox = new OkHttpClientBuilderBox();
    }

    public static OkHttpClient.Builder getInstance() {
        return OkHttpclientBuilderProviderHolder.okHttpClientBuilderBox.instance();
    }
}
