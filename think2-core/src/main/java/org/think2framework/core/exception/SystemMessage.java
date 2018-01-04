package org.think2framework.core.exception;

/**
 * 系统级别消息
 */
public enum SystemMessage {

	SUCCESS("0", "?"), UNKNOWN("9", "?");

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
