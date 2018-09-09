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
         List<String> l = Splitter.on(";").trimResults().omitEmptyStrings()
                 .splitToList(cookieStr);
         System.out.println(l);

         List<Cookie> cookies = Splitter.on(";").trimResults().omitEmptyStrings()
                 .splitToList(cookieStr).stream()
                 .filter(StringUtils::isNotEmpty).map(t -> {
                     int index = t.indexOf("=");
                     if (index == -1) {
                         return null;
                     }
                     return new Cookie(t.substring(0, index), t.substring(index+1, t.length()));
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
