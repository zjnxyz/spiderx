package app.bravo.zu.spiderx.file;

import java.io.File;

/**
 * 文件转换器
 *
 * @author riverzu
 */
public interface FileConverter {


    /**
     * 转换
     *
     * @param file         文件
     * @param targetFormat 目标格式
     * @return 转换后的文件地址
     */
    String convert(File file, String targetFormat);

    /**
     * 转换
     *
     * @param url          原文件地址
     * @param targetFormat 目标格式
     * @return 转换的文件地址
     */
    String convert(String url, String targetFormat);
}
