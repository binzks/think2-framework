package org.think2framework.core.datasource;

import org.apache.commons.lang3.StringUtils;

/**
 * 抽象字段定义
 */
public abstract class AbstractField implements Field {

	private String name; // 字段名称

	private Boolean nullable; // 字段是否可空

	private String key; // 表名或关联名称.字段名称

	private String aliasKey; // 表名或关联名称.字段名称 AS 别名

	public AbstractField(String name, Boolean nullable, String join, String alias) {
		this.name = name;
		this.nullable = nullable;
		if (StringUtils.isBlank(join)) {
			this.key = Query.MAIN_TABLE_ALIAS + "." + name;
		} else {
			this.key = join + "." + name;
		}
		if (StringUtils.isBlank(alias)) {
			this.aliasKey = this.key + " AS " + alias;
		} else {
			this.aliasKey = this.key;
		}
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

}
