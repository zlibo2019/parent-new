package com.weds.bean.exception;

/**
 * HTTP请求异常
 *
 * @author sxm
 */
public class HttpRequestException extends RuntimeException {

    public HttpRequestException() {
        super();
    }

    public HttpRequestException(String message) {
        super(message);
    }

    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestException(Throwable cause) {
        super(cause);
    }

    protected HttpRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
