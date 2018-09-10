package app.bravo.zu.spiderx.core.parser.bean;

import app.bravo.zu.spiderx.core.Task;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 抽象实体类
 *
 * @author riverzu
 */
public abstract class AbstractSpiderBean implements SpiderBean {

    private static final long serialVersionUID = -6168651595871527287L;

    @Getter
    private List<Task> targetTasks = new CopyOnWriteArrayList<>();


    public void addTask(Task task) {
        targetTasks.add(task);
    }

    public void addTasks(List<Task> tasks) {
        targetTasks.addAll(tasks);
    }

}
