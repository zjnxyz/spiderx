package app.bravo.zu.spiderx.core.pipeline;

import app.bravo.zu.spiderx.core.SpiderContext;
import app.bravo.zu.spiderx.core.Task;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import com.alibaba.fastjson.JSON;

/**
 * 控制台输出
 *
 * @author riverzu
 */
public class ConsolePipeline implements Pipeline<SpiderBean> {

    @Override
    public void process(SpiderBean bean, Task task, SpiderContext ctx) {
        System.out.println(JSON.toJSONString(bean));
    }

}
