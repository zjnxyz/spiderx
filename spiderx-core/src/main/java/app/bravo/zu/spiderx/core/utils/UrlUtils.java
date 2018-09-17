package app.bravo.zu.spiderx.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * url 工具类
 *
 * @author riverzu
 */
public class UrlUtils {

    private static Pattern patternForProtocol = Pattern.compile("[\\w]+://");

    public static String getHost(String url) {
        String host = url;
        int i = StringUtils.ordinalIndexOf(url, "/", 3);
        if (i > 0) {
            host = StringUtils.substring(url, 0, i);
        }
        return host;
    }

    public static String getDomain(String url) {
        String domain = removeProtocol(url);
        int i = StringUtils.indexOf(domain, "/", 1);
        if (i > 0) {
            domain = StringUtils.substring(domain, 0, i);
        }
        return removePort(domain);
    }

    private static String removePort(String domain) {
        int portIndex = domain.indexOf(":");
        if (portIndex != -1) {
            return domain.substring(0, portIndex);
        } else {
            return domain;
        }
    }

    private static String removeProtocol(String url) {
        return patternForProtocol.matcher(url).replaceAll("");
    }

}
