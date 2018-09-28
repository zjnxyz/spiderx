package app.bravo.zu.spiderx.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import app.bravo.zu.spiderx.http.request.PostRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.util.function.Tuples;

import static app.bravo.zu.spiderx.file.SpiderFileUtils.isImage;
import static app.bravo.zu.spiderx.file.SpiderFileUtils.isWebpImage;

/**
 * 新浪图片上传器
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/28
 */
@Slf4j
public class SinaImgFileUploader implements FileUploader {

    private final static String ACTION_17UW_URL = "https://www.17uw.cn/api/upload/weibo";

    private final static String ACTION_LYLARES_URL = "https://api.lylares.com/sinaimg/";

    @Override public String upload(File file) {
        //判断文件是否为图片
        try {
            String contentType = Files.probeContentType(file.toPath());
            if (!isImage(contentType)){
                throw new RuntimeException("新浪图库只能上传图片");
            }
            if (isWebpImage(contentType)) {
                throw new RuntimeException("新浪图库不支持webp格式图片");
            }
        } catch (IOException e) {
            log.error("文件读取异常", e);
            return null;
        }
        String imageUrl = adopt17uwUpload(file);
        if (StringUtils.isEmpty(imageUrl)) {
            //17uw上传失败，换个上传源
            log.warn("通过17uw上传失败，切换到lylares上传");
            imageUrl = adoptLylaresUpload(file);
        }
        return imageUrl;
    }

    /**
     * 通过 17uw 上传到新浪图库
     *
     * @param file 文件
     * @return String
     */
    private String adopt17uwUpload(File file) {
        PostRequest request = PostRequest.builder(ACTION_17UW_URL)
                .fileTuple(Tuples.of("iufile", "image/*", file)).build();
        return doUpload(request, UwResult.class);
    }

    /**
     * 通过 lylares 上传图片到新浪图库
     * @param file 文件
     * @return 上传后的图片地址
     */
    private String adoptLylaresUpload(File file) {
        Map<String, String> headers = new HashMap<>();
        headers.put(":authority", "api.lylares.com");
        headers.put(":method", "POST");
        headers.put(":path", "/sinaimg/");
        headers.put(":scheme", "https");
        headers.put("accept", "application/json, text/javascript, */*; q=0.01");
        headers.put("origin", "https://simg.lylares.com");
        headers.put("referer", "https://simg.lylares.com/");
        PostRequest request = PostRequest.builder(ACTION_LYLARES_URL).headers(headers)
                .fileTuple(Tuples.of("file", "image/*", file)).build();

        return doUpload(request, LylaresResult.class);
    }

    private String doUpload(PostRequest request, Class<?extends BaseResult> clz) {
        return OkHttpUtil.getClient().post(request)
                .filter(HttpResponse::isSuccess)
                .map(t -> JSON.parseObject(t.getBodyText(), clz))
                .map(t ->{
                    if (!t.isSuccess()) {
                        throw new RuntimeException("文件上传异常："+t.getMsg());
                    }
                    return t.getImageUrl();
                }).doOnError(e -> log.error("文件上传出错了, url="+request.getUrl(), e))
                .onErrorReturn("").block();
    }

    public static SinaImgFileUploader getInstance(){
        return SinaImgFileUploaderHolder.INSTANCE;
    }

    private static class SinaImgFileUploaderHolder {
        private static SinaImgFileUploader INSTANCE = new SinaImgFileUploader();
    }

    /**
     * 基础返回结果
     */
    private static abstract class BaseResult {

        /**
         * 是否上传成功
         *
         * @return boolean
         */
        abstract boolean isSuccess();

        /**
         * 上传后的图片地址
         * @return string
         */
        abstract String getImageUrl();

        /**
         * 错误信息
         *
         * @return string
         */
        abstract String getMsg();

    }

    @Data
    private static class LylaresResult extends BaseResult{

        private String msg;

        private LylaresResult.Data data;

        @lombok.Data
        private static class Data {
            private Map<String, String> images;

            String getLargeUrl() {
                return images.get("large");
            }
        }

        @Override boolean isSuccess() {
            return !"ok".equalsIgnoreCase(msg);
        }

        @Override String getImageUrl() {
            return data.getLargeUrl();
        }

    }

    @Data
    private static class UwResult extends BaseResult{

        private String code;

        private String msg;

        private Data data;

        @Override boolean isSuccess() {
            return "SUCCESS".equalsIgnoreCase(code);
        }

        @Override String getImageUrl() {
            return data.getUrl();
        }

        @lombok.Data
        private static class Data {

            private int width;

            private int height;

            private String filename;

            private long size;

            private String url;
        }
    }


}
