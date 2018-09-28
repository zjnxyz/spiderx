package app.bravo.zu.spiderx.http.ua;

import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * ua资源默认加载器
 *
 * @author riverzu
 */
@Slf4j
public class DefaultUaResourceLoad implements UaResourceLoad {

    /**
     * 1024
     */
    private final static int BUFFER_SIZE = 0x400;

    @Override
    public List<Ua> load() {

        return Arrays.stream(UaConfig.UA_FILES).filter(StringUtils::isNotEmpty).map(t -> {
            InputStream input = null;
            try {
                input = this.getClass().getResourceAsStream(t);
                return inputStream2String(input);
            } catch (Exception e) {
                log.error("加载ua配置出错,file=" + t, e);
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e1) {
                        log.error("关闭流出错了", e1);
                    }
                }
            }
            return null;
        }).filter(StringUtils::isNotEmpty)
                .flatMap(t -> Splitter.on("\r\n").omitEmptyStrings().trimResults().splitToList(t).stream())
                .map(t -> Ua.builder().category("cate").name("default").value(t).build())
                .collect(toList());
    }


    /**
     * 从输入流读取数据
     *
     * @param inStream instream
     * @return byte[]
     * @throws Exception exception
     */
    private static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            if (len != 0) {
                outSteam.write(buffer, 0, len);
            }
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 从输入流读取数据
     *
     * @param inStream in
     * @return string
     * @throws Exception exception
     */
    private static String inputStream2String(InputStream inStream) throws IOException {

        return new String(readInputStream(inStream), StandardCharsets.UTF_8);
    }

}
