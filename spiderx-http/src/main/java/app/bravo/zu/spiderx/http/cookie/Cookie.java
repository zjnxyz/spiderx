package app.bravo.zu.spiderx.http.cookie;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * cookie 信息
 *
 * @author riverzu
 */
@Data
@AllArgsConstructor
public class Cookie {

    private String name;

    private String value;

}
