package org.think2framework.core.exception;

/**
 * 不存在异常
 */
public class NonExistException extends RuntimeException {

	public NonExistException(String message) {
		super(message + "不存在！");
	}

	public NonExistException(Throwable cause) {
		super(cause);
	}

}
