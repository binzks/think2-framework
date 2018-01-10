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
	 * 根据数据源创建一个查询生成器
	 * 
	 * @param table
	 *            主表名称
	 * @param pk
	 *            主键名称
	 * @param fields
	 *            字段
	 * @param redis
	 *            缓存redis
	 * @param filters
	 *            默认过滤条件
	 * @param groups
	 *            分组
	 * @param orders
	 *            排序
	 * @param joins
	 *            关联
	 * @return 查询生成器
	 */
	Query createQuery(String table, String pk, Map<String, Field> fields, Redis redis, List<Filter> filters,
			List<String> groups, List<Order> orders, List<Join> joins);

    /**
     * 根据数据源创建一个写入生成器
     * @param table 主表名称
     * @param pk 主键名称
     * @param autoIncrement 是否自增长
     * @param fields 字段
     * @param uniques
     * @param indexes
     * @param comment
     * @return
     */
	Writer createWriter(String table, String pk, Boolean autoIncrement, Map<String, Field> fields, List<String> uniques,
			List<String> indexes, String comment);

}
