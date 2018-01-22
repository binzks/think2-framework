package org.think2framework.context.bean;

import java.util.Map;

/**
 * 系统参数
 */
public class Param {

	private String name; // 参数名称

	private String model; // 参数取值对应模型

	private String key; // 参数取值对应模型的取值字段名称

	private String display; // 参数取值对应模型的显示字段名称

	private Map<String, String> values; // 静态参数值，map的key是值，value是显示

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}
}
