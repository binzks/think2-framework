package org.think2framework.core.orm.database.mysql;

import org.think2framework.core.orm.database.AbstractField;
import org.think2framework.core.orm.database.Entity;
import org.think2framework.core.utils.StringUtils;

/**
 * string字段
 */
public class StringField extends AbstractField {

	protected String createSql;

	public StringField(String name, Boolean nullable, String join, String alias, String defaultValue, Integer length,
			String comment) {
		super(name, nullable, join, alias, defaultValue);
		createSql = fieldCreateSql(name, join, "VARCHAR(" + length + ")", nullable, defaultValue, comment);
	}

	@Override
	public String createSql() {
		return createSql;
	}

    /**
     * 获取字段的创建sql，如果是关联字段则返回空
     *
     * @param name
     *            字段名称
     * @param join
     *            字段关联
     * @param type
     *            类型和长度
     * @param nullable
     *            是否可空
     * @param defaultValue
     *            默认值
     * @param comment
     *            注释
     * @return 创建sql
     */
    protected String fieldCreateSql(String name, String join, String type, Boolean nullable, String defaultValue,
                                        String comment) {
        if (!StringUtils.isBlank(join) && !Entity.MAIN_TABLE_ALIAS.equals(join)) {
            return "";
        }
        StringBuilder sql = new StringBuilder();
        sql.append("`").append(name).append("` ").append(type);
        if (!nullable) {
            sql.append(" NOT NULL");
        }
        if (StringUtils.isNotBlank(defaultValue)) {
            sql.append(" DEFAULT '").append(defaultValue).append("'");
        }
        if (StringUtils.isNotBlank(comment)) {
            sql.append(" COMMENT '").append(comment).append("'");
        }
        return sql.toString();
    }
}
