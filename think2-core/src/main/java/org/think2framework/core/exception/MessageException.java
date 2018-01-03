package org.think2framework.core.exception;

/**
 * 系统预定义的带有错误编号的异常，返回json字符串到前端
 */
public class MessageException extends RuntimeException {

	private String message; // 异常消息

	public MessageException(String code, String... values) {
		this.message = MessageFactory.getJson(code, values);
	}

	@Override
	public String getMessage() {
		return message;
	}
}
