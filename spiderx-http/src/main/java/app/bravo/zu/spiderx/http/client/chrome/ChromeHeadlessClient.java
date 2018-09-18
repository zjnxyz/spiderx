package app.bravo.zu.spiderx.http.client.chrome;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.util.Collections;
import java.util.OptionalInt;
import java.util.Random;

import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.client.HttpClient;
import app.bravo.zu.spiderx.http.request.HttpRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;
import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.event.network.RequestIntercepted;
import io.webfolder.cdp.event.network.ResponseReceived;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import io.webfolder.cdp.type.network.Response;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static io.webfolder.cdp.event.Events.NetworkRequestIntercepted;
import static io.webfolder.cdp.event.Events.NetworkResponseReceived;
import static io.webfolder.cdp.session.SessionFactory.DEFAULT_PORT;
import static java.lang.System.getProperty;

/**
 * 封装chrome headless 请求
 *
 * @author jiangnan.zjn
 * @createDate 2018/9/17
 */
@Slf4j
public class ChromeHeadlessClient implements HttpClient {


    /**
     * 浏览器工厂
     */
    private final SessionFactory factory;

    private Session session;

    private final Site site;


    public ChromeHeadlessClient(Site site) {
        this.site = site;
        int port = getAvailablePort();
        Launcher launcher = new Launcher(port);
        Path path = java.nio.file.Paths.get(getProperty("java.io.tmpdir"))
            .resolve("remote-profile-" + port);
        factory = launcher.launch(Collections.singletonList("--user-data-dir=" + path.toString()));
        session = factory.create();
    }

    @Override
    public Mono<HttpResponse> execute(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        try(Session session = factory.create()){
            session.getCommand().getNetwork().enable();
            session.addEventListener((e, o) -> {
                if (NetworkRequestIntercepted.equals(e)) {
                    RequestIntercepted ri = (RequestIntercepted) o;
                    boolean isRedirect = ri.getRedirectUrl() != null && !ri.getRedirectUrl().isEmpty();
                    if (isRedirect) {
                        log.info("RedirectUrl={}, statusCode={}", ri.getRedirectUrl(), ri.getResponseStatusCode());
                    }
                    session.getCommand().getNetwork().continueInterceptedRequest(ri.getInterceptionId());
                }
            });
            session.addEventListener((e, o) ->{
                if (NetworkResponseReceived.equals(e)) {
                    ResponseReceived rr = (ResponseReceived) o;
                    Response resp = rr.getResponse();
                    if (!resp.getUrl().startsWith("http")){
                        log.warn("url={}, 返回值异常， 非http协议", resp.getUrl());
                    }
                    response.setStatus(resp.getStatus());
                    resp.getHeaders().forEach((k,v) -> response.header(k, v.toString()));
                }
            });
            session.navigate(request.getUrl());
            session.waitDocumentReady();
            response.setBodyText(session.getContent());
        }
        return Mono.just(response);
    }

    @Override
    public void close() {
        if (session != null) {
            session.close();
        }

        if (factory != null) {
            factory.close();
        }
    }

    /**
     * 获取可用的端口
     *
     * @return int
     */
    private static int getAvailablePort() {
        //tcp最大端口号 65535
        OptionalInt first = new Random().ints(9223, 55535).limit(1).findFirst();
        return selectAvailablePort(first.orElse(DEFAULT_PORT));
    }

    /**
     * 选择一个可用的 端口
     *
     * @param port 原始端口
     * @return int
     */
    private static int selectAvailablePort(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            return selectAvailablePort(port + 1);
        }
    }
}
