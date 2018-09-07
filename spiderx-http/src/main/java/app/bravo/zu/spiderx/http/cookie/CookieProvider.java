package app.bravo.zu.spiderx.http.cookie;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * cookie 信息提供者
 *
 * @author riverzu
 */
public interface CookieProvider {

    /**
     * 保存cookies 信息
     *
     * @param domain domain
     * @param cookies cookies
     */
     void save(String domain, List<Cookie> cookies);

    /**
     * 保存cookie
     *
     * @param domain domain
     * @param cookieStr cookieStr
     */
     default void save(String domain, String cookieStr) {
         if (StringUtils.isEmpty(cookieStr)){
             return;
         }

         List<Cookie> cookies = Splitter.on(";").trimResults().omitEmptyStrings()
                 .splitToList(cookieStr).stream()
                 .filter(StringUtils::isNotEmpty).map(t -> {
                     List<String> elements = Splitter.on("=").omitEmptyStrings().trimResults().splitToList(t);
                     if (elements.size() == 2){
                        return new Cookie(elements.get(0), elements.get(1));
                     }
                     return null;
         }).filter(Objects::nonNull).collect(toList());
         save(domain, cookies);
     }


    /**
     * 加载cookies 信息
     *
     * @param domain domain
     * @return cookieGroup
     */
     CookieGroup load(String domain);
}
