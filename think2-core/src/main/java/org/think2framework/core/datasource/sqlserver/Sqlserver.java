package org.think2framework.core.datasource.sqlserver;

import java.util.List;
import java.util.Map;

import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.datasource.*;
import org.think2framework.core.persistence.FieldType;

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
	public Field createPrimaryField(String name, Boolean autoIncrement) {
		return null;
	}

	@Override
	public Field createField(FieldType fieldType, String name, Boolean nullable, String join, String alias,
			Integer length, Integer scale, String comment) {
		return null;
	}

	@Override
	public Query createQuery(String table, String pk, Map<String, Field> fields, List<Filter> filters,
			List<String> groups, List<Order> orders, List<Join> joins, Redis redis) {
		return null;
	}

	@Override
	public Writer createWriter(String table, String pk, Boolean autoIncrement, Map<String, Field> fields,
			List<String> uniques, List<String> indexes, String comment) {
		return null;
	}

}
