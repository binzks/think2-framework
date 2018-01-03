package org.think2framework.core.orm.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.think2framework.core.Constants;
import org.think2framework.core.bean.Column;
import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.orm.bean.SqlObject;
import org.think2framework.core.utils.StringUtils;

/**
 * mysql数据库接口实现
 */
public class Mysql extends AbstractDatabase {

	public Mysql(Integer minIdle, Integer maxIdle, Integer initialSize, Integer timeout, String address,
			String database, String username, String password) {
		super(minIdle, maxIdle, initialSize, timeout, username, password, "com.mysql.cj.jdbc.Driver",
				"jdbc:mysql://" + address + "/" + database + "?characterEncoding=utf-8&useSSL=false");
	}

	/**
	 * 根据过滤条件设置生成相关sql和值
	 *
	 * @param filters
	 *            过滤条件
	 * @return sql和值
	 */
	private static SqlObject generateFilters(List<Filter> filters) {
		StringBuilder sql = new StringBuilder();
		List<Object> sqlValues = new ArrayList<>();
		for (Filter filter : filters) {
			List<Object> values = filter.getValues();
			Operator operator = filter.getOperator();
			sql.append(" AND ").append(filter.getKey()).append(" ");
			if (Operator.EQUAL == operator) {
				if (null == values) {
					throw new RuntimeException("运算符[=]没有过滤值！");
				}
				sql.append("= ?");
				sqlValues.add(values.get(0));
			} else if (Operator.BETWEEN == operator) {
				if (null == values || values.size() < 2) {
					throw new RuntimeException("运算符[between]没有两个过滤值！");
				}
				sql.append("BETWEEN ? AND ?");
				sqlValues.add(values.get(0));
				sqlValues.add(values.get(1));
			} else if (Operator.IN == operator || Operator.NOT_IN == operator) {
				if (null == values) {
					throw new RuntimeException("运算符[" + operator.toString() + "]没有过滤值！");
				}
				StringBuilder in = new StringBuilder();
				for (Object value : values) {
					in.append(",?");
					sqlValues.add(value);
				}
				sql.append(operator.toString()).append("(").append(in.substring(1)).append(")");
			} else {
				sql.append(operator.toString());
				if (null != values) {
					sql.append(" ?");
					sqlValues.add(values.get(0));
				}
			}
		}
		return new SqlObject(sql.toString(), sqlValues);
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
		// 基本字段
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE `").append(table).append("` (`").append(pk).append("`");
		// 主键
		if (autoIncrement) {
			sql.append(" int(11) NOT NULL AUTO_INCREMENT COMMENT '主键'");
		} else {
			sql.append(" varchar(32) NOT NULL COMMENT '主键'");
		}
		// 字段
		for (Column column : columns.values()) {
			// 忽略主键
			if (pk.equals(column.getName())) {
				continue;
			}
			sql.append(",").append(getMysqlColumnCreateSql(column));
		}
		// 主键
		sql.append(", PRIMARY KEY (`").append(pk).append("`)");
		// 唯一性约束
		if (null != uniques) {
			for (String unique : uniques) {
				if (StringUtils.isBlank(unique)) {
					continue;
				}
				sql.append(",UNIQUE KEY `unique_").append(table).append("_").append(unique).append("` (`")
						.append(unique.replace(",", "`,`")).append("`)");
			}
		}
		// 索引
		if (null != indexes) {
			for (String index : indexes) {
				if (StringUtils.isBlank(index)) {
					continue;
				}
				sql.append(",KEY `index_").append(table).append("_").append(index).append("` (`")
						.append(index.replace(",", "`,`")).append("`)");
			}
		}
		sql.append(")");
		if (StringUtils.isNotBlank(comment)) {
			sql.append(" COMMENT='").append(comment).append("';");
		}
		return sql.toString();
	}

	/**
	 * 根据字段定义获取mysql字段创建sql
	 *
	 * @param column
	 *            字段
	 * @return 创建sql
	 */
	private String getMysqlColumnCreateSql(Column column) {
		StringBuilder sql = new StringBuilder();
		sql.append("`").append(column.getName()).append("`");
		String type = column.getTag();
		if (Constants.FIELD_TEXT.equalsIgnoreCase(type)) {
			sql.append(" varchar(").append(column.getLength()).append(")");
		} else if (Constants.FIELD_INT.equalsIgnoreCase(type) || Constants.FIELD_TIMESTAMP.equalsIgnoreCase(type)
				|| Constants.FIELD_ITEM_INT.equalsIgnoreCase(type)) {
			sql.append(" int(").append(column.getLength()).append(")");
		} else if (Constants.FIELD_BOOL.equalsIgnoreCase(type)) {
			sql.append(" int(1)");
		} else if (Constants.FIELD_FLOAT.equalsIgnoreCase(type)) {
			sql.append(" decimal(").append(column.getLength()).append(",").append(column.getScale()).append(")");
		} else if (Constants.FIELD_JSON.equalsIgnoreCase(type)) {
			sql.append(" longtext");
		} else if (Constants.FIELD_TEXTAREA.equals(type)) {
			sql.append(" text");
		} else {
			sql.append(" varchar(").append(column.getLength()).append(")");
		}
		if (!column.getNullable()) {
			sql.append(" NOT NULL");
		}
		String defaultValue = StringUtils.toString(column.getDefaultValue());
		if (StringUtils.isNotBlank(defaultValue)) {
			sql.append(" DEFAULT '").append(defaultValue).append("'");
		}
		if (StringUtils.isNotBlank(column.getComment())) {
			sql.append(" COMMENT '").append(column.getComment()).append("'");
		}
		return sql.toString();
	}

	@Override
	public String generateJoins(List<Join> joins) {
		if (null == joins || joins.size() == 0) {
			return "";
		}
		StringBuilder joinSql = new StringBuilder();
		for (Join join : joins) {
			joinSql.append(join.getType().toUpperCase()).append(" ");
			if (StringUtils.isNotBlank(join.getDatabase())) {
				joinSql.append(join.getDatabase()).append(".");
			}
			joinSql.append(join.getTable()).append(" AS ").append(join.getName()).append(" ON ").append(join.getName())
					.append(".").append(join.getKey()).append("=");
			if (StringUtils.isBlank(join.getJoinName())) {
				joinSql.append(Constants.MAIN_TABLE_ALIAS);
			} else {
				joinSql.append(join.getJoinName());
			}
			joinSql.append(".").append(join.getJoinKey());
			if (StringUtils.isNotBlank(join.getFilter())) {
				joinSql.append(" ").append(join.getFilter());
			}
		}
		return joinSql.toString();
	}

	@Override
	public String generateColumns(Map<String, Column> columns) {
		if (null == columns || columns.size() == 0) {
			return "";
		}
		StringBuilder sql = new StringBuilder();
		for (Column column : columns.values()) {
			sql.append(",")
					.append(StringUtils.isBlank(column.getJoin()) ? Constants.MAIN_TABLE_ALIAS : column.getJoin())
					.append(".").append(column.getName());
			if (StringUtils.isNotBlank(column.getAlias())) {
				sql.append(" AS ").append(column.getAlias());
			}
		}
		return sql.substring(1);
	}

	@Override
	public SqlObject createSelect(String table, String joinSql, String fields, List<Filter> filters, List<String> group,
			List<Order> orders, Integer page, Integer size) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(fields).append(" FROM `").append(table).append("` ")
				.append(Constants.MAIN_TABLE_ALIAS).append(" ").append(joinSql).append(" WHERE 1=1 ");
		SqlObject sqlObject = generateFilters(filters);
		sql.append(sqlObject.getSql());
		if (null != group && group.size() > 0) {
			StringBuilder groupSql = new StringBuilder();
			for (String key : group) {
				groupSql.append(",").append(key);
			}
			sql.append(" GROUP BY ").append(groupSql.substring(1));
		}
		if (null != orders && orders.size() > 0) {
			StringBuilder orderSql = new StringBuilder();
			for (Order order : orders) {
				for (String key : order.getKeys()) {
					orderSql.append(",").append(key);
				}
				orderSql.append(" ").append(order.getType());
			}
			sql.append(" ORDER BY ").append(orderSql.substring(1));
		}
		if (page >= 0 && size > 0) {
			int begin = (page - 1) * size;
			if (begin < 0) {
				begin = 0;
			}
			sql.append(" LIMIT ").append(begin).append(",").append(size);
		}
		return new SqlObject(sql.toString(), sqlObject.getValues());
	}
}
