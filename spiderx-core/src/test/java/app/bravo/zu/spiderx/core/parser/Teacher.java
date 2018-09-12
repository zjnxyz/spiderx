package app.bravo.zu.spiderx.core.parser;

import app.bravo.zu.spiderx.core.parser.bean.HtmlBean;
import app.bravo.zu.spiderx.core.parser.bean.JsonBean;
import app.bravo.zu.spiderx.core.parser.bean.annotation.HtmlField;
import app.bravo.zu.spiderx.core.parser.bean.annotation.JsonObject;
import lombok.Data;

import java.util.List;

@Data
public class Teacher extends HtmlBean {

    private static final long serialVersionUID = -3258811977721403819L;

    @HtmlField(xPath = "//id/text()")
    private long id;

    @HtmlField(xPath = "//id/@name")
    private String name;

    @HtmlField(xPath = "//students/text()")
    private StudentList studentList;


    @Data
    public static class StudentList extends JsonBean {

        @JsonObject(path = "*")
        private List<Student> list;

    }


    @Data
    public static class Student extends JsonBean {

        private static final long serialVersionUID = 6748979275587483426L;

        private Long id;

        private String name;

        private Integer sex;
    }

}
