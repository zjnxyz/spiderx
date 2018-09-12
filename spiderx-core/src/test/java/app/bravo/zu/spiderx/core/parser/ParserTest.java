package app.bravo.zu.spiderx.core.parser;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.parser.render.BeanRenderFactory;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import org.junit.Test;

public class ParserTest {


    @Test
    public void testParseTeacher() {
        String str = "<entity><id name=\"zhangSan\">123</id><students>[{\"id\":1001,\"name\":\"name1\"},{\"id\":1002,\"name\":\"name2\"}]</students></entity>";
        HttpResponse response = new HttpResponse();
        response.setBodyText(str);
        response.setStatus(200);
        Teacher teacher = (Teacher) BeanRenderFactory.instance().get(Teacher.class).inject(Teacher.class, Page.builder().response(response).build());
        System.out.println(teacher);
    }

}
