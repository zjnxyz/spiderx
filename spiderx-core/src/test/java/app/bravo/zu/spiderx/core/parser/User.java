package app.bravo.zu.spiderx.core.parser;

import app.bravo.zu.spiderx.core.parser.bean.annotation.HtmlField;
import lombok.Data;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;

@Data
public class User {

    @HtmlField(xPath = "//id")
    private Long id;


    public static void main(String[] args) {
        Set<Field> fields = ReflectionUtils.getAllFields(User.class,
                ReflectionUtils.withAnnotation(app.bravo.zu.spiderx.core.parser.bean.annotation.HtmlField.class));

        fields.stream().filter(Objects::nonNull).forEach(t -> {
            app.bravo.zu.spiderx.core.parser.bean.annotation.Field f = t.getAnnotation(app.bravo.zu.spiderx.core.parser.bean.annotation.Field.class);
        });

    }
}
