package app.bravo.zu.spiderx.core;

/**
 * 事件监听器
 *
 * @author riverzu
 */
public interface EventListener {

    /**
     * 下载前
     * @param task 任务
     * @param ctx 上下文
     */
    default void beforeDownload(Task task, SpiderContext ctx) {

    }

    /**
     * 下载后
     *
     * @param page 结果数据
     * @param ctx  上下文
     */
    default void afterDownload(Page page, SpiderContext ctx) {

    }


}
