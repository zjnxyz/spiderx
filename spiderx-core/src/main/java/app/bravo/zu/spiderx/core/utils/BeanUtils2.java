package app.bravo.zu.spiderx.core.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.ContextClassLoaderLocal;
import org.apache.commons.beanutils.converters.DateConverter;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 * beanUtils 帮助类
 *
 * @author riverzu
 */
public class BeanUtils2 extends BeanUtils{

    private static final ContextClassLoaderLocal<BeanUtilsBean2>
            BEANS2_BY_CLASSLOADER = new ContextClassLoaderLocal<BeanUtilsBean2>() {
        @Override
        protected BeanUtilsBean2 initialValue() {
            BeanUtilsBean2 beanUtilsBean2 = new BeanUtilsBean2();
            //设置日期转换器
            DateConverter dateConverter = new DateConverter();
            dateConverter.setPattern("yyyy-MM-dd HH:mm:ss");
            beanUtilsBean2.getConvertUtils().register(dateConverter, Date.class);
            return beanUtilsBean2;
        }
    };

    private static BeanUtilsBean getInstance() {
        return BEANS2_BY_CLASSLOADER.get();
    }


    /**
     * 将对象解析成map
     *
     * @param bean bean
     * @return map
     * @throws IllegalAccessException ex
     * @throws InvocationTargetException ex
     * @throws NoSuchMethodException ex
     */
    public static Map<String, String> describe(final Object bean)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return getInstance().describe(bean);
    }

    /**
     * 进行类型转换
     *
     * @param value 值
     * @param type 目标类型
     * @return Object
     */
    public static Object convert(final Object value, final Class<?> type){
        return getInstance().getConvertUtils().convert(value, type);
    }

}
