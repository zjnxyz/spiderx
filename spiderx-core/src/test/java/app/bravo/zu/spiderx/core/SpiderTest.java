package app.bravo.zu.spiderx.core;

import app.bravo.zu.spiderx.core.downloader.ChromeHeadlessDownloader;
import app.bravo.zu.spiderx.core.listener.KanDyEventListener;
import app.bravo.zu.spiderx.core.parser.KanDy;
import app.bravo.zu.spiderx.core.parser.TbItemList;
import app.bravo.zu.spiderx.core.pipeline.KanDyDetailPipeline;
import app.bravo.zu.spiderx.core.pipeline.TbItemListPipeline;
import app.bravo.zu.spiderx.http.request.GetRequest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SpiderTest {


    /**
     * 爬取看电影数据
     */
    @Test
    public void testCrawlKandy() {
        Spider.create("kanDy_Spider", KanDy.class)
                .url("http://kandy.cc/?s=vod-type-id-1.html")
                .initialDelay(3000).workerNum(2)
                .pipeline(new KanDyDetailPipeline())
                .listener(new KanDyEventListener())
                .run();
    }

    @Test
    public void testHeadlessCrawlKandy() {
        Spider.create("kanDy_Spider", KanDy.class)
                .url("http://kandy.cc/?s=vod-type-id-1.html")
                .downloaderClz(ChromeHeadlessDownloader.class)
                .initialDelay(3000).workerNum(2)
                .pipeline(new KanDyDetailPipeline())
                .listener(new KanDyEventListener())
                .run();
    }

    @Test
    public void testCrawTb() {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        GetRequest request = GetRequest.builder(TbItemListPipeline.LIST_URL).parameters(params).build();
        Spider.create("tb_item_list_spider", TbItemList.class).request(request)
                .initialDelay(3000).pipeline(new TbItemListPipeline()).run();
    }
}
