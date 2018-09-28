package app.bravo.zu.spiderx.core.parser.render;

import java.io.File;
import java.util.Collections;
import java.util.List;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.exception.SpiderException;
import app.bravo.zu.spiderx.core.parser.bean.SpiderBean;
import app.bravo.zu.spiderx.core.parser.bean.annotation.Media;
import app.bravo.zu.spiderx.core.parser.bean.annotation.Media.Location;
import app.bravo.zu.spiderx.core.utils.ReflectUtils;
import app.bravo.zu.spiderx.file.SpiderxFileManager;
import app.bravo.zu.spiderx.file.SpiderxFileManager.UpdatedInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import static app.bravo.zu.spiderx.core.utils.ReflectUtils.getGenericClass;
import static app.bravo.zu.spiderx.core.utils.ReflectUtils.haveSuperType;
import static java.util.stream.Collectors.toList;

/**
 * 媒体类型注入
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/28
 */
@Slf4j
public class MediaBeanFieldRender implements BeanFieldRender {

    private final static String JSON_PATH_START_TAG = "$";

    private final static String HTML_XPATH_START_TAG = "//";

    @Override public void doRender(Page page, BeanMap beanMap, FieldDescribe fieldDescribe) {
        Media media = (Media) fieldDescribe.getAnnotation();
        //检查是否为string
        Class<?> type = fieldDescribe.getType();
        if (type.isArray() && !haveSuperType(type.getComponentType(), String.class)) {
            log.error("fieldName={} 错误使用注解@Media", fieldDescribe.getName());
            throw new SpiderException("@Media只能注解在String 、List<String> 和 String[] 上");
        }else if (haveSuperType(type, List.class)
                && !haveSuperType(getGenericClass(fieldDescribe.getGenericType(), 0), String.class)) {
            log.error("fieldName={} 错误使用注解@Media", fieldDescribe.getName());
            throw new SpiderException("@Media只能注解在String 、List<String> 和 String[] 上");
        }else if (!haveSuperType(type, String.class)) {
            log.error("fieldName={} 错误使用注解@Media", fieldDescribe.getName());
            throw new SpiderException("@Media只能注解在String 、List<String> 和 String[] 上");
        }
        List<String> list = null;
        if (media.path().startsWith(JSON_PATH_START_TAG)) {
            //json
            if (type.isArray() || haveSuperType(type, List.class)) {
                list = page.getJson().jsonPath(media.path()).all();
            } else {
                String value = page.getJson().jsonPath(media.path()).get();
                list = StringUtils.isNotEmpty(value) ? Collections.singletonList(value)
                        : Collections.emptyList();
            }
        } else if (media.path().startsWith(HTML_XPATH_START_TAG)) {
            //HTML
            if (type.isArray() || haveSuperType(type, List.class)) {
                list = page.getHtml().xpath(media.path()).all();
            } else {
                String value = page.getHtml().xpath(media.path()).get();
                list = StringUtils.isNotEmpty(value) ? Collections.singletonList(value)
                        : Collections.emptyList();
            }
        }
        if (CollectionUtils.isEmpty(list)){
            log.info("fieldName={}, path={},未找到有效数据", fieldDescribe.getName(), media.path());
            return;
        }

        SpiderxFileManager manager = SpiderxFileManager.builder().build();
        //进行图片下载
        List<String> targetUrls = Flux.just(list.toArray(new String[0])).map(t ->{
            if (t.startsWith("http")) {
                return t;
            }else if (t.startsWith("//")) {
                return "http"+t;
            }else {
                return media.imageDomain()+t;
            }
        }).parallel(Runtime.getRuntime().availableProcessors())
                .runOn(Schedulers.parallel()).map(t -> {
                    if (media.location() == Location.LOCAL) {
                        File file = manager.download(t);
                        if (file == null) {
                            return null;
                        }else {
                            return file.getAbsolutePath();
                        }
                    } else {
                        UpdatedInfo info = manager.upload(t);
                        if (info == null) {
                            return "";
                        }
                        return info.getUrl();
                    }
        }).filter(StringUtils::isNotEmpty).sequential().collect(toList()).block();

        if (CollectionUtils.isEmpty(targetUrls)) {
            return;
        }
        if (type.isArray()) {
            beanMap.put(fieldDescribe.getName(), targetUrls.toArray());
        }else if (haveSuperType(type, List.class)) {
            beanMap.put(fieldDescribe.getName(), targetUrls);
        } else {
            beanMap.put(fieldDescribe.getName(), targetUrls.get(0));
        }

    }
}
