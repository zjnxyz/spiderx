package app.bravo.zu.spiderx.file;

import java.io.File;

import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.client.HttpClient;
import app.bravo.zu.spiderx.http.client.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * okHttp文件下载器
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/26
 */
@Slf4j
public class OkHttpFileDownloader implements FileDownloader {

    @Override public File download(Request request, String savePath) {
        //文件不存在时，使用临时目录作为文件保存地址
        if (StringUtils.isEmpty(savePath)) {
            savePath = getTmpPath();
        }

        return null;
    }


    private static class OkHttpUtil {

        private OkHttpClient client;

        private OkHttpUtil() {
            client = new OkHttpClient(Site.builder().connectTimeout(30000).build());
        }

        /**
         * 获取请求客户端
         *
         * @return HttpClient
         */
        public static HttpClient getClient() {
            return OkHttpUtilHolder.UTIL.client;
        }

        private static class OkHttpUtilHolder {
            private static OkHttpUtil UTIL = new OkHttpUtil();
        }
    }
}
