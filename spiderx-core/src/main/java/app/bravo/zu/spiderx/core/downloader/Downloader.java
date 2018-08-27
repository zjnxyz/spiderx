package app.bravo.zu.spiderx.core.downloader;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.Task;

/**
 * 网页下载器
 * @param <REQUEST>
 * @param <RESPONSE>
 */
public interface Downloader<REQUEST, RESPONSE> {

    /**
     * 进行页面下载
     * @param task 待处理任务
     * @return page
     */
    default Page<REQUEST, RESPONSE> process(Task<REQUEST> task) {
        return null;
    }

    /**
     * 停止任务
     */
    default void shutdown() {

    }
}
