package app.bravo.zu.spiderx.core;

import lombok.Data;

/**
 * 抓取任务
 * @author riverzu
 */
@Data
public class Task<T> {

    /**
     * 任务id
     */
    private String uuid;

    /**
     * 请求
     */
    private T request;

    /**
     * 任务优先级
     */
    private int priority;
}
