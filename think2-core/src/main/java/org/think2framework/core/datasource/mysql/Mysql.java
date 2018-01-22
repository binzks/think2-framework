package org.think2framework.core.datasource.mysql;

import java.util.List;
import java.util.Map;

import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.datasource.*;
import org.think2framework.core.persistence.FieldType;

/**
 * mysql
 */
public class Mysql extends AbstractDatasource {

	public Mysql(Integer minIdle, Integer maxIdle, Integer initialSize, Integer timeout, String username,
			String password, String host, Integer port, String database) {
		super(minIdle, maxIdle, initialSize, timeout, username, password, "com.mysql.cj.jdbc.Driver", "jdbc:mysql://"
				+ host + (null == port ? "" : ":" + port) + "/" + database + "?characterEncoding=utf-8&useSSL=false");
	}

	@Override
	public Field createPrimaryField(String name, Boolean autoIncrement) {
		return new PrimaryField(name, autoIncrement);
	}

	@Override
	public Field createField(FieldType fieldType, String name, Boolean nullable, String join, String alias,
                             String defaultValue, Integer length, Integer scale, String comment) {
		return new MysqlField(fieldType, name, nullable, join, alias, length, scale, defaultValue, comment);
	}

	@Override
	public Query createQuery(String table, String pk, Map<String, Field> fields, List<Filter> filters,
			List<String> groups, List<Order> orders, List<Join> joins, Redis redis) {
		return new MysqlQuery(table, pk, fields, filters, groups, orders, joins, redis, jdbcTemplate);
	}

	@Override
	public Writer createWriter(String table, String pk, Boolean autoIncrement, Map<String, Field> fields,
			List<String> uniques, List<String> indexes, String comment) {
		return new MysqlWriter(table, pk, autoIncrement, fields, uniques, indexes, comment, jdbcTemplate);
	}

}
