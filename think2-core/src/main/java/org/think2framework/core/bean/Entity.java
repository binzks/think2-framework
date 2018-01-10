package org.think2framework.core.bean;

import java.util.List;
import java.util.Map;

import org.think2framework.core.datasource.Field;

/**
 * 数据orm实体
 */
public class Entity {

	private String table; // 主表表名

	private String pk; // 主键名称

	private Boolean autoIncrement; // 是否自增长

	private Map<String, Field> fields; // 字段

	private List<Filter> filters; // 默认过滤条件

	private List<String> groups; // 分组

	private List<Order> orders; // 排序

	private List<Join> joins; // 关联

	private List<String> uniques; // 唯一性约束

	private List<String> indexes; // 索引

	private String comment; // 注释

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

	public Map<String, Field> getFields() {
		return fields;
	}

	public void setFields(Map<String, Field> fields) {
		this.fields = fields;
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

	public List<Join> getJoins() {
		return joins;
	}

	public void setJoins(List<Join> joins) {
		this.joins = joins;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
