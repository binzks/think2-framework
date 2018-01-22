package org.think2framework.context.bean;

import java.util.List;

import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;

/**
 * 系统模型基础定义，主要为模型基础信息，可以复用到任何环境
 */
public class Model {

	private String name; // 模型名称

	private String table; // 模型对应的主表表名

	private String pk; // 模型主表主键名称

	private Boolean autoIncrement = true; // 是否自增长

	private List<Cell> cells; // 模型的单元格

	private List<Filter> filters; // 默认的过滤条件

	private List<String> groups; // 分组

	private List<Order> orders; // 排序

	private List<String> uniques; // 唯一性约束

	private List<String> indexes; // 索引

	private List<Join> joins; // 关联

	private String comment; // 注释

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public Boolean getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(Boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<String> getUniques() {
		return uniques;
	}

	public void setUniques(List<String> uniques) {
		this.uniques = uniques;
	}

	public List<String> getIndexes() {
		return indexes;
	}

	public void setIndexes(List<String> indexes) {
		this.indexes = indexes;
	}

	public List<Join> getJoins() {
		return joins;
	}

	public void setJoins(List<Join> joins) {
		this.joins = joins;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
