package org.think2framework.core.orm.database.mysql;

/**
 * int字段，如果长度为null默认设置长度11
 */
public class IntField extends StringField {

	public IntField(String name, Boolean nullable, String join, String alias, String defaultValue, Integer length,
			String comment) {
		super(name, nullable, join, alias, defaultValue, length, comment);
		// 整型字段默认长度为11
		createSql = fieldCreateSql(name, join, "INT(" + (null == length ? 11 : length) + ")", nullable, defaultValue,
				comment);
	}

}
