package com.weds.bean.jwt;

public class JwtHeaderException extends RuntimeException {
    public JwtHeaderException() {
        super();
    }

    public JwtHeaderException(String message) {
        super(message);
    }

    public JwtHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtHeaderException(Throwable cause) {
        super(cause);
    }

    protected JwtHeaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
