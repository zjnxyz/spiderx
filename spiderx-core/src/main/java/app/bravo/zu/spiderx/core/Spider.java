package app.bravo.zu.spiderx.core;

import app.bravo.zu.spiderx.core.downloader.Downloader;
import app.bravo.zu.spiderx.core.downloader.OkHttpDownloader;
import app.bravo.zu.spiderx.core.parser.Parser;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import app.bravo.zu.spiderx.core.pipeline.Pipeline;
import app.bravo.zu.spiderx.core.queue.DefaultQueue;
import app.bravo.zu.spiderx.core.queue.Queue;
import app.bravo.zu.spiderx.core.utils.CountableThreadPool;
import app.bravo.zu.spiderx.core.utils.UrlUtils;
import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.request.GetRequest;
import app.bravo.zu.spiderx.http.request.HttpRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 爬虫
 *
 * @author riverzu
 */
@Slf4j
public class Spider {

    /**
     * 初始化
     */
    private final static int STAT_INIT = 0;

    /**
     * 已启动
     */
    private final static int STAT_RUNNING = 1;

    /**
     * 停止
     */
    private final static int STAT_STOPPED = 4;

    private AtomicInteger stat = new AtomicInteger(STAT_INIT);

    /**
     * 爬虫名字
     */
    @Getter
    private String name;

    /**
     * 爬虫数量
     */
    private int workerNum = 1;

    /**
     * 间隔时间
     */
    private long initialDelay = 0;

    /**
     * 流水线
     */
    private Set<Pipeline> pipelines = new LinkedHashSet<>();

    private Set<EventListener> listeners = new LinkedHashSet<>();

    /**
     * 任务队列
     */
    @Getter
    private Queue queue;

    /**
     * 下载器 对应class
     */
    @Getter
    private Class<? extends Downloader> downloaderClz;

    @Getter
    private SpiderContext ctx;

    /**
     * 待爬取网站信息
     */
    @Getter
    private Site site;


    /**
     * 完成后退出状态
     */
    private boolean exitWhenComplete = true;

    private ReentrantLock newUrlLock = new ReentrantLock();

    private Condition newUrlCondition = newUrlLock.newCondition();

    /**
     * 结果对象
     */
    @Getter
    private Class<? extends SpiderBean> clz;

    /**
     * 工作池
     */
    private CountableThreadPool workerPool;

    /**
     * 下载器
     */
    private List<Downloader> downloaders = new CopyOnWriteArrayList<>();

    private Spider(String name, Class<? extends SpiderBean> clz, Queue queue) {
        checkArgument(StringUtils.isNotEmpty(name), "爬虫名称不能为空");
        this.name = name;
        this.clz = clz == null ? SpiderBean.class : clz;
        this.queue = queue == null ? new DefaultQueue() : queue;
        this.ctx = SpiderContext.instance(this.queue);
        this.downloaderClz = OkHttpDownloader.class;
    }

    /**
     * 创建爬虫
     *
     * @param name      爬虫名称
     * @param renderClz 结果渲染的class
     * @return Spider
     */
    public static Spider create(String name, Class<? extends SpiderBean> renderClz) {
        return create(name, renderClz, null);
    }

    /**
     * 创建爬虫
     *
     * @param name      爬虫名称
     * @param renderClz 结果渲染的class
     * @param queue     任务队列
     * @return Spider
     */
    public static Spider create(String name, Class<? extends SpiderBean> renderClz, Queue queue) {
        return new Spider(name, renderClz, queue);
    }


    public Spider taskQueue(Queue queue) {
        checkIfRunning();
        if (this.queue.getTotal() > 0) {
            //将旧任务队列中的数据移动到新队列中
            queue.into(this.queue.getAllTask());
        }
        this.queue = queue;
        return this;
    }

    public Spider site(Site site) {
        checkIfRunning();
        this.site = site;
        return this;
    }

