package app.bravo.zu.spiderx.core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 爬取
 * @author riverzu
 */
public class Crawler implements Runnable {

    @Getter
    private Spider spider;

    @Getter
    private SpiderContext ctx;

    /**
     * 监听器
     */
    private List<EventListener> listeners;


    public Crawler(Spider spider, SpiderContext ctx) {
        this.spider = spider;
        this.ctx = ctx;
        this.listeners = new ArrayList<>();
    }




    @Override
    public void run() {

    }



}
