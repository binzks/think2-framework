package org.think2framework.core.orm.database.mysql;

/**
 * float字段
 */
public class FloatField extends StringField {

	public FloatField(String name, Boolean nullable, String join, String alias, Integer length, Integer scale,
			String defaultValue, String comment) {
		super(name, nullable, join, alias, defaultValue, length, comment);
		createSql = fieldCreateSql(name, join, "DECIMAL(" + length + "," + scale + ")", nullable, defaultValue,
				comment);
	}

}
