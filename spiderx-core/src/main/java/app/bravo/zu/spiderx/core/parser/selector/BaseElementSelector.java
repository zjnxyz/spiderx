package app.bravo.zu.spiderx.core.parser.selector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com
 * @author tony
 */
public abstract class BaseElementSelector implements Selector, ElementSelector {

    @Override
    public String select(String text) {
        if (text != null) {
            return select(Jsoup.parse(text));
        }
        return null;
    }

    @Override
    public List<String> selectList(String text) {
        if (text != null) {
            return selectList(Jsoup.parse(text));
        } else {
            return new ArrayList<>();
        }
    }

    public Element selectElement(String text) {
        if (text != null) {
            return selectElement(Jsoup.parse(text));
        }
        return null;
    }

    public List<Element> selectElements(String text) {
        if (text != null) {
            return selectElements(Jsoup.parse(text));
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 选择元素
     *
     * @param element element
     * @return element
     */
    public abstract Element selectElement(Element element);

    /**
     * 选择多个元素
     *
     * @param element element
     * @return list
     */
    public abstract List<Element> selectElements(Element element);

    /**
     * 是否有其他
     *
     * @return boolean
     */
    public abstract boolean hasAttribute();

}
