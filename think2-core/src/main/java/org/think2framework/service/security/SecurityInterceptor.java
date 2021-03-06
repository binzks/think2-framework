package org.think2framework.service.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Repository
public class SecurityInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		// // 用户是否登录
		// if (SessionHelp.isLogin(request.getSession())) {
		// return true;
		// } else {
		// response.sendRedirect("/think2/admin/welcome.do");
		//// if ("/".equals(request.getRequestURI()) ||
		// "/think2/admin/welcome.do".equals(request.getRequestURI())) {
		//// return true;
		//// } else {
		//// return false;
		//// }
		// return false;
		// }
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
	}

}