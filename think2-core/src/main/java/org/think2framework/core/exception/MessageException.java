package org.think2framework.core.exception;

/**
 * 系统预定义的带有错误编号的异常，返回json字符串到前端
 */
public class MessageException extends RuntimeException {

	private String message; // 异常消息

	/**
	 * 抛出一个未知的异常
	 * 
	 * @param cause
	 *            异常
	 */
	public MessageException(Throwable cause) {
		this.message = MessageFactory.getJson(SystemMessage.UNKNOWN.getCode(), cause.getMessage());
	}

	/**
	 * 抛出一个自定义异常
	 * 
	 * @param code
	 *            异常编号
	 * @param values
	 *            异常参数值
	 */
	public MessageException(String code, String... values) {
		this.message = MessageFactory.getJson(code, values);
	}

	@Override
	public String getMessage() {
		return message;
	}
}
