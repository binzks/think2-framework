package org.think2framework.core.datasource;

import java.util.List;
import java.util.Map;

import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.persistence.FieldType;

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
	 * 根据数据源创建一个对应的实体的字段
	 * 
	 * @param fieldType
	 *            字段类型
	 * @param name
	 *            字段名称
	 * @param nullable
	 *            是否可空
	 * @param join
	 *            关联名称
	 * @param alias
	 *            别名
	 * @param length
	 *            长度
	 * @param scale
	 *            小数位数
	 * @param comment
	 *            注释
	 * @return 字段
	 */
	Field createField(FieldType fieldType, String name, Boolean nullable, String join, String alias, Integer length,
			Integer scale, String comment);

	/**
	 * 根据数据源创建一个查询生成器
	 * 
	 * @param table
	 *            主表名称
	 * @param pk
	 *            主键名称
	 * @param fields
	 *            字段
	 * @param filters
	 *            默认过滤条件
	 * @param groups
	 *            分组
	 * @param orders
	 *            排序
	 * @param joins
	 *            关联
	 * @param redis
	 *            缓存redis
	 * @return 查询生成器
	 */
	Query createQuery(String table, String pk, Map<String, Field> fields, List<Filter> filters, List<String> groups,
			List<Order> orders, List<Join> joins, Redis redis);

	/**
	 * 根据数据源创建一个写入生成器
	 * 
	 * @param table
	 *            主表名称
	 * @param pk
	 *            主键名称
	 * @param autoIncrement
	 *            是否自增长
	 * @param fields
	 *            字段
	 * @param uniques
	 *            唯一性约束
	 * @param indexes
	 *            索引
	 * @param comment
	 *            注释
	 * @return 写入生成器
	 */
	Writer createWriter(String table, String pk, Boolean autoIncrement, Map<String, Field> fields, List<String> uniques,
			List<String> indexes, String comment);

}
