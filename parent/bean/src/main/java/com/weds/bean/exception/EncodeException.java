package com.weds.bean.exception;

/**
 * 加解密异常
 * @author Administrator
 *
 */
public class EncodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EncodeException() {
		super();
	}

	public EncodeException(String message) {
		super(message);
	}

	public EncodeException(Throwable cause) {
		super(cause);
	}

	public EncodeException(String message, Throwable cause) {
		super(message, cause);
	}
}
