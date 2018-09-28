package app.bravo.zu.spiderx.file;

import app.bravo.zu.spiderx.file.SpiderxFileManager.UpdatedInfo;
import org.junit.Test;

/**
 * 类/接口注释
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/28
 */
public class SpiderxFileManagerTest {

    @Test
    public void testUpload() {
        UpdatedInfo info =SpiderxFileManager.builder().build()
                .upload("http://img.alicdn.com/imgextra/i3/TB1x40jXPgy_uJjSZSgYXHz0XXa_M2.SS2_640x640q85s150_.webp");
        System.out.println(info);
    }

    @Test
    public void testUpload2() {
        UpdatedInfo info =SpiderxFileManager.builder().build()
                .upload("https://img.alicdn.com/imgextra/i3/1705240549/O1CN011FvTtgDf2OB6wFf-1705240549.jpg");
        System.out.println(info);
    }
}
