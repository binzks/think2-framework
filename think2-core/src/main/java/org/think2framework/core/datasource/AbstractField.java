package org.think2framework.core.datasource;

import org.think2framework.core.utils.StringUtils;

/**
 * 抽象字段定义
 */
public abstract class AbstractField implements Field {

	private String name; // 字段名称

	private Boolean nullable; // 字段是否可空

	private String key; // 表名或关联名称.字段名称

	private String aliasKey; // 表名或关联名称.字段名称 AS 别名

	private String defaultValue; // 默认值

	public AbstractField(String name, Boolean nullable, String join, String alias, String defaultValue) {
		this.name = name;
		this.nullable = nullable;
		if (StringUtils.isBlank(join)) {
			this.key = Entity.MAIN_TABLE_ALIAS + "." + name;
		} else {
			this.key = join + "." + name;
		}
		if (StringUtils.isBlank(alias)) {
			this.aliasKey = this.key + " AS " + alias;
		} else {
			this.aliasKey = this.key;
		}
		this.defaultValue = defaultValue;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String key() {
		return key;
	}

	@Override
	public String aliasKey() {
		return aliasKey;
	}

	@Override
	public Boolean nullable() {
		return nullable;
	}

	@Override
	public String defaultValue() {
		return defaultValue;
	}

}
