package app.bravo.zu.spiderx.core;

import lombok.Builder;
import lombok.Data;

/**
 * 请求返回值
 * @author riverzu
 */
@Data
@Builder
public class Page<REQUEST, RESPONSE> {

    /**
     * 请求
     */
    private Task<REQUEST> task;


    /**
     * 响应状态
     */
    private int status;

    /**
     * 响应
     */
    private RESPONSE response;
}
