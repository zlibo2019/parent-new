package com.weds.bean.jwt;

public class JwtInvalidException extends RuntimeException {
    public JwtInvalidException() {
        super();
    }

    public JwtInvalidException(String message) {
        super(message);
    }

    public JwtInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtInvalidException(Throwable cause) {
        super(cause);
    }

    protected JwtInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
