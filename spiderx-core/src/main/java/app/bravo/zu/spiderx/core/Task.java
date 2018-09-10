package app.bravo.zu.spiderx.core;

import app.bravo.zu.spiderx.http.request.HttpRequest;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

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

}
