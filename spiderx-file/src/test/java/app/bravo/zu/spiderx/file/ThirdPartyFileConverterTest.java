package app.bravo.zu.spiderx.file;

import org.junit.Test;

public class ThirdPartyFileConverterTest {

    @Test
    public void covertOnline() {
        FileConverter fileConverter = new ThirdPartyFileConverter();
        String targetUrl = fileConverter.convert("http://img.alicdn.com/imgextra/i3/TB1x40jXPgy_uJjSZSgYXHz0XXa_M2.SS2_640x640q85s150_.webp",
                "png");
        System.out.println(targetUrl);

    }
}
