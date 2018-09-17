package app.bravo.zu.spiderx.http;

import java.util.concurrent.TimeUnit;

import app.bravo.zu.spiderx.http.client.chrome.ChromeHeadlessClient;
import app.bravo.zu.spiderx.http.request.GetRequest;
import io.webfolder.cdp.ChromiumDownloader;
import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.SessionFactory;
import org.junit.Test;

/**
 * 类/接口注释
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/17
 */
public class HeadLessTest {

    @Test
    public void test1() {
        Launcher launcher = new Launcher();
        SessionFactory sessionFactory = launcher.launch();

        //Session session = sessionFactory.create();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //session.close();
        //sessionFactory.close();
        launcher.kill();
    }


    @Test
    public void test2() throws InterruptedException {
        ChromeHeadlessClient client = new ChromeHeadlessClient(Site.builder().build());
        client.execute(GetRequest.builder("https://www.oschina.net").build()).subscribe(System.out::println);
        System.out.println("================================================");
        TimeUnit.SECONDS.sleep(5);
        client.execute(GetRequest.builder("https://www.csdn.net").build()).subscribe(System.out::println);

        client.close();
    }
}
