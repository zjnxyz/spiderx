package app.bravo.zu.spiderx.core.queue;

import app.bravo.zu.spiderx.core.Task;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 默认任务队列实现
 *
 * @author riverzu
 */
@Slf4j
public class DefaultQueue implements Queue {

    private ConcurrentLinkedQueue<Task> queue = new ConcurrentLinkedQueue<>();

    @Override
    public Task out() {
        return queue.poll();
    }

    @Override
    public void into(Task task) {
        if (task == null) {
            log.warn("task 不能为空");
            return;
        }
        queue.offer(task);
    }

    @Override
    public void into(List<Task> tasks) {
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        tasks.stream().filter(Objects::nonNull).forEach(this::into);
    }

    @Override
    public int getTotal() {
        return queue.size();
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(queue);
    }
}
