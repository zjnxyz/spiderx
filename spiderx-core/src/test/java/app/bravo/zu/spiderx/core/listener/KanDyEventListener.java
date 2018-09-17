package app.bravo.zu.spiderx.core.listener;

import app.bravo.zu.spiderx.core.EventListener;
import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.SpiderContext;
import app.bravo.zu.spiderx.core.Task;
import lombok.extern.slf4j.Slf4j;

/**
 * 类/接口注释
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/17
 */
@Slf4j
public class KanDyEventListener implements EventListener {

    @Override
    public void beforeDownload(Task task, SpiderContext ctx) {
        log.info("task={}, before download", task.getUuid());
    }

    @Override
    public void afterDownload(Page page, SpiderContext ctx) {
        log.info("task={}, after download", page.getTask().getUuid());
    }
}
