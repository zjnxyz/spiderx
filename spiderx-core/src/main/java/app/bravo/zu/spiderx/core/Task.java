package app.bravo.zu.spiderx.core;

import app.bravo.zu.spiderx.http.request.HttpRequest;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;
/**
 * 抓取任务
 *
 * @author riverzu
 */
@Data
@Builder
public class Task implements Cloneable{

    /**
     * 任务id
     */
    private String uuid;

    /**
     * 请求
     */
    private HttpRequest request;

    /**
     * 任务额外数据
     */
    private Map<String, Object> extras;

    /**
     * 任务优先级
     */
    private int priority;

    /**
     * 任务链接
     *
     * @return url
     */
    public String getUrl() {
        return request.getUrl();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    public Task clone(String url) {
        try {
            Task task = (Task) clone();
            task.setUuid(UUID.randomUUID().toString());
            HttpRequest clone = (HttpRequest) request.clone();
            clone.referer(clone.getUrl());
            task.setRequest(clone);
            return task;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
