package app.bravo.zu.spiderx.core;

import app.bravo.zu.spiderx.core.parser.KanDy;
import app.bravo.zu.spiderx.core.pipeline.ConsolePipeline;
import org.junit.Test;

public class SpiderTest {


    /**
     * 爬取看电影数据
     */
    @Test
    public void testCrawlKandy() {
        Spider.create("kanDy_Spider", KanDy.class)
                .url("http://kandy.cc/?s=vod-type-id-1.html")
                .initialDelay(3000)
                .pipeline(new ConsolePipeline())
                .run();
    }
}
