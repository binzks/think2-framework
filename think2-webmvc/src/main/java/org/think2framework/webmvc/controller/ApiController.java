//package org.think2framework.webmvc.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.think2framework.core.ModelFactory;
//import org.think2framework.core.bean.Join;
//import org.think2framework.core.bean.Model;
//import org.think2framework.core.exception.MessageException;
//import org.think2framework.core.exception.MessageFactory;
//import org.think2framework.core.utils.HttpServletUtils;
//import org.think2framework.core.utils.JsonUtils;
//import org.think2framework.core.utils.StringUtils;
//
//@Controller
//@RequestMapping(value = "/api")
//public class ApiController {
//
//	/**
//	 * 获取模型，如果没有则抛错
//	 *
//	 * @param request
//	 *            请求
//	 * @return 模型
//	 */
//	private Model getModel(HttpServletRequest request) {
//		String name = request.getParameter("m");
//		if (StringUtils.isBlank(name)) {
//			throw new MessageException("01001", "模型");
//		}
//		return ModelFactory.get(name);
//	}
//
//	/**
//	 * 新增或修改模型数九
//	 *
//	 * @param request
//	 *            请求
//	 * @param response
//	 *            响应
//	 * @param insert
//	 *            是否新增
//	 */
//	private void insertOrUpdate(HttpServletRequest request, HttpServletResponse response, boolean insert) {
//		Model model = getModel(request);
//		Writer writer = model.createWriter();
//		Map<String, Object> map = new HashMap<>();
//		for (Column column : model.getColumns().values()) {
//			String value = request.getParameter(column.getName());
//			if (StringUtils.isBlank(value)) {
//				if (!column.getNullable()) {
//					throw new MessageException("01002", column.getTitle());
//				}
//			} else {
//				map.put(column.getName(), value);
//			}
//		}
//		if (insert) {
//			writer.insert(map);
//		} else {
//			writer.update(map);
//		}
//		HttpServletUtils.writeResponse(response, MessageFactory.getJson());
//	}
//
//	@RequestMapping(value = "/add.api")
//	public void add(HttpServletRequest request, HttpServletResponse response) {
//	    Column column = new Column();
//	    column.setName("name");
//        JsonUtils.writerFile("/Users/zhoubin/Desktop/2.json", column);
//		Join join = JsonUtils.readFile("/Users/zhoubin/Desktop/1.json", Join.class);
//		join.setDatabase("db2");
////		Join join = new Join("test", "db", "table", JoinType.LEFT, "key", "joinName", "joinKey", "filter");
//		JsonUtils.writerFile("/Users/zhoubin/Desktop/2.json", join);
//		insertOrUpdate(request, response, true);
//	}
//
//	@RequestMapping(value = "/edit.api")
//	public void edit(HttpServletRequest request, HttpServletResponse response) {
//		insertOrUpdate(request, response, false);
//	}
//
//	/**
//	 * 状态变更，参数m模型名称，k状态字段名称，v变更值，id数据主键值
//	 *
//	 * @param request
//	 *            请求
//	 * @param response
//	 *            响应
//	 */
//	@RequestMapping(value = "/status.api")
//	public void status(HttpServletRequest request, HttpServletResponse response) {
//		Model model = getModel(request);
//		String key = request.getParameter("k");
//		String value = request.getParameter("v");
//		String id = request.getParameter("id");
//		if (StringUtils.isBlank(key)) {
//			throw new MessageException("01001", "字段");
//		}
//		if (StringUtils.isBlank(value)) {
//			throw new MessageException("01001", "值");
//		}
//		if (StringUtils.isBlank(id)) {
//			throw new MessageException("01001", "主键");
//		}
//		Map<String, Object> map = new HashMap<>();
//		map.put(model.getPk(), id);
//		map.put(key, value);
//		Writer writer = model.createWriter();
//		writer.update(map);
//		HttpServletUtils.writeResponse(response, MessageFactory.getJson());
//	}
//
//}
