package app.bravo.zu.spiderx.core.queue;

import app.bravo.zu.spiderx.core.Task;

import java.util.List;

/**
 * 任务队列
 *
 * @author riverzu
 */
public interface Queue {

    /**
     * 任务出队列
     *
     * @return task
     */
    Task out();

    /**
     * 单个任务入队列
     *
     * @param task 任务
     */
    void into(Task task);

    /**
     * 批量任务入队列
     *
     * @param tasks 任务列表
     */
    void into(List<Task> tasks);

    /**
     * 获取总任务数
     *
     * @return int
     */
    int getTotal();

    /**
     * 获取所有任务任务
     *
     * @return list
     */
    List<Task> getAllTask();
}
