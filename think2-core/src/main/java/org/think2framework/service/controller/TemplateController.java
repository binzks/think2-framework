package org.think2framework.service.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.think2framework.context.ModelFactory;
import org.think2framework.core.bean.Column;
import org.think2framework.core.bean.View;
import org.think2framework.core.datasource.Writer;
import org.think2framework.core.exception.MessageFactory;

@RestController
@RequestMapping(value = "/tpl")
public class TemplateController {

    @RequestMapping(value = "/list-{model}.do")
    public String list(@PathVariable String model, HttpServletRequest request) {
        return "";
    }

	@RequestMapping(value = "/add-{model}.do")
	public String add(@PathVariable String model,HttpServletRequest request) {
		View view = ModelFactory.getView(model);
		Map<String, Object> map = new HashMap<>();
		for (Column column : view.getColumns().values()) {
			String value = request.getParameter(column.getName());
			if (StringUtils.isBlank(value) && StringUtils.isNotBlank(column.getDefaultValue())) {
				// value = SessionFactory.isLogin();
			}
			if (null != value) {
				map.put(column.getName(), value);
			}
		}
		Writer writer = ModelFactory.createWriter(model);
		writer.insert(map);
		return MessageFactory.getJson();
	}
}
