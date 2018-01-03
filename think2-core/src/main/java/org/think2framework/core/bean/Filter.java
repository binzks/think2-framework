package org.think2framework.core.bean;

import java.util.List;

import org.think2framework.core.orm.database.Operator;

/**
 * 查询实体的查询条件
 */
public class Filter {

	/**
	 * 过滤关键字
	 */
	private String key;

	/**
	 * 运算符= > <等
	 */
	private Operator operator;

	/**
	 * 过滤值数组
	 */
	private List<Object> values;

	public Filter() {
	}

	public Filter(String key, Operator operator, List<Object> values) {
		this.key = key;
		this.operator = operator;
		this.values = values;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}
}
