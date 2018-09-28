package app.bravo.zu.spiderx.file;

import java.io.File;

/**
 * 文件上传器
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/27
 */
public interface FileUploader {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 上传后的路径
     */
    String upload(File file);

}
