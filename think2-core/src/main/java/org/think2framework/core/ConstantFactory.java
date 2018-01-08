package org.think2framework.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.think2framework.core.exception.NonExistException;
import org.think2framework.core.utils.StringUtils;

/**
 * 常量工厂
 */
public class ConstantFactory {

	private static final Logger logger = LogManager.getLogger(ConstantFactory.class);

	private static Map<String, Object> constants = new HashMap<>(); // 系统常量缓存

	private static Map<String, Map<String, Object>> groups = new HashMap<>(); // 系统常量组缓存

	/**
	 * 获取常量的key,如果分组不为null和空,则分组名_常量名,否则常量名
	 *
	 * @param group
	 *            分组名
	 * @param name
	 *            常量名
	 * @return 常量key
	 */
	private static String getKey(String group, String name) {
		String key = name;
		if (StringUtils.isNotBlank(group)) {
			key = group + "_" + name;
		}
		return key;
	}

	/**
	 * 获取已经追加的常量数量
	 * 
	 * @return 常量数量
	 */
	public static int size() {
		return constants.size();
	}

	/**
	 * 追加常量
	 */

	/**
	 * 追加一个常量，如果常量已经存在则忽略
	 * 
	 * @param group
	 *            分组名称
	 * @param name
	 *            常量名称
	 * @param value
	 *            常量值
	 */
	public static synchronized void append(String group, String name, String value) {
		String key = getKey(group, name);
		if (null != constants.get(key)) {
			logger.warn("常量[{}]已经存在，忽略追加！", key);
		} else {
			constants.put(key, value);
			// 如果常量存在分组名称,则将常量添加到分组
			if (StringUtils.isNotBlank(group)) {
				// 如果常量分组还不存在则创建
				Map<String, Object> map = groups.get(group);
				if (null == map) {
					map = new HashMap<>();
				}
				// 如果常量分组中已经存在待添加的常量值则抛出异常
				if (null != map.get(value)) {
					logger.warn("常量分组[{}]值[{}]已经存在，忽略追加！", group, value);
				} else {
					map.put(value, name);
					groups.put(group, map);
				}

			}
			logger.debug("追加常量[{}][{}][{}]", group, name, value);
		}
	}

	/**
	 * 根据分组名称和常量名称获取常量值
	 *
	 * @param group
	 *            分组名称
	 * @param name
	 *            常量名称
	 * @return 常量值
	 */
	public static String get(String group, String name) {
		String key = getKey(group, name);
		Object value = constants.get(key);
		if (null == value) {
			throw new NonExistException("常量[" + key + "]");
		}
		return value.toString();
	}

	/**
	 * 根据常量名称获取常量值
	 *
	 * @param name
	 *            常量名称
	 * @return 常量值
	 */
	public static String get(String name) {
		return get(null, name);
	}

	/**
	 * 根据分组名称获取一个常量分组
	 *
	 * @param name
	 *            分组名称
	 * @return 分组map
	 */
	public static Map<String, Object> getGroup(String name) {
		Map<String, Object> map = groups.get(name);
		if (null == map) {
			throw new NonExistException("常量分组[" + name + "]");
		}
		return map;
	}

	/**
	 * 对比值是否和常量值相等
	 *
	 * @param name
	 *            常量名称
	 * @param value
	 *            比较值
	 * @return 是否相等
	 */
	public static boolean equals(String name, Object value) {
		return equals(null, name, value);
	}

	/**
	 * 对比值是否和常量值相等
	 *
	 * @param group
	 *            分组名称
	 * @param name
	 *            常量名称
	 * @param value
	 *            比较值
	 * @return 是否相等
	 */
	public static boolean equals(String group, String name, Object value) {
		Boolean result = false;
		Object v = get(group, name);
		if (Object.class.getClass() == String.class.getClass()) {
			if (value.toString().equals(v.toString())) {
				result = true;
			}
		} else {
			if (v == value) {
				result = true;
			}
		}
		return result;
	}

}
