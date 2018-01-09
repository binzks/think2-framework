package org.think2framework.core.datasource.mysql;

/**
 * text字段
 */
public class TextField extends StringField {

	public TextField(String name, Boolean nullable, String join, String alias, String defaultValue, String comment) {
		super(name, nullable, join, alias, defaultValue, 0, comment);
		createSql = fieldCreateSql(name, join, "TEXT", nullable, defaultValue, comment);
	}

}
