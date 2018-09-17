package app.bravo.zu.spiderx.core.pipeline;

import com.alibaba.fastjson.JSON;

import app.bravo.zu.spiderx.core.SpiderContext;
import app.bravo.zu.spiderx.core.Task;
import app.bravo.zu.spiderx.core.parser.KanDy.Detail;
import lombok.extern.slf4j.Slf4j;

/**
 * 类/接口注释
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/17
 */
@Slf4j
public class KanDyDetailResultPipeline implements Pipeline<Detail> {

    @Override
    public void process(Detail detail, Task task, SpiderContext ctx) {
        log.info("task={}, detail={}", task.getRequest().getUrl(), JSON.toJSONString(detail));
    }

}
