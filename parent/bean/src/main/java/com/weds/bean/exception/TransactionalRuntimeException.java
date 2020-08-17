package com.weds.bean.exception;

public class TransactionalRuntimeException extends RuntimeException {
	public TransactionalRuntimeException(String msg) {
		super(msg);
	}

	public TransactionalRuntimeException() {
		super();
	}
}
