package app.bravo.zu.spiderx.file;

import java.io.File;

import app.bravo.zu.spiderx.file.FileConverter.Format;
import org.junit.Test;

public class ThirdPartyFileConverterTest {

    @Test
    public void covertOnline() {
        FileConverter fileConverter = ThirdPartyFileConverter.getInstance();
        String targetUrl = fileConverter.convert("http://img.alicdn.com/imgextra/i3/TB1x40jXPgy_uJjSZSgYXHz0XXa_M2.SS2_640x640q85s150_.webp",
                Format.JPG);
        System.out.println(targetUrl);

    }

    @Test
    public void covertFile() {
        File file = new File(ThirdPartyFileConverterTest.class.getResource("/test.webp").getFile());
        FileConverter fileConverter = ThirdPartyFileConverter.getInstance();
        String targetUrl = fileConverter.convert(file, Format.PNG);
        System.out.println(targetUrl);
    }
}
