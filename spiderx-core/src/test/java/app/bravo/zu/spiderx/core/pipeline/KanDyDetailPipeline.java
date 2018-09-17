package app.bravo.zu.spiderx.core.pipeline;

import app.bravo.zu.spiderx.core.Spider;
import app.bravo.zu.spiderx.core.SpiderContext;
import app.bravo.zu.spiderx.core.Task;
import app.bravo.zu.spiderx.core.parser.KanDy;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class KanDyDetailPipeline implements Pipeline<KanDy> {

    @Override
    public void process(KanDy kanDy, Task task, SpiderContext ctx) {
        if (CollectionUtils.isEmpty(kanDy.getItems())) {
            return;
        }
        Spider subSpider = ctx.getSubSpider().getOrDefault("kanDy_spider_detail",
                Spider.create("kanDy_spider_detail").initialDelay(1000));
        Task[] tasks = kanDy.getItems().stream().filter(Objects::nonNull).map(t -> {
            Task subTask = task.clone(t.getDetailUrl());
            Map<String, Object> map = new HashMap<>();
            map.put("item", t);
            subTask.setExtras(map);
            return subTask;
        }).toArray(Task[]::new);
        if (subSpider.isRunning()) {

        }
    }
}