    /**
     * 下载器
     *
     * @param downloaderClz downloaderClz
     * @return spider
     */
    public Spider downloaderClz(Class<? extends Downloader> downloaderClz) {
        checkIfRunning();
        this.downloaderClz = downloaderClz;
        return this;
    }

    public Spider pipelines(Collection<Pipeline> pipelines) {
        checkIfRunning();
        this.pipelines.addAll(pipelines);
        return this;
    }

    public Spider pipeline(Pipeline pipeline) {
        checkIfRunning();
        this.pipelines.add(pipeline);
        return this;
    }


    public Spider listeners(Collection<EventListener> listeners) {
        checkIfRunning();
        this.listeners.addAll(listeners);
        return this;
    }

    public Spider listener(EventListener listener) {
        checkIfRunning();
        this.listeners.add(listener);
        return this;
    }


    /**
     * 工作线程数
     *
     * @param workerNum 线程数
     * @return spider
     */
    public Spider workerNum(int workerNum) {
        checkArgument(workerNum > 0, "worker 必须大于0");
        this.workerNum = workerNum;
        return this;
    }

    /**
     * 爬虫启动延迟时间
     *
     * @param delay delay
     * @return Spider
     */
    public Spider initialDelay(long delay) {
        checkArgument(delay > 0, "delay 必须大于0");
        this.initialDelay = delay;
        return this;
    }

    public Spider url(String... urls) {
        checkArgument(urls != null, "urls不能为空");
        HttpRequest[] requests = Arrays.stream(urls).filter(StringUtils::isNotEmpty)
                .map(t -> GetRequest.builder(t).build())
                .toArray(HttpRequest[]::new);
        request(requests);
        return this;
    }

    public Spider request(HttpRequest... requests) {
        checkArgument(requests != null, "请求不能为空");
        if (site == null || StringUtils.isEmpty(site.getDomain())) {
            HttpRequest request = requests[0];
            if (site == null) {
                site = Site.builder().build();
            }
            site.setDomain(UrlUtils.getDomain(request.getUrl()));
        }

        //加入任务
        Arrays.stream(requests)
                .filter(Objects::nonNull).peek(t -> t.setSite(site))
                .map(t -> Task.builder().uuid(UUID.randomUUID().toString()).request(t).build())
                .forEach(queue::into);
        return this;
    }

    public Spider addTasks(List<Task> tasks) {
        checkArgument(CollectionUtils.isNotEmpty(tasks), "爬虫任务不能为空");
        if (site == null || StringUtils.isEmpty(site.getDomain())) {
            Task task = tasks.get(0);
            HttpRequest request = task.getRequest();
            site = request.getSite();
            if (site == null) {
                site = Site.builder().domain(UrlUtils.getDomain(request.getUrl())).build();
            }
        }

        queue.into(tasks);
        return this;
    }


