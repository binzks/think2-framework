package org.think2framework.core.orm.database;

import java.util.List;
import java.util.Map;

import org.think2framework.core.bean.Column;
import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.orm.bean.SqlObject;

/**
 * sqlserver数据库接口实现
 */
public class Sqlserver extends AbstractDatabase {

	public Sqlserver(Integer minIdle, Integer maxIdle, Integer initialSize, Integer timeout, String address,
			String database, String username, String password) {
		super(minIdle, maxIdle, initialSize, timeout, username, password,
				"com.microsoft.sqlserver.jdbc.SQLServerDriver",
				"jdbc:sqlserver://" + address + ";databaseName=" + database);
	}

	@Override
	public String getKeyBegin() {
		return "[";
	}

	@Override
	public String getKeyEnd() {
		return "]";
	}

	@Override
	public String createSql(String table, String pk, boolean autoIncrement, List<String> uniques, List<String> indexes,
			String comment, Map<String, Column> columns) {
		return null;
	}

	@Override
	public SqlObject createSelect(String table, String joinSql, String fields, List<Filter> filters, List<String> group,
			List<Order> orders, Integer page, Integer size) {
		return null;
	}
}
