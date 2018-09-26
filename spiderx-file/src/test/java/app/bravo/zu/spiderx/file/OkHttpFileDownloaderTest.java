package app.bravo.zu.spiderx.file;

import org.junit.Test;

public class OkHttpFileDownloaderTest {

    @Test
    public void testDownloadFile() {
        FileDownloader downloader = new OkHttpFileDownloader();
        downloader.download("http://img.alicdn.com/imgextra/i3/TB1x40jXPgy_uJjSZSgYXHz0XXa_M2.SS2_640x640q85s150_.webp");
    }
}
