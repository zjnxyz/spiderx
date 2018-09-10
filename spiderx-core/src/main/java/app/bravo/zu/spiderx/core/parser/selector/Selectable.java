package app.bravo.zu.spiderx.core.parser.selector;

import java.util.List;

/**
 * Selectable text.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @author tony
 */
public interface Selectable {

    /**
     * select list with xpath
     *
     * @param xpath xpath
     * @return new Selectable after extract
     */
    Selectable xpath(String xpath);


    /**
     * select smart content with ReadAbility algorithm
     *
     * @return content
     */
    Selectable smartContent();

    /**
     * select all links
     *
     * @return all links
     */
    Selectable links();

    /**
     * select list with regex, default group is group 1
     *
     * @param regex regex
     * @return new Selectable after extract
     */
    Selectable regex(String regex);

    /**
     * select list with regex
     *
     * @param regex regex
     * @param group group
     * @return new Selectable after extract
     */
    Selectable regex(String regex, int group);

    /**
     * single string result
     *
     * @return single string result
     */
    String get();

    /**
     * if result exist for select
     *
     * @return true if result exist
     */
    boolean match();

    /**
     * multi string result
     *
     * @return multi string result
     */
    List<String> all();

    /**
     * extract by JSON Path expression
     *
     * @param jsonPath jsonPath
     * @return result
     */
    Selectable jsonPath(String jsonPath);

    /**
     * extract by custom selector
     *
     * @param selector selector
     * @return result
     */
    Selectable select(Selector selector);

    /**
     * extract by custom selector
     *
     * @param selector selector
     * @return result
     */
    Selectable selectList(Selector selector);

    /**
     * get all nodes
     * @return result
     */
    List<Selectable> nodes();
}
