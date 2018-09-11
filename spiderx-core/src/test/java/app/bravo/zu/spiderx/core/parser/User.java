package app.bravo.zu.spiderx.core.parser;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.parser.bean.HtmlBean;
import app.bravo.zu.spiderx.core.parser.bean.annotation.HtmlField;
import app.bravo.zu.spiderx.core.parser.render.html.HtmlBeanRender;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import lombok.Data;

@Data
public class User extends HtmlBean {

    private static final long serialVersionUID = 6805565818377724487L;

    @HtmlField(xPath = "//id/text()")
    private Long id;

    @HtmlField(xPath = "//address")
    private Address address;


    public static void main(String[] args) {
        HtmlBeanRender render = new HtmlBeanRender();
        HttpResponse response = new HttpResponse();
        response.setBodyText("<entry><id>123</id><address><addr>北京</addr></address></entry>");
        response.setStatus(200);
        User user = (User) render.inject(User.class, Page.builder().response(response).build());
        System.out.println(user);

    }


    @Data
    public static class Address extends HtmlBean {

        private static final long serialVersionUID = -4327788080441573201L;

        @HtmlField(xPath = "//addr/text()")
        private String addr;
    }
}
