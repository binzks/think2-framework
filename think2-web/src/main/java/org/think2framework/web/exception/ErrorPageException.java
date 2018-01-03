package org.think2framework.web.exception;

import org.think2framework.core.exception.MessageFactory;

/**
 * 系统预定义的错误页面，返回到一个错误页面
 */
public class ErrorPageException extends RuntimeException {

	private String message; // 异常消息

	public ErrorPageException(String code, String... values) {
		this.message = MessageFactory.getJson(code, values);
	}

	@Override
	public String getMessage() {
		return message;
	}

//} catch (DuplicateKeyException e) {// 如果是唯一性约束错误，转化为中文
//		String message = e.getMessage();
//		throw new DatabaseException(
//		"数据" + StringUtils.substring(message, StringUtils.indexOf(message, "Duplicate entry '") + 17,
//		StringUtils.indexOf(message, "' for key")) + "已存在！");
//		}
}
