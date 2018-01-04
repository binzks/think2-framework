package org.think2framework.webmvc;

import javax.servlet.http.HttpSession;

import org.think2framework.core.utils.StringUtils;

/**
 * 系统默认值
 */
public enum DefaultValue {

	NOW("当前时间"), LOGIN_ID("登录用户ID"), LOGIN_CODE("登录用户编号"), LOGIN_NAME("登录用户名称");

	private String comment;

	DefaultValue(String comment) {
		this.comment = comment;
	}

	public static String getValue(DefaultValue defaultValue, HttpSession session) {
		if (NOW == defaultValue) {
			long now = System.currentTimeMillis() / 1000;
			return StringUtils.toString(now);
		} else {
			if (null != session) {
				return StringUtils.toString(session.getAttribute(defaultValue.toString()));
			} else {
				return "";
			}
		}
	}

	public static String getValue(String key, HttpSession session) {
		try {
			DefaultValue defaultValue = DefaultValue.valueOf(key.toUpperCase());
			return getValue(defaultValue, session);
		} catch (IllegalArgumentException e) {
			return key;
		}
	}

	@Override
	public String toString() {
		return comment;
	}

}
