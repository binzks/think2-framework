package org.think2framework.core.orm.database.mysql;

/**
 * json字段
 */
public class JsonField extends StringField {

	public JsonField(String name, Boolean nullable, String join, String alias, String defaultValue, String comment) {
		super(name, nullable, join, alias, defaultValue, 0, comment);
		this.createSql = fieldCreateSql(name, join, "LONG TEXT", nullable, defaultValue, comment);
	}

}
