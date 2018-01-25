package org.think2framework.core.security;

import org.think2framework.context.ModelFactory;
import org.think2framework.core.bean.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * session管理
 */
public class SessionFactory {

    private static Map<String, HttpSession> sessions = new HashMap<>();

	public static View getView(String name, HttpSession session){
		return ModelFactory.getView(name);
	}

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
