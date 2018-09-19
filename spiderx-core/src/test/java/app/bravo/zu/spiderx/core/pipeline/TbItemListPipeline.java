package app.bravo.zu.spiderx.core.pipeline;

import app.bravo.zu.spiderx.core.Spider;
import app.bravo.zu.spiderx.core.SpiderContext;
import app.bravo.zu.spiderx.core.Task;
import app.bravo.zu.spiderx.core.parser.TbItemDetail;
import app.bravo.zu.spiderx.core.parser.TbItemList;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Slf4j
public class TbItemListPipeline implements Pipeline<TbItemList> {

    private final static String DETAIL_URL = "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?appKey=12574478&sign=ef22a6dc765bd6ce86d36e2ba9a6cc33&ttid=2017%40taobao_h5_6.6.0&AntiCreep=true&jsv=2.4.8api=mtop.taobao.detail.getdetail&v=6.0&dataType=jsonp&type=jsonp&callback=mtopjsonp2&data=%7B%22itemNumId%22%3A%22item_id%22%7D";

    public final static String LIST_URL = "https://s.m.taobao.com/search?q=%E5%B9%B3%E5%BA%95%E9%95%BF%E9%9D%B4%E5%A5%B3%E6%98%A5%E7%A7%8B&search=%E6%8F%90%E4%BA%A4&tab=all&sst=1&n=20&buying=buyitnow&m=api4h5&abtest=29&wlsort=29";

    @Override
    public void process(TbItemList tbItemList, Task task, SpiderContext ctx) {
        if (CollectionUtils.isEmpty(tbItemList.getListItem())) {
            log.warn("商品列表信息为空");
            return;
        }

        log.info("当前列表数据：{}", JSON.toJSONString(tbItemList));

        Spider subSpider = ctx.getSubSpider().getOrDefault("tb_goods_detail_spider",
                Spider.create("tb_goods_detail_spider", TbItemDetail.class)
                        .pipeline(new ConsolePipeline()).initialDelay(2000));

        List<Task> tasks = tbItemList.getListItem().stream().filter(Objects::nonNull).map(t -> {
            String url = DETAIL_URL.replace("item_id", t.getItemId());
            return task.clone(url);
        }).collect(toList());
        subSpider.addTasks(tasks);
        if (!subSpider.isRunning()) {
            subSpider.run();
        }
        Task subTask = task.clone(LIST_URL);
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(tbItemList.getPage() + 1));
        subTask.getRequest().setParameters(params);
        ctx.getQueue().into(subTask);
    }
}
