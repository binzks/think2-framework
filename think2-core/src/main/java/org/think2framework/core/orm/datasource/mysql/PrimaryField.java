package org.think2framework.core.orm.datasource.mysql;

import org.think2framework.core.orm.datasource.AbstractField;
import org.think2framework.core.orm.datasource.Entity;

/**
 * 主键
 */
public class PrimaryField extends AbstractField {

	private String createSql; // 创建sql

	public PrimaryField() {
		super(Entity.PRIMARY_KEY, false, null, null, null);
		setCreateSql(true);
	}

	public PrimaryField(String name) {
		super(name, false, null, null, null);
		setCreateSql(true);
	}

	public PrimaryField(String name, Boolean autoIncrement) {
		super(name, false, null, null, null);
		setCreateSql(autoIncrement);
	}

	private void setCreateSql(Boolean autoIncrement) {
		if (autoIncrement) {
			createSql = "`" + name() + "` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键'";
		} else {
			createSql = "`" + name() + "` VARCHAR(32) NOT NULL COMMENT '主键'";
		}
	}

	@Override
	public String createSql() {
		return createSql;
	}
}
