package com.weds.bean.exception;

/**
 * 签名验证失败
 * @author Administrator
 *
 */
public class SignException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SignException() {
		super();
	}

	public SignException(String message) {
		super(message);
	}

	public SignException(Throwable cause) {
		super(cause);
	}

	public SignException(String message, Throwable cause) {
		super(message, cause);
	}
}
