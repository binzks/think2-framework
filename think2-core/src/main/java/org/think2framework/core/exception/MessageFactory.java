package org.think2framework.core.exception;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.think2framework.core.exception.bean.Message;
import org.think2framework.core.utils.FileUtils;
import org.think2framework.core.utils.JsonUtils;
import org.think2framework.core.utils.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息工厂,主要用于处理系统的错误和正确消息提示 错误内容中?代表需要替换为其他参数
 */
public class MessageFactory {

	private static final Logger logger = LogManager.getLogger(MessageFactory.class);

	public static final String CODE_UNKNOWN = "9"; // 未知错误编号

	public static final String CODE_SUCCESS = "0"; // 成功编号

	private static String messageFiles; // 消息配置文件

	private static Map<String, String> messages = new HashMap<>(); // 系统消息

	/**
	 * 设置消息配置文件，如果第一次设置则加载配置
	 *
	 * @param message
	 *            配置文件
	 */
	public static synchronized void setMessage(String message) {
		if (StringUtils.isBlank(messageFiles)) {
			messageFiles = message;
			logger.debug("Set message files {}", messageFiles);
			reload();
		}
	}

	/**
	 * 重新加载消息配置文件
	 */
	public static synchronized void reload() {
		if (StringUtils.isBlank(messageFiles)) {
			return;
		}
		File[] files = FileUtils.getFiles(messageFiles);
		if (null == files) {
			return;
		}
		messages.clear();
		logger.debug("All messages cleared successfully");
		appendDefault();
		for (File file : files) {
			List<Message> messages = JsonUtils.readFile(file, new TypeReference<List<Message>>() {
			});
			for (Message message : messages) {
				append(message);
			}
		}
		logger.debug("Reload message config file {}", messageFiles);
	}

	/**
	 * 追加默认通用的消息
	 */
	private static void appendDefault() {
		//默认通用错误定义
		append(new Message(CODE_SUCCESS, "系统保留的成功编号"));
		append(new Message(CODE_UNKNOWN, "系统保留的未知异常编号"));
		append(new Message("00101", "请求失败，对象[?]未获得签名授权！"));
		append(new Message("00102", "请求格式错误，未正确获取关键参数！"));
		append(new Message("00103", "请求已失效，请重新生成系统请求！"));
		append(new Message("00104", "签名校验失败！"));
	}

	/**
	 * 追加一个消息,如果已经存在则抛出异常
	 *
	 * @param message
	 *            消息
	 */
	public static synchronized void append(String code, String message) {
		if (null != messages.get(code)) {
			logger.warn("Message code {} already exists, ignore add", code);
		} else {
			messages.put(code, message);
			logger.debug("Append message {}", JsonUtils.toString(message));
		}
	}

	/**
	 * 获取一个成功消息
	 *
	 * @param data
	 *            消息内容
	 * @return 消息
	 */
	public static Message createSuccessMessage(Object data) {
		Message message = new Message();
		message.setCode(CODE_SUCCESS);
		message.setData(data);
		return message;
	}

	/**
	 * 获取一个json字符串的成功消息
	 *
	 * @param data
	 *            消息内容
	 * @return json字符串
	 */
	public static String createSuccessJsonMessage(Object data) {
		Message message = createSuccessMessage(data);
		return JsonUtils.toString(message);
	}

	/**
	 * 获取一个消息,如果带参数则将消息内容中的?用参数代替
	 *
	 * @param code
	 *            消息编号
	 * @param values
	 *            参数值
	 * @return 消息
	 */
	public static Message createMessage(String code, String... values) {
		Message message = new Message();
		message.setCode(code);
		Message msg = messages.get(code);
		if (null == msg) {
			message.setCode(CODE_UNKNOWN);
			message.setData(code);
		} else {
			String data = StringUtils.toString(msg.getData());
			if (null != values) {
				for (String value : values) {
					data = data.replaceFirst("\\?", value);
				}
			}
			message.setData(data);
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
	public static String createJsonMessage(String code, String... values) {
		Message message = createMessage(code, values);
		return JsonUtils.toString(message);
	}
}
