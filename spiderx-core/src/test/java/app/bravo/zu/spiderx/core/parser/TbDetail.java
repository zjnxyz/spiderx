package app.bravo.zu.spiderx.core.parser;

import app.bravo.zu.spiderx.core.parser.bean.HtmlBean;
import app.bravo.zu.spiderx.core.parser.bean.annotation.HtmlField;
import lombok.Data;

/**
 * 类/接口注释
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/18
 */
@Data
public class TbDetail extends HtmlBean {

    @HtmlField(xPath = "//body/text()")
    private String body;


}
