package org.think2framework.core.orm.bean;

import java.util.List;

import org.think2framework.core.orm.datasource.Sort;

/**
 * 查询实体的排序
 */
public class Order {

	/**
	 * 排序字段数组
	 */
	private List<String> keys;

	/**
	 * 排序规则asc、desc
	 */
	private Sort sort;

	public Order() {
	}

	public Order(List<String> keys, Sort sort) {
		this.keys = keys;
		this.sort = sort;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}
}
