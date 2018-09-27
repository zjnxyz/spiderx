package app.bravo.zu.spiderx.file;

import app.bravo.zu.spiderx.http.request.PostRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.util.function.Tuple3;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用第三方转换器
 * https://s4.aconvert.com/convert/convert-batch-win.php
 * https://www.aconvert.com/cn/image/
 *
 * @author riverzu
 */
@Slf4j
public class ThirdPartyFileConverter implements FileConverter {

    private final static String ACTION_URL = "https://s4.aconvert.com/convert/convert-batch-win.php";

    @Override
    public String convert(File file, String targetFormat) {
        Map<String, String> parameters = new HashMap<>(5);
        parameters.put("targetformat", targetFormat);
        parameters.put("code", "83000");
        parameters.put("filelocation", "online");
        PostRequest request = PostRequest.builder(ACTION_URL).headers(getHeaders())
                .parameters(parameters).fileTuple(new Tuple3<>("file", Files.probeContentType(Paths.get(file.getAbsolutePath())))).build();
        return null;
    }

    @Override
    public String convert(String url, String targetFormat) {
        Map<String, String> parameters = new HashMap<>(5);
        parameters.put("file", url);
        parameters.put("targetformat", targetFormat);
        parameters.put("code", "83000");
        parameters.put("filelocation", "online");
        PostRequest request = PostRequest.builder(ACTION_URL).headers(getHeaders())
                .parameters(parameters).build();
        ConvertResult result = OkHttpUtil.getClient().post(request)
                .filter(HttpResponse::isSuccess)
                .map(t -> JSON.parseObject(t.getBodyText(), ConvertResult.class)).block();
        if (result == null) {
            return null;
        }
        return result.getTargetUrl();
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>(10);
        headers.put("Host", "s4.aconvert.com");
        headers.put("Origin", "https://www.aconvert.com");
        headers.put("Referer", "https://www.aconvert.com/cn/image/");
        return headers;
    }

    @Data
    private static class ConvertResult {

        private final static String SUCCESS_STATE = "SUCCESS";

        @JSONField(name = "filename")
        private String fileName;

        private String server;

        private String state;

        private String ext;

        public String getTargetUrl() {
            if (!SUCCESS_STATE.equals(state)) {
                log.warn("文件转换失败");
                return null;
            }
            return "http://s" + server + ".aconvert.com/convert/p3r68-cdx67/" + fileName;
        }
    }
}
