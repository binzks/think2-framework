package org.think2framework.core.security;

import javax.servlet.http.HttpServletRequest;

/**
 * session管理
 */
public class SessionFactory {

	/**
	 * 根据session校验用户是否登录
	 * 
	 * @param httpServletRequest
	 *            请求
	 * @return 是否登录
	 */
	public static boolean isLogin(HttpServletRequest httpServletRequest) {
		return false;
	}
}
