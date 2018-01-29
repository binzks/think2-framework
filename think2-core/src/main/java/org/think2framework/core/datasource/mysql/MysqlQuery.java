package org.think2framework.core.datasource.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.bean.SqlObject;
import org.think2framework.core.datasource.AbstractQuery;
import org.think2framework.core.datasource.Field;
import org.think2framework.core.datasource.Operator;
import org.think2framework.core.datasource.Redis;

/**
 * mysql查询生成器
 */
public class MysqlQuery extends AbstractQuery implements Cloneable {

	private String table; // 主表表名

	private String joinSql; // 关联sql

	private String defaultFields; // 默认查询字段

	public MysqlQuery(String table, String pk, Map<String, Field> fields, List<Filter> filters, List<String> groups,
			List<Order> orders, List<Join> joins, Redis redis, JdbcTemplate jdbcTemplate) {
		super(pk, fields, redis, filters, groups, orders, jdbcTemplate);
		this.table = table;
		if (null == joins || joins.size() == 0) {
			this.joinSql = "";
		} else {
			StringBuilder joinSql = new StringBuilder();
			for (Join join : joins) {
				joinSql.append(join.toString()).append(" ");
				if (StringUtils.isNotBlank(join.getDatabase())) {
					joinSql.append(join.getDatabase()).append(".");
				}
				joinSql.append(join.getTable()).append(" AS ").append(join.getName()).append(" ON ")
						.append(join.getName()).append(".").append(join.getKey()).append("=");
				if (StringUtils.isBlank(join.getJoinName())) {
					joinSql.append(MAIN_TABLE_ALIAS);
				} else {
					joinSql.append(join.getJoinName());
				}
				joinSql.append(".").append(join.getJoinKey());
				if (StringUtils.isNotBlank(join.getFilter())) {
					joinSql.append(" ").append(join.getFilter());
				}
			}
			this.joinSql = joinSql.toString();
		}
	}

	@Override
	public String getDefaultFields() {
		if (null == defaultFields) {
			if (null == fields) {
				defaultFields = "";
			} else {
				StringBuilder sql = new StringBuilder();
				for (Field field : fields.values()) {
					sql.append(",").append(field.aliasKey());
				}
				defaultFields = sql.substring(1);
			}
		}
		return defaultFields;
	}

	/**
	 * 根据过滤条件设置生成相关sql和值
	 *
	 * @param filters
	 *            过滤条件
	 * @return sql和值
	 */
	private SqlObject generateFilters(List<Filter> filters) {
		StringBuilder sql = new StringBuilder();
		List<Object> sqlValues = new ArrayList<>();
		for (Filter filter : filters) {
			List<Object> values = filter.getValues();
			Operator operator = filter.getOperator();
			sql.append(" AND ").append(filter.getKey()).append(" ");
			switch (operator) {
				case EQUAL:
					if (null == values) {
						throw new RuntimeException("运算符[=]没有过滤值！");
					}
					sql.append("= ?");
					sqlValues.add(values.get(0));
					break;
				case BETWEEN:
					if (null == values || values.size() < 2) {
						throw new RuntimeException("运算符[between]没有两个过滤值！");
					}
					sql.append("BETWEEN ? AND ?");
					sqlValues.add(values.get(0));
					sqlValues.add(values.get(1));
					break;
				case IN:
				case NOT_IN:
					if (null == values) {
						throw new RuntimeException("运算符[" + operator.toString() + "]没有过滤值！");
					}
					StringBuilder in = new StringBuilder();
					for (Object value : values) {
						in.append(",?");
						sqlValues.add(value);
					}
					sql.append(operator.toString()).append("(").append(in.substring(1)).append(")");
					break;
				default:
					sql.append(operator.toString());
					if (null != values) {
						sql.append(" ?");
						sqlValues.add(values.get(0));
					}
					break;
			}
		}
		return new SqlObject(sql.toString(), sqlValues);
	}

	@Override
	public SqlObject createSelect(String fields) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(fields).append(" FROM `").append(table).append("` ").append(MAIN_TABLE_ALIAS)
				.append(" ").append(joinSql).append(" WHERE 1=1 ");
		SqlObject sqlObject = generateFilters(filters);
		sql.append(sqlObject.getSql());
		if (null != groups && groups.size() > 0) {
			StringBuilder groupSql = new StringBuilder();
			for (String key : groups) {
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
				orderSql.append(" ").append(order.getSort().toString());
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
