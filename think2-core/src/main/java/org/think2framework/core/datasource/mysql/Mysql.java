package org.think2framework.core.datasource.mysql;

import java.util.List;
import java.util.Map;

import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.datasource.AbstractDatasource;
import org.think2framework.core.orm.Entity;
import org.think2framework.core.datasource.Field;
import org.think2framework.core.datasource.Redis;

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
	public Field createStringField(String name, Boolean nullable, String join, String alias, String defaultValue,
			Integer length, String comment) {
		return new StringField(name, nullable, join, alias, defaultValue, length, comment);
	}

	@Override
	public Field createIntegerField(String name, Boolean nullable, String join, String alias, String defaultValue,
			Integer length, String comment) {
		return new IntegerField(name, nullable, join, alias, defaultValue, length, comment);
	}

	@Override
	public Field createBooleanField(String name, Boolean nullable, String join, String alias, String defaultValue,
			String comment) {
		return new BooleanField(name, nullable, join, alias, defaultValue, comment);
	}

	@Override
	public Field createFloatField(String name, Boolean nullable, String join, String alias, String defaultValue,
			Integer length, Integer scale, String comment) {
		return new FloatField(name, nullable, join, alias, length, scale, defaultValue, comment);
	}

	@Override
	public Field createTextField(String name, Boolean nullable, String join, String alias, String defaultValue,
			String comment) {
		return new TextField(name, nullable, join, alias, defaultValue, comment);
	}

	@Override
	public Field createJsonField(String name, Boolean nullable, String join, String alias, String defaultValue,
			String comment) {
		return new JsonField(name, nullable, join, alias, defaultValue, comment);
	}

	@Override
	public Entity createEntity(String table, String pk, Boolean autoIncrement, Map<String, Field> fields,
			List<Filter> filters, List<String> groups, List<Order> orders, Redis redis, List<String> uniques,
			List<String> indexes, List<Join> joins, String comment) {
		return new MysqlEntity(table, pk, autoIncrement, fields, filters, groups, orders, jdbcTemplate, redis, uniques,
				indexes, joins, comment);
	}
}