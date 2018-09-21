package app.bravo.zu.spiderx.core.downloader;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.Task;
import app.bravo.zu.spiderx.core.exception.SpiderException;
import app.bravo.zu.spiderx.http.client.HttpClient;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 下载器抽象实现
 *
 * @author riverzu
 */
@Slf4j
public abstract class AbstractDownloader implements Downloader {

    private AtomicBoolean atomic = new AtomicBoolean(true);

    private final HttpClient httpClient;

    AbstractDownloader(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Mono<Page> process(Task task) {
        if (!atomic.compareAndSet(true, false)) {
            throw new SpiderException("OkHttpDownloader running....");
        }
        Mono<Page> mono;
        if (task == null || task.getRequest() == null) {
            log.warn("task 和 request 都不能为空");
            mono = Mono.just(Page.error(task));
        } else {
            log.info("uuid={} url={} 准备 download", task.getUuid(), task.getUrl());
            mono = httpClient.execute(task.getRequest())
                    .doOnError(t -> log.error(String.format("uuid=%s, url=%s, 执行下载时异常", task.getUuid(), task.getUrl()), t))
                    .onErrorReturn(HttpResponse.error())
                    .map(t -> Page.builder().status(t.getStatus()).task(task).response(t).build());
        }
        // 下载完成
        atomic.compareAndSet(false, true);
        return mono;
    }

    @Override
    public void shutdown() {
        httpClient.close();
    }

    @Override
    public boolean isCompeted() {
        return atomic.get();
    }

}
