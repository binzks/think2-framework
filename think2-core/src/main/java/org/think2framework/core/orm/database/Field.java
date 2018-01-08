package org.think2framework.core.orm.database;

public interface Field {

	/**
	 * 获取字段名称
	 * 
	 * @return 字段名称
	 */
	String name();

	/**
	 * 返回字段sql，表名(表别名、关联名).字段名称
	 * 
	 * @return 字段sql
	 */
	String key();

	/**
	 * 返回字段别名sql，表名(表别名、关联名).字段名称 AS 别名
	 * 
	 * @return 字段带别名sql
	 */
	String aliasKey();

	/**
	 * 是否可空，主键不允许为空，其他默认可空
	 * 
	 * @return 是否可空
	 */
	Boolean nullable();

	/**
	 * 字段创建sql，如果是关联字段返回空
	 * 
	 * @return 创建sql
	 */
	String createSql();

	/**
	 * 获取字段默认值，没有设置返回null，时间默认当前时间
	 * 
	 * @return 默认值
	 */
	String defaultValue();

}
