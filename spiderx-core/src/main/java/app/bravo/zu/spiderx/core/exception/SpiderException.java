package app.bravo.zu.spiderx.core.exception;

/**
 * 爬虫异常信息
 *
 * @author riverzu
 */
public class SpiderException extends RuntimeException {

    private static final long serialVersionUID = -7275935780755604073L;

    public SpiderException(String msg) {
        super(msg);
    }

    public SpiderException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