    /**
     * 执行爬虫任务
     */
    public void run() {
        //设置运行状态
        checkRunningStat();
        log.info("spider [{}] 已启动", name);
        workerPool = new CountableThreadPool(workerNum);
        SpiderContext ctx = SpiderContext.instance(queue);

        while (!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {
            final Task task = queue.out();
            if (task == null) {
                log.debug("spider={}, alive worker 数量={}",name, workerPool.getThreadAlive());
                if (workerPool.getThreadAlive() == 0 && exitWhenComplete) {
                    break;
                }
                waitNewUrl();
            } else {
                //间隔时间
                initialDelay();
                workerPool.execute(() -> {
                    try {
                        this.notifyObserver(listener -> listener.beforeDownload(task, ctx));
                        getDownloader().process(task)
                                .map(t ->{
                                    this.notifyObserver(listener -> listener.afterDownload(t, ctx));
                                    return Parser.instance().parse(clz, t, ctx);
                                }).subscribe(t -> pipelines.forEach(pipeline -> pipeline.process(t, task, ctx)),
                                        e -> log.error("数据爬取异常", e)
                                );
                    } catch (Exception e) {
                        log.warn(String.format("spider=%s,爬取任务执行异常", name), e);
                    } finally {
                        signalNewUrl();
                    }
                });
                //等待子爬虫完成任务
                waitSubSpiderCompleted();
            }
        }

        completed();

        log.info("spider {} completed", name);
    }

    /**
     * 爬虫任务已完成
     */
    private void completed() {
        //任务完成，爬虫结束
        stat.set(STAT_STOPPED);
        //关闭
        downloaders.forEach(Downloader::shutdown);
    }

    /**
     * 获取下载器
     *
     * @return downloader
     */
    private synchronized Downloader getDownloader() {
        if (CollectionUtils.isEmpty(downloaders)) {
            IntStream.range(0, workerNum).boxed()
                    .map(t -> initDownloader()).filter(Objects::nonNull)
                    .forEach(downloaders::add);
        }
        Optional<Downloader> optional = downloaders.stream().filter(Downloader::isCompeted).findFirst();
        if (!optional.isPresent()) {
            Downloader downloader = initDownloader();
            downloaders.add(downloader);
            return downloader;
        }
        return optional.get();
    }

    private Downloader initDownloader() {
        log.debug("initDownloader------");
        try {
            Constructor<? extends Downloader> ctor = downloaderClz.getDeclaredConstructor(Site.class);
            return ctor.newInstance(site);
        } catch (Exception e) {
            log.error("初始化下载器异常", e);
        }
        return null;
    }


    /**
     * 等到子爬虫执行完成
     */
    private void waitSubSpiderCompleted() {
        if (MapUtils.isEmpty(ctx.getSubSpider())) {
            return;
        }
        while (true) {
            log.info("检查子爬虫是否执行完成");
            Optional<Spider> sub = ctx.getSubSpider().values().stream()
                    .filter(t -> !t.isCompleted()).findFirst();
            if (sub.isPresent()) {
                log.info("子爬虫 {}，还未执行完成", sub.get().name);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                log.info("爬虫 {} 全部任务都已执行完成", name);
                break;
            }
        }
    }

    /**
     * 通知观察者
     *
     * @param consumer 观察者
     */
    private void notifyObserver(Consumer<EventListener> consumer) {
        listeners.forEach(consumer);
    }


    /**
     * 检查爬虫是否已经启动
     */
    private void checkIfRunning() {
        if (stat.get() == STAT_RUNNING) {
            throw new IllegalStateException("Spider is already running!");
        }
    }

    private void checkRunningStat() {
        while (true) {
            int statNow = stat.get();
            if (statNow == STAT_RUNNING) {
                throw new IllegalStateException("Spider is already running!");
            }
            if (stat.compareAndSet(statNow, STAT_RUNNING)) {
                break;
            }
        }
    }

    private void waitNewUrl() {
        newUrlLock.lock();
        try {
            //double check
            if (workerPool.getThreadAlive() == 0 && exitWhenComplete) {
                return;
            }
            newUrlCondition.await(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.warn("waitNewUrl - interrupted, error {}", e);
        } finally {
            newUrlLock.unlock();
        }
    }

    private void initialDelay() {
        if (initialDelay > 0) {
            try {
                //为了每次休眠时间不一样，做一个随机值
                long max = initialDelay + initialDelay/2;
                Optional<Long> optional =new Random().longs(initialDelay/2, max)
                    .limit(1).boxed().findFirst();
                TimeUnit.MILLISECONDS.sleep(optional.orElse(initialDelay));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void signalNewUrl() {
        try {
            newUrlLock.lock();
            newUrlCondition.signalAll();
        } finally {
            newUrlLock.unlock();
        }
    }


    /**
     * 判断爬虫任务是否完成
     *
     * @return boolean
     */
    public boolean isCompleted() {
        return stat.get() == STAT_STOPPED;
    }

    /**
     * 是否在运行中
     *
     * @return boolean
     */
    public boolean isRunning() {
        return stat.get() == STAT_RUNNING;
    }

}
