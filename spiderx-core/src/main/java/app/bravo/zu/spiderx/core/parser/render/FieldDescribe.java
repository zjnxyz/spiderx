package app.bravo.zu.spiderx.core.parser.render;

import lombok.Data;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * 字段信息
 *
 * @author riverzu
 */
public class FieldDescribe {

    /**
     * 返回值类型
     */
    private Class<?> type;

    /**
     * 字段上的注释
     */
    @Getter
    private Annotation annotation;

    /**
     * 字段名
     */
    private String name;

    @Getter
    private Field field;

    public FieldDescribe(Field field, Annotation annotation) {
        this.field = field;
        this.annotation = annotation;
    }


    public Class<?> getType() {
        if (type == null) {
            type = field.getType();
        }
        return type;
    }

    /**
     * 获取泛型信息
     *
     * @return type
     */
    public Type getGenericType() {
        return field.getGenericType();
    }

    /**
     * 获取字段名
     *
     * @return string
     */
    public String getName() {
        if (name == null) {
            name = field.getName();
        }
        return name;
    }



}
