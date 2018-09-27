package app.bravo.zu.spiderx.file;


import java.io.File;

import okhttp3.MediaType;
import org.junit.Test;

public class OkHttpFileDownloaderTest {

    @Test
    public void testDownloadFile() {
        FileDownloader downloader = OkHttpFileDownloader.instance();
        File file = downloader.download("http://img.alicdn.com/imgextra/i3/TB1x40jXPgy_uJjSZSgYXHz0XXa_M2.SS2_640x640q85s150_.webp");
        System.out.println(file.getAbsolutePath());
    }

    @Test
    public void testDownloadVideo() {
        FileDownloader downloader = OkHttpFileDownloader.instance();
        File file = downloader.download("https://cloud.video.taobao.com/play/u/475769551/p/2/e/6/t/1/50076330063.mp4");
        System.out.println(file.getAbsolutePath());
    }
}
