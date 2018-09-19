package app.bravo.zu.spiderx.core.parser;

import app.bravo.zu.spiderx.core.parser.bean.JsonBean;
import app.bravo.zu.spiderx.core.parser.bean.annotation.JsonObject;
import app.bravo.zu.spiderx.core.parser.bean.annotation.RequestParameter;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class TbItemList extends JsonBean {

    private static final long serialVersionUID = 510484351217214146L;

    @JsonObject(path = "$.listItem")
    private List<ItemList> listItem;

    @RequestParameter(value = "page")
    private int page;


    @Data
    public static class ItemList extends JsonBean {

        private static final long serialVersionUID = -265935662731832364L;

        private String title;

        private String sold;

        private String auctionURL;

        @JSONField(name = "item_id")
        private String itemId;

        private ShopInfo shopInfo;
    }


    @Data
    public static class ShopInfo extends JsonBean {

        private static final long serialVersionUID = 973478086294617206L;

        private String icon;

        private String url;
    }
}
