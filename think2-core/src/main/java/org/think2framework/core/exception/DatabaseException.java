package org.think2framework.core.exception;

/**
 * 系统预定义的带有错误编号的异常，返回json字符串到前端
 */
public class DatabaseException extends RuntimeException {

	public DatabaseException(Throwable cause) {
		super(cause);
	}

	public DatabaseException(String message) {
		super(message);
	}
}
