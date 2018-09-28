package app.bravo.zu.spiderx.file;

import java.io.File;

import org.junit.Test;

/**
 * 类/接口注释
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/28
 */
public class SinaImgFileUploaderTest {

    @Test
    public void testUploadImage() {
        File file = new File(ThirdPartyFileConverterTest.class.getResource("/IMG_4203.JPG").getFile());
        String url = SinaImgFileUploader.getInstance().upload(file);
        System.out.println(url);
    }

    @Test
    public void testUploadWebp() {
        File file = new File(ThirdPartyFileConverterTest.class.getResource("/test.webp").getFile());
        String url = SinaImgFileUploader.getInstance().upload(file);
        System.out.println(url);
    }
}
