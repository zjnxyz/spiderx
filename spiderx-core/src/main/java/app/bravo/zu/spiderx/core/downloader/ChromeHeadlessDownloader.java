package app.bravo.zu.spiderx.core.downloader;

import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.client.chrome.ChromeHeadlessClient;
import lombok.extern.slf4j.Slf4j;


/**
 * chrome Headless 下载器
 *
 * @author riverzu
 */
@Slf4j
public class ChromeHeadlessDownloader extends AbstractDownloader {

    public ChromeHeadlessDownloader(Site site) {
        super(new ChromeHeadlessClient(site));
    }
}
