package org.think2framework.core.datasource.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.think2framework.core.datasource.ClassUtils;
import org.think2framework.core.bean.SqlObject;
import org.think2framework.core.datasource.AbstractWriter;
import org.think2framework.core.datasource.Field;
import org.think2framework.core.exception.MessageException;
import org.think2framework.core.exception.SystemMessage;
import org.think2framework.core.utils.StringUtils;

public class MysqlWriter extends AbstractWriter {

	private Map<String, Field> fields; // 字段

	private List<String> uniques; // 唯一性约束

	private List<String> indexes; // 索引

	private String comment; // 注释

	public MysqlWriter(String table, String pk, Boolean autoIncrement, Map<String, Field> fields, List<String> uniques,
			List<String> indexes, String comment, JdbcTemplate jdbcTemplate) {
		super(table, pk, autoIncrement, jdbcTemplate);
		this.fields = fields;
		this.uniques = uniques;
		this.indexes = indexes;
		this.comment = comment;
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
				throw new MessageException(SystemMessage.NON_EXIST.getCode(), "主键字段");
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
	public int delete(String key, Object value) {
		return update("DELETE FROM `" + table + "` WHERE 1=1 AND `" + key + "` =?", value);
	}
}
