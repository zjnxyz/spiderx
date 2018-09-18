package app.bravo.zu.spiderx.core;

import app.bravo.zu.spiderx.core.downloader.ChromeHeadlessDownloader;
import app.bravo.zu.spiderx.core.listener.KanDyEventListener;
import app.bravo.zu.spiderx.core.parser.KanDy;
import app.bravo.zu.spiderx.core.parser.TbDetail;
import app.bravo.zu.spiderx.core.pipeline.ConsolePipeline;
import app.bravo.zu.spiderx.core.pipeline.KanDyDetailPipeline;
import org.junit.Test;

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
    public void testCrawlTaobao() {
        Spider.create("taobao_detail_spider", TbDetail.class)
            .url("https://detail.m.tmall.com/item.htm?spm=a2141.9304519.0.0&id=574767946531")
            .pipeline(new ConsolePipeline()).run();
    }
}
