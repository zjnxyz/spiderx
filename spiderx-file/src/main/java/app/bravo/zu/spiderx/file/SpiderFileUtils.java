package app.bravo.zu.spiderx.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;

/**
 * 帮助方法
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/28
 */
public class SpiderFileUtils {

    private final static String IMAGE_TYPE_WEBP = "webp";

    private final static String MEDIA_TYPE_IMAGE = "image";

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 是否为webp格式图片
     * @param contentType contentType
     * @return boolean
     */
     static boolean isWebpImage(String contentType) {
        return StringUtils.isNotEmpty(contentType) && contentType.endsWith(IMAGE_TYPE_WEBP);
    }

    /**
     * 是否为图片
     *
     * @param contentType contentType
     * @return boolean
     */
     static boolean isImage(String contentType) {
        return StringUtils.isNotEmpty(contentType) && contentType.startsWith(MEDIA_TYPE_IMAGE);
    }

    /**
     * 计算文件hash值
     *
     * @param file 文件
     * @return string
     */
    static String calculateFileHash(File file) {
        try(FileInputStream in = new FileInputStream(file)) {
            FileChannel ch = in.getChannel();
            return calculateHash(ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String calculateHash(ByteBuffer buffer) {
        String s = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buffer);
            byte[] tmp = md.digest();
            // 用字节表示就是 16 个字节
            char[] str = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                // 逻辑右移，将符号位一起右移
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            s = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }


}
