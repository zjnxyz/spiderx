package app.bravo.zu.spiderx.core.parser;

import app.bravo.zu.spiderx.core.parser.bean.HtmlBean;
import app.bravo.zu.spiderx.core.parser.bean.annotation.HtmlField;
import app.bravo.zu.spiderx.core.parser.bean.annotation.Next;
import app.bravo.zu.spiderx.core.parser.bean.annotation.RequestExtra;
import lombok.Data;

import java.util.List;

@Data
public class KanDy extends HtmlBean {

    private static final long serialVersionUID = -8526469294522620817L;

    @HtmlField(xPath = "//ul[@id='contents']/li")
    private List<Item> items;

    @HtmlField(xPath = "//ul[@class='pagination']/li[15]/a/@href")
    @Next
    private String nextPageUrl;


    @Data
    public static class Item extends HtmlBean {

        private static final long serialVersionUID = 6701557253714062621L;

        @HtmlField(xPath = "//li/a/@title")
        private String title;

        @HtmlField(xPath = "//li/a/img/@src")
        private String imageUrl;

        @HtmlField(xPath = "//li/a/@href")
        private String detailUrl;

        @HtmlField(xPath = "//li/a/label[@class='score']/text()")
        private String score;

        @HtmlField(xPath = "//li/div[@class='info']/p[@class='type']/a/text()")
        private List<String> tags;

    }


    @Data
    public static class Detail extends HtmlBean {

        private static final long serialVersionUID = 75645940943212619L;

        @HtmlField(xPath = "//ul[@class='player_list']/a/@href")
        private List<String> players;

        @RequestExtra(value = "item")
        private Item item;

    }
}
