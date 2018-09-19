package app.bravo.zu.spiderx.core.parser;

import app.bravo.zu.spiderx.core.parser.bean.JsonBean;
import app.bravo.zu.spiderx.core.parser.bean.annotation.JsonObject;
import app.bravo.zu.spiderx.core.parser.bean.annotation.JsonPath;
import lombok.Data;

import java.util.List;

@Data
public class TbItemDetail extends JsonBean {
    private static final long serialVersionUID = -8892073681673850118L;

    @JsonPath(path = "$.data.mockData")
    private String mockData;

    @JsonObject(path = "$.data.item")
    private Goods item;


    @Data
    public static class Goods extends JsonBean {

        private static final long serialVersionUID = 3295851774913789020L;

        private String itemId;

        private String title;

        private List<String> images;

        private String taobaoDescUrl;

        private String h5moduleDescUrl;

        private String categoryId;
    }
}
