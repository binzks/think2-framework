package org.think2framework.core.orm.datasource.sqlserver;

import java.util.List;
import java.util.Map;

import org.think2framework.core.orm.bean.Filter;
import org.think2framework.core.orm.bean.Join;
import org.think2framework.core.orm.bean.Order;
import org.think2framework.core.orm.datasource.AbstractDatasource;
import org.think2framework.core.orm.datasource.Entity;
import org.think2framework.core.orm.datasource.Field;
import org.think2framework.core.orm.datasource.FieldType;

/**
 * sqlserver
 */
public class Sqlserver extends AbstractDatasource {

	public Sqlserver(Integer minIdle, Integer maxIdle, Integer initialSize, Integer timeout, String username,
			String password, String host, Integer port, String database) {
		super(minIdle, maxIdle, initialSize, timeout, username, password,
				"com.microsoft.sqlserver.jdbc.SQLServerDriver",
				"jdbc:sqlserver://" + host + (null == port ? "" : ":" + port) + ";databaseName=" + database);
	}

	@Override
	public Field createField(FieldType fieldType, String name, Boolean nullable, String join, String alias,
			String defaultValue, Integer length, Integer scale, String comment) {
		return null;
	}

	@Override
	public Entity createEntity(String table, String pk, Boolean autoIncrement, Map<String, Field> fields,
			List<Filter> filters, List<String> groups, List<Order> orders, String redis, List<String> uniques,
			List<String> indexes, List<Join> joins, String comment) {
		return null;
	}
}
