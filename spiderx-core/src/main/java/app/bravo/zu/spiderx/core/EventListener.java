package app.bravo.zu.spiderx.core;

/**
 * 事件监听器
 */
public interface EventListener {

    /**
     * 下载前
     * @param task
     * @param ctx
     */
    default void beforeDownload(Task<?> task, SpiderContext ctx) {

    }

}
