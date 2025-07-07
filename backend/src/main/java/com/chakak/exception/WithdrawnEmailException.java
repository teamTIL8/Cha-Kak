package com.chakak.exception;

public class WithdrawnEmailException extends RuntimeException {
	public WithdrawnEmailException(String message) {
		super(message);
	}

	public WithdrawnEmailException(String message, Throwable cause) {
		super(message, cause);
	}
}
