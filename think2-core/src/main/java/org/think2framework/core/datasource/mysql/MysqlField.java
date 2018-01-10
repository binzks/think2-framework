package org.think2framework.core.datasource.mysql;

import org.think2framework.core.datasource.AbstractField;
import org.think2framework.core.datasource.FieldType;
import org.think2framework.core.datasource.Query;
import org.think2framework.core.utils.StringUtils;

public class MysqlField extends AbstractField {

	private String createSql; // 创建sql

	public MysqlField(FieldType fieldType, String name, Boolean nullable, String join, String alias, Integer length,
			Integer scale, String defaultValue, String comment) {
		super(name, nullable, join, alias, defaultValue);
		switch (fieldType) {
		case STRING:
			createSql = fieldCreateSql(name, join, "VARCHAR(" + length + ")", nullable, defaultValue, comment);
			break;
		case INTEGER:
			createSql = fieldCreateSql(name, join, "INT(" + (null == length ? 11 : length) + ")", nullable,
					defaultValue, comment);
			break;
		case BOOLEAN:
			createSql = fieldCreateSql(name, join, "int(1)", nullable, defaultValue, comment);
			break;
		case FLOAT:
			createSql = fieldCreateSql(name, join, "DECIMAL(" + length + "," + scale + ")", nullable, defaultValue,
					comment);
			break;
		case JSON:
			createSql = fieldCreateSql(name, join, "LONG TEXT", nullable, defaultValue, comment);
			break;
		case TEXT:
			createSql = fieldCreateSql(name, join, "TEXT", nullable, defaultValue, comment);
			break;
		}
	}

	@Override
	public String createSql() {
		return createSql;
	}

	/**
	 * 获取字段的创建sql，如果是关联字段则返回空
	 *
	 * @param name
	 *            字段名称
	 * @param join
	 *            字段关联
	 * @param type
	 *            类型和长度
	 * @param nullable
	 *            是否可空
	 * @param defaultValue
	 *            默认值
	 * @param comment
	 *            注释
	 * @return 创建sql
	 */
	private String fieldCreateSql(String name, String join, String type, Boolean nullable, String defaultValue,
			String comment) {
		if (!StringUtils.isBlank(join) && !Query.MAIN_TABLE_ALIAS.equals(join)) {
			return "";
		}
		StringBuilder sql = new StringBuilder();
		sql.append("`").append(name).append("` ").append(type);
		if (!nullable) {
			sql.append(" NOT NULL");
		}
		if (StringUtils.isNotBlank(defaultValue)) {
			sql.append(" DEFAULT '").append(defaultValue).append("'");
		}
		if (StringUtils.isNotBlank(comment)) {
			sql.append(" COMMENT '").append(comment).append("'");
		}
		return sql.toString();
	}
}