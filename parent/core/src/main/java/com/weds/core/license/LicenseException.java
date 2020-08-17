package com.weds.core.license;

public class LicenseException extends RuntimeException {

    private static final long serialVersionUID = -4907874187783245132L;

    public LicenseException() {
    }

    public LicenseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LicenseException(String message) {
        super(message);
    }

    public LicenseException(Throwable cause) {
        super(cause);
    }
}
