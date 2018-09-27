package app.bravo.zu.spiderx.file;

import java.util.Map;
import java.util.UUID;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 请求参数
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/26
 */
@Data
public class Request {

    private String url;

    /**
     * 请求参数
     */
    private Map<String, String> parameters;


    private Map<String, String> headers;

    /**
     * 保存的文件名
     */
    private String fileName;

    /**
     * 获取文件名
     *
     * @return 文件名
     */
    public String getFileName() {
        if (StringUtils.isEmpty(fileName)){
            this.fileName = UUID.randomUUID().toString().replaceAll("-","");
        }
        return fileName;

    }
}
