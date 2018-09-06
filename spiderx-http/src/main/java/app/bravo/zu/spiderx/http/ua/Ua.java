package app.bravo.zu.spiderx.http.ua;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ua 实体
 * @author riverzu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ua {

    /**
     * 名称 default
     */
    private String name;

    /**
     * ua 值
     */
    private String value;

    /**
     * 分类
     */
    private String category;

}
