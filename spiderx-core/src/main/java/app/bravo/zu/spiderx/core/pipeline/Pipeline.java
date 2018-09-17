package app.bravo.zu.spiderx.core.pipeline;

import app.bravo.zu.spiderx.core.SpiderContext;
import app.bravo.zu.spiderx.core.Task;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;

/**
 * 处理流水线
 *
 * @author riverzu
 */
public interface Pipeline<T extends SpiderBean> {

    /**
     * 处理
     *
     * @param t    参数
     * @param task 当前任务
     * @param ctx  爬虫 上下文
     */
    void process(T t, Task task, SpiderContext ctx);

}
