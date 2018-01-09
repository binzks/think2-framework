package org.think2framework.core.datasource;

import java.util.List;
import java.util.Map;

import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;

/**
 * 数据源接口
 */
public interface Datasource {

	/**
	 * 根据数据源创建一个对应的实体主键
	 * 
	 * @param name
	 *            主键名称，null默认为id
	 * @param autoIncrement
	 *            是否自增长
	 * @return 主键字段
	 */
	Field createPrimaryField(String name, Boolean autoIncrement);

	/**
	 * 根据数据源创建一个对应的实体字符串字段
	 * 
	 * @param name
	 *            字段名称
	 * @param nullable
	 *            是否可空
	 * @param join
	 *            关联名称
	 * @param alias
	 *            别名
	 * @param defaultValue
	 *            默认值
	 * @param length
	 *            长度
	 * @param comment
	 *            注释
	 * @return 字符串字段
	 */
	Field createStringField(String name, Boolean nullable, String join, String alias, String defaultValue,
			Integer length, String comment);

	/**
	 * 根据数据源创建一个对应的实体的整数字段
	 * 
	 * @param name
	 *            字段名称
	 * @param nullable
	 *            是否可空
	 * @param join
	 *            关联名称
	 * @param alias
	 *            别名
	 * @param defaultValue
	 *            默认值
	 * @param length
	 *            长度
	 * @param comment
	 *            注释
	 * @return 整数字段
	 */
	Field createIntegerField(String name, Boolean nullable, String join, String alias, String defaultValue,
			Integer length, String comment);

	/**
	 * 根据数据源创建一个对应的实体的bool字段
	 * 
	 * @param name
	 *            字段名称
	 * @param nullable
	 *            是否可空
	 * @param join
	 *            关联名称
	 * @param alias
	 *            别名
	 * @param defaultValue
	 *            默认值
	 * @param comment
	 *            注释
	 * @return bool字段
	 */
	Field createBooleanField(String name, Boolean nullable, String join, String alias, String defaultValue,
			String comment);

	/**
	 * 根据数据源创建一个对应的实体的小数字段
	 *
	 * @param name
	 *            字段名称
	 * @param nullable
	 *            是否可空
	 * @param join
	 *            关联名称
	 * @param alias
	 *            别名
	 * @param defaultValue
	 *            默认值
	 * @param length
	 *            长度
	 * @param scale
	 *            小数位数
	 * @param comment
	 *            注释
	 * @return 小数字段
	 */
	Field createFloatField(String name, Boolean nullable, String join, String alias, String defaultValue,
			Integer length, Integer scale, String comment);

	/**
	 * 根据数据源创建一个对应的实体的文本字段
	 * 
	 * @param name
	 *            字段名称
	 * @param nullable
	 *            是否可空
	 * @param join
	 *            关联名称
	 * @param alias
	 *            别名
	 * @param defaultValue
	 *            默认值
	 * @param comment
	 *            注释
	 * @return 文本字段
	 */
	Field createTextField(String name, Boolean nullable, String join, String alias, String defaultValue,
			String comment);

	/**
	 * 根据数据源创建一个对应的实体的json字段
	 * 
	 * @param name
	 *            字段名称
	 * @param nullable
	 *            是否可空
	 * @param join
	 *            关联名称
	 * @param alias
	 *            别名
	 * @param defaultValue
	 *            默认值
	 * @param comment
	 *            注释
	 * @return json字段
	 */
	Field createJsonField(String name, Boolean nullable, String join, String alias, String defaultValue,
			String comment);

	/**
	 * 根据数据源创建一个数据库对应的实体
	 * 
	 * @param table
	 *            主表名称
	 * @param pk
	 *            主键名称
	 * @param autoIncrement
	 *            是否自增长
	 * @param fields
	 *            字段定义
	 * @param filters
	 *            过滤条件
	 * @param groups
	 *            分组
	 * @param orders
	 *            排序
	 * @param redis
	 *            redis缓存数据库名称
	 * @param uniques
	 *            唯一性约束
	 * @param indexes
	 *            索引
	 * @param joins
	 *            关联
	 * @param comment
	 *            注释
	 * @return 实体
	 */
	Entity createEntity(String table, String pk, Boolean autoIncrement, Map<String, Field> fields, List<Filter> filters,
                        List<String> groups, List<Order> orders, Redis redis, List<String> uniques, List<String> indexes,
                        List<Join> joins, String comment);
}
