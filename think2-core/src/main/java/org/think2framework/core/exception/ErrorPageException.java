package org.think2framework.core.exception;

/**
 * 系统预定义的错误页面，返回到一个错误页面
 */
public class ErrorPageException extends RuntimeException {

	private String message; // 异常消息

	public ErrorPageException(String code, String... values) {
		this.message = MessageFactory.createJsonMessage(code, values);
	}

	@Override
	public String getMessage() {
		return message;
	}
}
