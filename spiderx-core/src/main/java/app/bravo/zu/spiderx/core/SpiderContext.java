package app.bravo.zu.spiderx.core;

import app.bravo.zu.spiderx.core.queue.Queue;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 爬虫上下文
 *
 * @author riverzu
 */
public class SpiderContext {

    /**
     * 当前爬虫下的子爬虫
     */
    @Getter
    private Map<String, Spider> subSpider = new ConcurrentHashMap<>();

    @Getter
    private Queue queue;

    private SpiderContext(Queue queue) {
        this.queue = queue;
    }

    public static SpiderContext instance(Queue queue) {
        return new SpiderContext(queue);
    }

}
