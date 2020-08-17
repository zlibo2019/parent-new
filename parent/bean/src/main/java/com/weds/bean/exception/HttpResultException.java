package com.weds.bean.exception;

/**
 * HTTP请求结果异常
 *
 * @author sxm
 */
public class HttpResultException extends RuntimeException {

    public HttpResultException() {
        super();
    }

    public HttpResultException(String message) {
        super(message);
    }

    public HttpResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpResultException(Throwable cause) {
        super(cause);
    }

    protected HttpResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
