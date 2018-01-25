package org.think2framework.core.datasource.mysql;

import org.think2framework.core.datasource.AbstractField;

/**
 * 主键
 */
public class PrimaryField extends AbstractField {

	private String createSql; // 创建sql

	public PrimaryField(String name, Boolean autoIncrement) {
		super(name, false, null, null);
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
