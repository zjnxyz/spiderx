package app.bravo.zu.spiderx.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * 文件下载接口
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/26
 */
public interface FileDownloader {

    /**
     * 下载文件
     *
     * @param request 请求参数
     * @param savePath 文件保存地址
     * @return 文件
     */
    File download(Request request, String savePath);

    /**
     * 下载文件
     *
     * @param request 请求
     * @return file
     */
    default File download(Request request) {
        return download(request, getTmpPath());
    }

    /**
     * 下载文件
     *
     * @param url 文件的url
     * @param savePath 文件的保存地址
     * @return 下载好的文件
     */
    default File download(String url, String savePath) {
        Request request = new Request();
        request.setUrl(url);
        return download(request, savePath);
    }

    /**
     * 下载文件
     *
     * @param url 文件地址
     * @return 下载好的文件
     */
    default File download(String url) {
        return download(url, getTmpPath());
    }

    /**
     * 获取临时目录地址
     *
     * @return 目录地址
     */
    default String getTmpPath() {
        return System.getProperty("java.io.tmpdir");
    }



    /**
     * 清除下载好的文件
     *
     * @param savePath 文件路径
     */
    default void clear(String savePath) {
        clear(new File(savePath));
    }

    /**
     * 清除下载后的文件
     *
     * @param file 文件
     */
    default void clear(File file) {
        if (file.isDirectory()) {
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                throw new RuntimeException("目录："+ file.getAbsolutePath()+"/" + file.getName() +" 删除失败");
            }
            return;
        }
        if (file.isFile() && file.exists()) {
            //是文件并且存在，删除
            if (FileUtils.deleteQuietly(file)) {
                throw new RuntimeException("文件："+file.getAbsolutePath()+"/"+file.getName()+" 删除失败");
            }
        }

    }


}
