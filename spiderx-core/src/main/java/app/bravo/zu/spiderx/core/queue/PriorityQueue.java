package app.bravo.zu.spiderx.core.queue;

import app.bravo.zu.spiderx.core.Task;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 带有优先级的队列
 *
 * @author riverzu
 */
public class PriorityQueue implements Queue {

    private ConcurrentLinkedQueue<Task> normalQueue = new ConcurrentLinkedQueue<>();

    private PriorityBlockingQueue<Task> priorityQueue = new PriorityBlockingQueue<>(20,
            (Task o1, Task o2) -> o1.getPriority() > o2.getPriority() ? -1 : (o1.getPriority() == o2.getPriority() ? 0 : -1)
    );

    @Override
    public Task out() {
        Task task = priorityQueue.poll();
        if (task != null) {
            return task;
        }
        return normalQueue.poll();
    }

    @Override
    public void into(Task task) {
        if (task.getPriority() > 0) {
            priorityQueue.put(task);
        } else {
            normalQueue.add(task);
        }
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
        return normalQueue.size() + priorityQueue.size();
    }

    @Override
    public List<Task> getAllTask() {
        List<Task> tasks = new ArrayList<>(priorityQueue);
        tasks.addAll(normalQueue);
        return tasks;
    }
}
