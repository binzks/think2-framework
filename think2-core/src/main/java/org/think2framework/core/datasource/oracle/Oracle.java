package org.think2framework.core.datasource.oracle;

import java.util.List;
import java.util.Map;

import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.datasource.AbstractDatasource;
import org.think2framework.core.datasource.Field;
import org.think2framework.core.datasource.Redis;
import org.think2framework.core.orm.Entity;

/**
 * oracle
 */
public class Oracle extends AbstractDatasource {

	public Oracle(Integer minIdle, Integer maxIdle, Integer initialSize, Integer timeout, String username,
			String password, String host, String database) {
		super(minIdle, maxIdle, initialSize, timeout, username, password, "oracle.jdbc.driver.OracleDriver",
				"jdbc:oracle:thin:@" + host + ":" + database);
	}

	@Override
	public Field createPrimaryField(String name, Boolean autoIncrement) {
		return null;
	}

	@Override
	public Field createStringField(String name, Boolean nullable, String join, String alias, String defaultValue,
			Integer length, String comment) {
		return null;
	}

	@Override
	public Field createIntegerField(String name, Boolean nullable, String join, String alias, String defaultValue,
			Integer length, String comment) {
		return null;
	}

	@Override
	public Field createBooleanField(String name, Boolean nullable, String join, String alias, String defaultValue,
			String comment) {
		return null;
	}

	@Override
	public Field createFloatField(String name, Boolean nullable, String join, String alias, String defaultValue,
			Integer length, Integer scale, String comment) {
		return null;
	}

	@Override
	public Field createTextField(String name, Boolean nullable, String join, String alias, String defaultValue,
			String comment) {
		return null;
	}

	@Override
	public Field createJsonField(String name, Boolean nullable, String join, String alias, String defaultValue,
			String comment) {
		return null;
	}

	@Override
	public Entity createEntity(String table, String pk, Boolean autoIncrement, Map<String, Field> fields,
			List<Filter> filters, List<String> groups, List<Order> orders, Redis redis, List<String> uniques,
			List<String> indexes, List<Join> joins, String comment) {
		return null;
	}
}
