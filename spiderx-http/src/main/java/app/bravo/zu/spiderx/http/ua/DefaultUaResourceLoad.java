package app.bravo.zu.spiderx.http.ua;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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
                    log.warn(String.format("加载ua配置文件 name=%s 失败", t.getFileName().toString()),e);
                }
            });
        } catch (IOException e) {
            log.warn(String.format("文件夹 path=%s 未找到", path),e);
        }
        return uas;
    }

}
