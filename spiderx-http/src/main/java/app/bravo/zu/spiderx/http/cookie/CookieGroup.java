package app.bravo.zu.spiderx.http.cookie;

import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 *
 * @author riverzu
 */
@Getter
@ToString
public class CookieGroup {

    /**
     * domain
     */
    private String domain;

    /**
     * cookies
     */
    private Set<Cookie> cookies;

    public CookieGroup(String domain) {
        this.domain = domain;
        this.cookies = new LinkedHashSet<>();
    }

    /**
     * 添加单个cookie
     *
     * @param name name
     * @param value value
     */
    public void putCookie(String name, String value) {
        putCookie(new Cookie(name, value));
    }

    /**
     * 添加单个 cookie
     *
     * @param cookie cookie
     */
    public void putCookie(Cookie cookie) {
        this.cookies.add(cookie);
    }

    /**
     * 批量添加cookie
     *
     * @param cookies cookies
     */
    public void putCookies(Collection<Cookie> cookies) {
        this.cookies.addAll(cookies);
    }

    /**
     * 移除单个cookie
     *
     * @param cookie cookie
     */
    public void removeCookie(Cookie cookie) {
        this.cookies.remove(cookie);
    }

    /**
     * 清空cookie
     */
    public void clearCookie() {
        this.cookies.clear();
    }

    /**
     * 获取cookie的字符串
     *
     * @return string
     */
    public String getCookieString() {
        if (CollectionUtils.isEmpty(cookies)){
            return null;
        }

        List<String> list = this.cookies.stream().filter(Objects::nonNull)
                .map(t -> t.getName() +"=" +t.getValue())
                .collect(toList());

        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        return Joiner.on(";").skipNulls().join(list);
    }

}
