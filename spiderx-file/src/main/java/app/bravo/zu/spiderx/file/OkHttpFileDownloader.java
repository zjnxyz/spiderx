package app.bravo.zu.spiderx.file;

import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.client.HttpClient;
import app.bravo.zu.spiderx.http.client.okhttp.OkHttpClient;
import app.bravo.zu.spiderx.http.request.GetRequest;
import app.bravo.zu.spiderx.http.request.HttpRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static app.bravo.zu.spiderx.http.request.HttpRequest.HttpMethod.HEAD;
import static java.util.stream.Collectors.toList;

/**
 * okHttp文件下载器
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/26
 */
@Slf4j
public class OkHttpFileDownloader implements FileDownloader {

    /**
     * 5M一个block
     */
    private final static int DEFAULT_BLOCK_SIZE = 5*1024*1024;

    private OkHttpFileDownloader() {

    }

    @Override public File download(Request request, @NonNull String savePath) {
        //文件不存在时，使用临时目录作为文件保存地址
        HttpRequest headRequest = new HttpRequest(request.getUrl(), HEAD);
        if (MapUtils.isNotEmpty(request.getParameters())) {
            headRequest.setParameters(request.getParameters());
        }
        if (MapUtils.isNotEmpty(request.getHeaders())) {
            headRequest.setHeaders(request.getHeaders());
        }

        FileInfo fileInfo = OkHttpUtil.getClient().execute(headRequest).filter(HttpResponse::isSuccess)
                .map(t ->{
                    int length = Integer.parseInt(t.getHeaders().get("Content-Length"));
                    if (length <= 0) {
                        throw new RuntimeException("文件："+request.getUrl()+" 大小必须大于0");
                    }
                    MediaType mt = MediaType.parse(t.getHeaders().get("Content-Type"));
                    String fileName = savePath+File.separator+request.getFileName();
                    if (mt != null) {
                        fileName = fileName+"."+mt.subtype();
                    }
                    File file = downloadFile(request, length, fileName);
                    if (file == null) {
                        return FileInfo.fail();
                    }
                    return new FileInfo(file, true);
                }).onErrorReturn(FileInfo.fail()).log().cast(FileInfo.class).block();

        return fileInfo != null && fileInfo.getSuccess() ? fileInfo.getFile() : null;
    }

    private File downloadFile(Request request, int length, String fileName) {
        //声明需要下载的文件
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            //文件夹不存在，需要重新创建
            if (!file.getParentFile().mkdir()) {
                throw new RuntimeException(String.format("文件夹：%s 创建失败", file.getParent()));
            }
        }
        List<String> results;
        int num = length / DEFAULT_BLOCK_SIZE + 1;
        try(RandomAccessFile accessFile = new RandomAccessFile(file, "rw")) {
            accessFile.setLength(length);
            results = Flux.range(0, num).filter(t1 -> t1*DEFAULT_BLOCK_SIZE < length).parallel(4)
                    .runOn(Schedulers.parallel())
                    .map(t1 ->{
                        long start = t1*DEFAULT_BLOCK_SIZE;
                        long end = start + DEFAULT_BLOCK_SIZE > length ? length : start + DEFAULT_BLOCK_SIZE;
                        GetRequest getRequest = GetRequest.builder(request.getUrl()).build();
                        if (MapUtils.isNotEmpty(request.getParameters())) {
                            getRequest.setParameters(request.getParameters());
                        }
                        if (MapUtils.isNotEmpty(request.getHeaders())) {
                            getRequest.setHeaders(request.getHeaders());
                        }
                        log.info("url={}, blockNum={} 正在下载中...",request.getUrl(), t1);
                        getRequest.header("RANGE", "bytes=" + start + "-" + end);
                        OkHttpUtil.getClient().execute(getRequest)
                                .filter(HttpResponse::isSuccess).subscribe(response -> {
                            // 写入文件
                            try {
                                accessFile.seek(start);
                                accessFile.write(response.getBodyBytes());
                                log.info("url={}, blockNum={} 下载完成...",request.getUrl(), t1);
                            } catch (IOException e) {
                                log.error("url="+request.getUrl()+" 块："+t1+" 写入失败", e);
                                throw new RuntimeException("文件："+request.getUrl()+" 块："+t1+" 写入失败");
                            }
                        });
                        return "s";
                    }).sequential().onErrorReturn("").filter("s"::equals).collect(toList()).block();
        }catch (Exception e) {
            log.error("文件："+request.getUrl()+"下载出错", e);
            return null;
        }
        return CollectionUtils.isNotEmpty(results) && results.size() == num ? file : null;
    }

    /**
     * 初始化下载器
     * @return
     */
    public static OkHttpFileDownloader instance() {
        return OkHttpFileDownloaderHolder.instance;
    }
    private static class OkHttpFileDownloaderHolder {
        private static OkHttpFileDownloader instance = new OkHttpFileDownloader();
    }

    @Getter
    @AllArgsConstructor
    private static class FileInfo {
        private File file;

        /**
         * 文件是否下载成功
         */
        private Boolean success;

        static FileInfo fail() {
            return new FileInfo(null, false);
        }
    }


    private static class OkHttpUtil {

        private OkHttpClient client;

        private OkHttpUtil() {
            client = new OkHttpClient(Site.builder().connectTimeout(30000).build());
        }

        /**
         * 获取请求客户端
         *
         * @return HttpClient
         */
         static HttpClient getClient() {
            return OkHttpUtilHolder.UTIL.client;
        }

        private static class OkHttpUtilHolder {
            private static OkHttpUtil UTIL = new OkHttpUtil();
        }
    }
}
