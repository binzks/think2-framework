package org.think2framework.core.datasource.mysql;

import static org.think2framework.core.persistence.FieldType.*;

import org.apache.commons.lang3.StringUtils;
import org.think2framework.core.datasource.AbstractField;
import org.think2framework.core.datasource.Query;
import org.think2framework.core.persistence.FieldType;

public class MysqlField extends AbstractField {

	private String createSql; // 创建sql

	public MysqlField(FieldType fieldType, String name, Boolean nullable, String join, String alias, Integer length,
			Integer scale, String comment) {
		super(name, nullable, join, alias);
		if (TEXT.equals(fieldType) || EMAIL.equals(fieldType) || FILE.equals(fieldType) || IMAGE.equals(fieldType)
				|| ITEM.equals(fieldType) || MOBILE.equals(fieldType) || MULTIPLE.equals(fieldType)
				|| PASSWORD.equals(fieldType) || TELEPHONE.equals(fieldType) || DATA_ITEM.equals(fieldType)
				|| DATA_MULTIPLE.equals(fieldType)) {
			createSql = fieldCreateSql(name, join, "VARCHAR(" + length + ")", nullable, comment);
		} else if (INT.equals(fieldType) || DATA_ITEM_INT.equals(fieldType) || ITEM_INT.equals(fieldType)) {
			createSql = fieldCreateSql(name, join, "INT(" + (null == length ? 11 : length) + ")", nullable, comment);
		} else if (BOOL.equals(fieldType)) {
			createSql = fieldCreateSql(name, join, "INT(1)", nullable, comment);
		} else if (FLOAT.equals(fieldType)) {
			createSql = fieldCreateSql(name, join, "DECIMAL(" + length + "," + scale + ")", nullable, comment);
		} else if (JSON.equals(fieldType) || HTML.equals(fieldType) || TEXTAREA.equals(fieldType)) {
			createSql = fieldCreateSql(name, join, "LONG TEXT", nullable, comment);
		} else if (TIMESTAMP.equals(fieldType)) {
			createSql = fieldCreateSql(name, join, "INT(10)", nullable, comment);
		} else if (DATE.equals(fieldType) || DATETIME.equals(fieldType)) {
			createSql = fieldCreateSql(name, join, "DATETIME", nullable, comment);
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
	 * @param comment
	 *            注释
	 * @return 创建sql
	 */
	private String fieldCreateSql(String name, String join, String type, Boolean nullable, String comment) {
		if (!StringUtils.isBlank(join) && !Query.MAIN_TABLE_ALIAS.equals(join)) {
			return "";
		}
		StringBuilder sql = new StringBuilder();
		sql.append("`").append(name).append("` ").append(type);
		if (!nullable) {
			sql.append(" NOT NULL");
		}
		if (StringUtils.isNotBlank(comment)) {
			sql.append(" COMMENT '").append(comment).append("'");
		}
		return sql.toString();
	}
}
