package org.think2framework.core.orm.database;

import java.util.List;
import java.util.Map;

import org.think2framework.core.bean.Column;
import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.orm.bean.SqlObject;

/**
 * sqlite数据库接口实现
 */
public class Sqlite extends AbstractDatabase {

	public Sqlite(Integer minIdle, Integer maxIdle, Integer initialSize, Integer timeout, String address,
			String database, String username, String password) {
		super(minIdle, maxIdle, initialSize, timeout, username, password, "org.sqlite.JDBC",
				"jdbc:sqlite:" + address + "/" + database);
	}

	@Override
	public String getKeyBegin() {
		return "`";
	}

	@Override
	public String getKeyEnd() {
		return "`";
	}

	@Override
	public String createSql(String table, String pk, boolean autoIncrement, List<String> uniques, List<String> indexes,
			String comment, Map<String, Column> columns) {
		return null;
	}

	@Override
	public String generateJoins(List<Join> joins) {
		return null;
	}

	@Override
	public String generateColumns(Map<String, Column> columns) {
		return null;
	}

	@Override
	public SqlObject createSelect(String table, String joinSql, String fields, List<Filter> filters, List<String> group,
			List<Order> orders, Integer page, Integer size) {
		return null;
	}
}
