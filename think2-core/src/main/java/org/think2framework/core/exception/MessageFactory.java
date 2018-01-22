package org.think2framework.core.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.think2framework.core.utils.StringUtils;

/**
 * 消息工厂,主要用于处理系统的错误和正确消息提示 错误内容中?代表需要替换为其他参数
 */
public class MessageFactory {

	private static final Logger logger = LogManager.getLogger(MessageFactory.class);

	private static final String JSON = "{\"code\": \"%s\",\"data\": \"%s\"}"; // json数据格式

	private static Map<String, String> messages = new HashMap<>(); // 系统消息

	static {
		clear();
	}

	/**
	 * 追加一条消息定义，如果编号已经存在则不追加
	 * 
	 * @param code
	 *            消息编号
	 * @param message
	 *            消息内容
	 */
	public static synchronized void append(String code, String message) {
		if (null != messages.get(code)) {
			logger.warn("消息编号[{}]已经存在，忽略追加！", code);
		} else {
			messages.put(code, message);
			logger.debug("追加消息[{}][{}]！", code, message);
		}
	}

	/**
	 * 批量追加消息定义，map的key为消息编号，value为消息内容
	 * 
	 * @param map
	 *            消息
	 */
	public static synchronized void append(Map<String, Object> map) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			append(entry.getKey(), StringUtils.toString(entry.getValue()));
		}
	}

	/**
	 * 清空消息定义，并追加系统级别消息定义
	 */
	public static synchronized void clear() {
		messages.clear();
		logger.debug("清空所有消息定义！");
		for (SystemMessage systemMessage : SystemMessage.values()) {
			append(systemMessage.getCode(), systemMessage.getMessage());
		}
	}

	/**
	 * 获取一个消息,如果带参数则将消息内容中的?用参数代替，如果消息尚未定义则返回编号
	 *
	 * @param code
	 *            消息编号
	 * @param values
	 *            参数值
	 * @return 消息
	 */
	public static String get(String code, String... values) {
		String message = messages.get(code);
		if (StringUtils.isNotBlank(message)) {
			if (null != values) {
				for (String value : values) {
					message = message.replaceFirst("\\?", value);
				}
			}
		} else {
			message = "消息编号[" + code + "]尚未定义！";
		}
		return message;
	}

	/**
	 * 获取一个消息,如果带参数则将消息内容中的?用参数代替
	 *
	 * @param code
	 *            消息编号
	 * @param values
	 *            参数值
	 * @return 消息json字符串
	 */
	public static String getJson(String code, String... values) {
		String message = get(code, values);
		return String.format(JSON, code, message);
	}

	/**
	 * 获取一个不带内容的成功消息
	 * 
	 * @return 成功消息
	 */
	public static String getJson() {
		return String.format(JSON, SystemMessage.SUCCESS.getCode(), "");
	}

	/**
	 * 获取一个成功消息
	 *
	 * @param data
	 *            消息内容
	 * @return 消息
	 */
	public static String getJson(Object data) {
		return String.format(JSON, SystemMessage.SUCCESS.getCode(), data);
	}

}
