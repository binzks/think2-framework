package org.think2framework.core.api;

import java.util.Map;

import org.think2framework.context.ModelFactory;
import org.think2framework.core.datasource.Writer;
import org.think2framework.core.exception.MessageFactory;

/**
 * 模型写入数据接口
 */
public class WriterModelActuator implements Actuator {

	private String model; // 模型名称

	public WriterModelActuator(String model) {
		this.model = model;
	}

	@Override
	public String exec(Map<String, Object> params) {
		Writer writer = ModelFactory.createWriter(model);
		writer.save(params);
		return MessageFactory.getJson();
	}
}
