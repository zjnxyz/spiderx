package app.bravo.zu.spiderx.core.parser.selector;

/**
 * Convenient methods for selectors.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @author tony
 */
public abstract class Selectors {

    public static RegexSelector regex(String expr) {
        return new RegexSelector(expr);
    }

    public static RegexSelector regex(String expr, int group) {
        return new RegexSelector(expr,group);
    }

    public static SmartContentSelector smartContent() {
        return new SmartContentSelector();
    }

    public static XpathSelector xpath(String expr) {
        return new XpathSelector(expr);
    }

}