package org.think2framework.core.datasource.mysql;

/**
 * int字段，如果长度为null默认设置长度11
 */
public class IntegerField extends StringField {

	public IntegerField(String name, Boolean nullable, String join, String alias, String defaultValue, Integer length,
                        String comment) {
		super(name, nullable, join, alias, defaultValue, length, comment);
		// 整型字段默认长度为11
		createSql = fieldCreateSql(name, join, "INT(" + (null == length ? 11 : length) + ")", nullable, defaultValue,
				comment);
	}

}
