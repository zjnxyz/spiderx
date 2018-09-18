package app.bravo.zu.spiderx.core.downloader;

import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.client.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;

/**
 * okHttp 下载器
 *
 * @author riverzu
 */
@Slf4j
public class OkHttpDownloader extends AbstractDownloader {

    /**
     * 构造函数
     * @param site 网站信息
     */
    public OkHttpDownloader(Site site) {
        super(new OkHttpClient(site));
    }
}
