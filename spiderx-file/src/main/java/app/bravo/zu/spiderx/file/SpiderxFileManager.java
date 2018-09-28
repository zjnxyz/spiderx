package app.bravo.zu.spiderx.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import com.alibaba.fastjson.JSON;

import app.bravo.zu.spiderx.file.FileConverter.Format;
import app.bravo.zu.spiderx.http.request.GetRequest;
import app.bravo.zu.spiderx.http.request.HttpRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import static app.bravo.zu.spiderx.file.SpiderFileUtils.calculateFileHash;
import static app.bravo.zu.spiderx.file.SpiderFileUtils.isImage;
import static app.bravo.zu.spiderx.file.SpiderFileUtils.isWebpImage;
import static app.bravo.zu.spiderx.http.request.HttpRequest.HttpMethod.HEAD;

/**
 * 文件管理
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/28
 */
@Slf4j
@Builder
public class SpiderxFileManager {

    /**
     * 文件下载
     */
    private volatile FileDownloader downloader;

    /**
     * 文件上传
     */
    private volatile FileUploader uploader;


    private FileDownloader getDownloader() {
        if (downloader == null) {
            synchronized (this) {
                if (downloader == null) {
                    downloader = OkHttpFileDownloader.instance();
                }
            }
        }
        return downloader;
    }

    private FileUploader getUploader() {
        if (uploader == null) {
            synchronized (this) {
                if (uploader == null) {
                    uploader = SinaImgFileUploader.getInstance();
                }
            }
        }
        return uploader;
    }


    /**
     * 下载文件
     *
     * @param request 请求参数
     * @param savePath 文件保存地址
     * @return 文件
     */
    public File download(Request request, String savePath) {
        return getDownloader().download(request, savePath);
    }

    /**
     * 下载文件
     *
     * @param request 请求
     * @return file
     */
    public File download(Request request) {
        return getDownloader().download(request);
    }

    /**
     * 下载文件
     *
     * @param url 文件的url
     * @param savePath 文件的保存地址
     * @return 下载好的文件
     */
    public File download(String url, String savePath) {
        return getDownloader().download(url, savePath);
    }

    /**
     * 下载文件
     *
     * @param url 文件地址
     * @return 下载好的文件
     */
    public File download(String url) {
        return getDownloader().download(url);
    }

    public UpdatedInfo upload(File file) {
        log.info("fileName={}，准备上传文件", file.getAbsolutePath());
        try {
            String contentType = Files.probeContentType(file.toPath());
            if (getUploader() instanceof SinaImgFileUploader) {
                if (isWebpImage(contentType)){
                    //webp 图片需要先转换为png格式
                    return upload(ThirdPartyFileConverter.getInstance().convert(file, Format.PNG));
                }
            }
            String url = getUploader().upload(file);
            if (StringUtils.isEmpty(url)) {
                log.warn("文件：{}， 上传失败", file.getAbsolutePath());
                return null;
            }
            UpdatedInfo info = UpdatedInfo.builder().url(url).contentType(contentType).size(file.length())
                    .hash(calculateFileHash(file)).build();
            if (info.isImage()) {
                //获取宽高
                BufferedImage image = ImageIO.read(file);
                info.setHeight(image.getHeight());
                info.setWidth(image.getWidth());
            }
            log.info("fileName={},文件上传成功，result={}", file.getAbsolutePath(), JSON.toJSONString(info));
            return info;
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.warn("fileName={},文件上传失败", file.getAbsolutePath());
        return null;
    }

    public UpdatedInfo upload(String url) {
        log.info("url={}, 准备下载文件", url);
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("文件地址不能为空");
        }
        if (!url.startsWith("http")) {
            throw new RuntimeException("错误的文件地址："+url);
        }
        if (getUploader() instanceof SinaImgFileUploader) {
            //检查是否为webp文件
            HttpRequest headRequest = new HttpRequest(url, HEAD);
            Boolean flag = OkHttpUtil.getClient().execute(headRequest).filter(HttpResponse::isSuccess)
                    .map(t -> isWebpImage(t.getContentType())).block();
            log.info("url={}, 检查是否为webp文件：{}", url, flag);
            if (flag!= null && flag){
               // 转换成png格式
                return upload(ThirdPartyFileConverter.getInstance().convert(url, Format.PNG));
            }
        }
        File file = null;
        try {
            log.info("url={}, 准备下载文件...", url);
            file = download(url);
            if (file == null) {
                log.warn("url={}, 文件不存在", url);
                return null;
            }
            log.info("url={}, 文件下载成功, 临时存放本地，fileName={}", url, file.getAbsolutePath());
            return upload(file);
        }finally {
           if (file != null){
               log.debug("删除临时文件：{}", file.getAbsolutePath());
               getDownloader().clear(file);
           }
        }

    }


    @Data
    @Builder
    public static class UpdatedInfo {

        private String url;

        /**
         * 文件hash值
         */
        private String hash;

        private String contentType;

        private long size;

        private int width;

        private int height;

        /**
         * 判断是否为图片
         * @return boolean
         */
        public boolean isImage() {
            return SpiderFileUtils.isImage(contentType);
        }

    }

}
