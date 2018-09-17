package app.bravo.zu.spiderx.core.pipeline;

import app.bravo.zu.spiderx.core.SpiderContext;
import app.bravo.zu.spiderx.core.Task;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * 输出到日志
 *
 * @author riverzu
 */
@Slf4j
public class LogPipeline implements Pipeline<SpiderBean> {

    @Override
    public void process(SpiderBean bean, Task task, SpiderContext ctx) {
        log.info(JSON.toJSONString(bean));
    }
}
