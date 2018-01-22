package org.think2framework.core.datasource.oracle;

import java.util.List;
import java.util.Map;

import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.datasource.*;
import org.think2framework.core.persistence.FieldType;

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
	public Field createField(FieldType fieldType, String name, Boolean nullable, String join, String alias, String defaultValue, Integer length, Integer scale, String comment) {
		return null;
	}

	@Override
	public Query createQuery(String table, String pk, Map<String, Field> fields, List<Filter> filters, List<String> groups, List<Order> orders, List<Join> joins, Redis redis) {
		return null;
	}


	@Override
	public Writer createWriter(String table, String pk, Boolean autoIncrement, Map<String, Field> fields, List<String> uniques, List<String> indexes, String comment) {
		return null;
	}


}
