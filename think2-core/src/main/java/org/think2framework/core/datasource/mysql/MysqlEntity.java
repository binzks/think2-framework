package org.think2framework.core.datasource.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.think2framework.core.ClassUtils;
import org.think2framework.core.exception.NonExistException;
import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.bean.SqlObject;
import org.think2framework.core.datasource.AbstractEntity;
import org.think2framework.core.datasource.Field;
import org.think2framework.core.datasource.Redis;
import org.think2framework.core.persistence.Operator;
import org.think2framework.core.utils.StringUtils;

/**
 * mysql数据库实体
 */
public class MysqlEntity extends AbstractEntity {

	private List<String> uniques; // 唯一性约束

	private List<String> indexes; // 索引

	private String comment; // 注释

	private String joinSql; // 关联sql

	private String defaultFields; // 默认查询字段

	public MysqlEntity(String table, String pk, Boolean autoIncrement, Map<String, Field> fields, List<Filter> filters,
			List<String> groups, List<Order> orders, JdbcTemplate jdbcTemplate, Redis redis, List<String> uniques,
			List<String> indexes, List<Join> joins, String comment) {
		super(table, pk, autoIncrement, fields, filters, groups, orders, jdbcTemplate, redis);
		this.uniques = uniques;
		this.indexes = indexes;
		this.comment = comment;
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
	public String createSql() {
		// 基本字段
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE `").append(table).append("` (`").append(pk).append("`");
		// 主键
		if (autoIncrement) {
			sql.append(" INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键'");
		} else {
			sql.append(" VARCHAR(32) NOT NULL COMMENT '主键'");
		}
		// 字段
		for (Field field : fields.values()) {
			sql.append(",").append(field.createSql());
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

	@Override
	public SqlObject createInsert(Object instance, Object id) {
		StringBuilder fieldSql = new StringBuilder();
		StringBuilder valueSql = new StringBuilder();
		List<Object> values = new ArrayList<>();
		for (Field field : fields.values()) {
			Object value = ClassUtils.getFieldValue(instance, field.name());
			if (field.getClass() == PrimaryField.class) {
				if (null == id) {
					continue;
				} else {
					value = id;
				}
			}
			// 不新增null值
			if (null == value) {
				continue;
			}
			fieldSql.append(",`").append(field.name()).append("`");
			valueSql.append(",?");
			values.add(value);
		}
		String sql = "INSERT INTO `" + table + "`(" + fieldSql.toString().substring(1) + ") VALUES ("
				+ valueSql.toString().substring(1) + ")";
		return new SqlObject(sql, values);
	}

	@Override
	public SqlObject createUpdate(Object instance, String... keys) {
		StringBuilder fieldSql = new StringBuilder();
		StringBuilder keySql = new StringBuilder();
		List<Object> values = new ArrayList<>();
		String pk = null;
		for (Field field : fields.values()) {
			// 如果字段不是主键,则修改
			if (field.getClass() == PrimaryField.class) {
				pk = field.name();
			} else {
				Object value = ClassUtils.getFieldValue(instance, field.name());
				// 不修改null的字段
				if (null == value) {
					continue;
				}
				fieldSql.append(",`").append(field.name()).append("`=?");
				values.add(value);
			}
		}
		if (null == keys || keys.length == 0) {
			if (null == pk) {
				throw new NonExistException("主键字段");
			}
			keySql.append(" AND `").append(pk).append("`=?");
			values.add(ClassUtils.getFieldValue(instance, pk));
		} else {
			for (String key : keys) {
				keySql.append(" AND `").append(key).append("`=?");
				values.add(ClassUtils.getFieldValue(instance, key));
			}
		}
		String sql = "UPDATE `" + table + "` SET " + fieldSql.toString().substring(1) + " WHERE 1=1"
				+ keySql.toString();
		return new SqlObject(sql, values);
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

	@Override
	public int delete(String key, Object value) {
		return update("DELETE FROM `" + table + "` WHERE 1=1 AND `" + key + "` =?", value);
	}

}
