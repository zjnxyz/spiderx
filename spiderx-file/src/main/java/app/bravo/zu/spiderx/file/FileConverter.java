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
    String convert(File file, Format targetFormat);

    /**
     * 转换
     *
     * @param url          原文件地址
     * @param targetFormat 目标格式
     * @return 转换的文件地址
     */
    String convert(String url, Format targetFormat);

    /**
     * 格式
     */
    enum Format {

        /**
         * png 图片格式
         */
        PNG("png"),

        /**
         * jpg图片
         */
        JPG("jpg");

        private String name;

        Format(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
