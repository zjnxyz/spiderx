package app.bravo.zu.spiderx.http.ua;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * ua资源默认加载器
 *
 * @author riverzu
 */
public class DefaultUaResourceLoad implements UaResourceLoad {

    @Override
    public List<Ua> load() {
        List<Ua> uas = new ArrayList<>();
        String path = DefaultUaResourceLoad.class.getResource("/").getPath()+"ua/";
        try {
            Files.list(Paths.get(path)).forEach(t ->  {
                        String category = t.getFileName().toString().replace(".txt","");
                try {
                    Files.lines(t).forEach(t1 ->
                            uas.add(Ua.builder().category(category).name("default").value(t1).build())
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uas;
    }


    public static void main(String[] args) {
        DefaultUaResourceLoad load = new DefaultUaResourceLoad();
        System.out.println(load.load());
    }

}
