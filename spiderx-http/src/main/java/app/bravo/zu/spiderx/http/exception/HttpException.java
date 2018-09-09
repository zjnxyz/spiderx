package app.bravo.zu.spiderx.http.exception;

/**
 * 异常统一基类
 *
 * @author riverzu
 */
public class HttpException extends RuntimeException{

    private static final long serialVersionUID = -8150564043503614912L;

    public HttpException(String msg) {
        super(msg);
    }

    public HttpException(String msg, Throwable ex){
        super(msg, ex);
    }

}
