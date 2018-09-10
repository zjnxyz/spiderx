package app.bravo.zu.spiderx.core.downloader;

import app.bravo.zu.spiderx.core.Task;
import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.cookie.DefaultCookieProvider;
import app.bravo.zu.spiderx.http.request.GetRequest;
import org.junit.Test;

public class OkHttpDownloaderTest {

    @Test
    public void testLoadBaidu() {
        String domain = "www.baidu.com";
        Site site = Site.builder().domain(domain).useCookie(true)
                .cookieProvider(DefaultCookieProvider.instance(domain, "BAIDUID=7D68262F6E841CD700BEA825FDCB1AD9:FG=1"))
                .build();
        Downloader downloader = new OkHttpDownloader(site);
        Task task = Task.builder().uuid("uuid-1").priority(1)
                .request(GetRequest.builder("https://www.baidu.com/").build())
                .build();
        downloader.process(task).subscribe(System.out::println);
    }

}
