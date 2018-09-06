package app.bravo.zu.spiderx.http.ua;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

/**
 * ua选择器
 *
 * @author riverzu
 */
public class UaSelector {


    private UaResourceLoad load;

    private List<Ua> uas = new ArrayList<>();

    private Random random = new Random();

    /**
     * 默认使用的UA
     */
    private final static Ua DEFAULT_UA = Ua.builder().category("Baiduspider").name("default")
            .value("Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)")
            .build();

    /**
     * 总的ua数
     */
    private int count;

    private UaSelector(UaResourceLoad load) {
        this.load = load;
        init();
    }


    /**
     * 初始化ua数据
     */
    private void init() {
        uas = load.load();
        count = uas.size();
    }

    /**
     * 随机选择一个UA
     * @return UA
     */
    public Ua select() {
        if (CollectionUtils.isEmpty(uas)) {
            return DEFAULT_UA;
        }
        int index = random.ints(0, count).limit(1).boxed().collect(toList()).get(0);
        return uas.get(index);
    }


    /**
     * 默认实现
     * @return UaSelector
     */
    public static UaSelector def() {
        return new UaSelector(new DefaultUaResourceLoad());
    }



}
