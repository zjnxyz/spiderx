package app.bravo.zu.spiderx.core.downloader;

import app.bravo.zu.spiderx.core.Page;
import app.bravo.zu.spiderx.core.Task;
import app.bravo.zu.spiderx.http.Site;
import app.bravo.zu.spiderx.http.client.okhttp.OkHttpClient;
import app.bravo.zu.spiderx.http.request.HttpRequest;
import app.bravo.zu.spiderx.http.response.HttpResponse;

public class OkHttpDownloader implements Downloader<HttpRequest, HttpResponse>{

    private final OkHttpClient client;
    /**
     * 构造函数
     * @param site 网站信息
     */
    public OkHttpDownloader(Site site) {
        client = new OkHttpClient(site);
    }

    @Override
    public Page<HttpRequest, HttpResponse> process(Task<HttpRequest> task) {
//        HttpResponse response = client.execute(task.getRequest());
        //Page<HttpRequest, HttpResponse> page = Page.builder().response(response).task(task).status(response.getStatus()).build()
        return null;
    }
}
