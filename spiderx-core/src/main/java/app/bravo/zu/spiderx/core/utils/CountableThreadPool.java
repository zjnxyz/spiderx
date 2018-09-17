package app.bravo.zu.spiderx.core.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 工作线程池
 *
 * @author riverzu
 */
@Slf4j
public class CountableThreadPool {

    private int threadNum;

    private AtomicInteger threadAlive = new AtomicInteger();

    private ReentrantLock reentrantLock = new ReentrantLock();

    private Condition condition = reentrantLock.newCondition();

    public CountableThreadPool(int threadNum) {
        this(threadNum, null);
    }

    public CountableThreadPool(int threadNum, ExecutorService executorService) {
        this.threadNum = threadNum;
        if (executorService == null) {
            ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("spider-thread-%d").setDaemon(true).build();
            executorService = new ThreadPoolExecutor(threadNum, threadNum, 30, TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(1024), factory);
        }
        this.executorService = executorService;
    }


    public int getThreadAlive() {
        return threadAlive.get();
    }

    public int getThreadNum() {
        return threadNum;
    }

    private ExecutorService executorService;

    /**
     * 任务执行
     *
     * @param runnable 线程
     */
    public void execute(final Runnable runnable) {
        if (threadAlive.get() >= threadNum) {
            try {
                reentrantLock.lock();
                while (threadAlive.get() >= threadNum) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        log.warn("执行异常", e);
                    }
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        threadAlive.incrementAndGet();
        executorService.execute(() -> {
            try {
                runnable.run();
            } finally {
                try {
                    reentrantLock.lock();
                    threadAlive.decrementAndGet();
                    condition.signal();
                } finally {
                    reentrantLock.unlock();
                }
            }
        });
    }

    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
