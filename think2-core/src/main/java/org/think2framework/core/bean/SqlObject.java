package org.think2framework.core.bean;

import java.util.List;

/**
 * sql语句和值
 */
public class SqlObject {

	private String sql;

	private List<Object> values;

	public SqlObject() {
	}

	public SqlObject(String sql, List<Object> values) {
		this.sql = sql;
		this.values = values;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}
}
