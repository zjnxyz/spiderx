package app.bravo.zu.spiderx.core.downloader;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.Task;
import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.client.okhttp.OkHttpClient;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * okHttp 下载器
 *
 * @author riverzu
 */
@Slf4j
public class OkHttpDownloader implements Downloader {

    private final OkHttpClient client;
    /**
     * 构造函数
     * @param site 网站信息
     */
    public OkHttpDownloader(Site site) {
        client = new OkHttpClient(site);
    }

    @Override
    public Mono<Page> process(Task task) {
        Mono<Page> mono;
        if (task == null || task.getRequest() == null){
            log.warn("task 和 request 都不能为空");
            mono = Mono.just(Page.error(task));
        }else {
            mono = client.execute(task.getRequest()).doOnError(t -> log.error("请求异常", t))
                    .onErrorReturn(HttpResponse.error())
                    .map(t -> Page.builder().status(t.getStatus()).task(task).response(t).build());
        }
        return mono;
    }
}
