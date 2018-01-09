package org.think2framework.core.datasource.mysql;

/**
 * bool字段
 */
public class BooleanField extends StringField {

	public BooleanField(String name, Boolean nullable, String join, String alias, String defaultValue, String comment) {
		super(name, nullable, join, alias, defaultValue, 1, comment);
		createSql = fieldCreateSql(name, join, "int(1)", nullable, defaultValue, comment);
	}

}
