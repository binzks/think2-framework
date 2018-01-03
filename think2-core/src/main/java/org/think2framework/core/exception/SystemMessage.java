package org.think2framework.core.exception;

/**
 * 系统级别消息
 */
public enum SystemMessage {

	SUCCESS("0", "系统保留的成功编号"), UNKNOWN("9", "系统保留的未知异常编号");

	private String code;

	private String message;

	SystemMessage(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
