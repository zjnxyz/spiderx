package app.bravo.zu.spiderx.core.downloader;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.Task;
import reactor.core.publisher.Mono;

/**
 * 网页下载器
 * @author riverzu
 */
public interface Downloader {

    /**
     * 进行页面下载
     * @param task 待处理任务
     * @return page
     */
    default Mono<Page> process(Task task) {
        return null;
    }

    /**
     * 停止任务
     */
    default void shutdown() {

    }

    /**
     * 是否完成
     *
     * @return boolean
     */
    boolean isCompeted();
}
