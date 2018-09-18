package app.bravo.zu.spiderx.core.pipeline;

import app.bravo.zu.spiderx.core.SpiderContext;
import app.bravo.zu.spiderx.core.Task;
import app.bravo.zu.spiderx.core.parser.KanDy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

@Slf4j
public class KanDyDetailPipeline implements Pipeline<KanDy> {

    @Override
    public void process(KanDy kanDy, Task task, SpiderContext ctx) {
        if (CollectionUtils.isEmpty(kanDy.getItems())) {
            return;
        }

        log.info("task={}, result={}", task.getRequest().getUrl(), JSON.toJSONString(kanDy));

//        Spider subSpider = ctx.getSubSpider().getOrDefault("kanDy_spider_detail",
//                Spider.create("kanDy_spider_detail", Detail.class)
//                    .pipeline(new KanDyDetailResultPipeline()).workerNum(3).initialDelay(1000));
//
//        List<Task> tasks = kanDy.getItems().stream().filter(Objects::nonNull).map(t -> {
//            Task subTask = task.clone(t.getDetailUrl());
//            Map<String, Object> map = new HashMap<>();
//            map.put("item", t);
//            subTask.setExtras(map);
//            return subTask;
//        }).collect(toList());
//        subSpider.addTasks(tasks);
//
//        if (!subSpider.isRunning()) {
//            subSpider.run();
//        }
    }
}
