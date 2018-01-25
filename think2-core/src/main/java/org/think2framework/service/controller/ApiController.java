package org.think2framework.service.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.think2framework.context.ApiFactory;

/**
 * 系统通用定义接口控制器
 */
@RestController
@RequestMapping("/api")
public class ApiController {

	@RequestMapping(value = "/exec-{id}.do")
	public String list(@PathVariable String id, HttpServletRequest request) {
		return ApiFactory.exec(id, request.getParameterMap());
	}
}
