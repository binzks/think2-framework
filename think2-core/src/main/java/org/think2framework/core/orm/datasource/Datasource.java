package org.think2framework.core.orm.datasource;

import java.util.List;
import java.util.Map;

import org.think2framework.core.orm.bean.Filter;
import org.think2framework.core.orm.bean.Join;
import org.think2framework.core.orm.bean.Order;

/**
 * 数据源接口
 */
public interface Datasource {

	/**
	 * 执行一个自定义新增方法,如果自增长则返回自增长的主键值,如果不是自增长则返回空
	 *
	 * @param sql
	 *            sql语句
	 * @param autoIncrement
	 *            主键是否自增长
	 * @param args
	 *            参数值
	 * @return 自增长则返回主键值, 否则返回空
	 */
	Object insert(String sql, Boolean autoIncrement, Object... args);

	/**
	 * 执行一个自定义写入sql
	 *
	 * @param sql
	 *            sql语句
	 */
	int update(String sql, Object... args);

	/**
	 * 执行一个自定义sql,获取单体类对象
	 *
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @param requiredType
	 *            类
	 * @return 类对象
	 */
	<T> T queryForObject(String sql, Object[] args, Class<T> requiredType);

	/**
	 * 执行一个自定义sql,获取map对象
	 *
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @return map
	 */
	Map<String, Object> queryForMap(String sql, Object... args);

	/**
	 * 执行一个自定义sql,获取对象数组
	 *
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @param elementType
	 *            类
	 * @return 类对象数组
	 */
	<T> List<T> queryForList(String sql, Object[] args, Class<T> elementType);

	/**
	 * 执行一个自定义sql,获取map数组
	 *
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数
	 * @return map数组
	 */
	List<Map<String, Object>> queryForList(String sql, Object... args);

	/**
	 * 执行一个自定义批量写入sql
	 *
	 * @param sql
	 *            sql语句
	 * @param batchArgs
	 *            参数值
	 * @return 返回影响的行数数组
	 */
	int[] batchUpdate(String sql, List<Object[]> batchArgs);

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
	 * @param defaultValue
	 *            默认值
	 * @param length
	 *            长度
	 * @param scale
	 *            小数位数
	 * @param comment
	 *            注释
	 * @return 字段
	 */
	Field createField(FieldType fieldType, String name, Boolean nullable, String join, String alias,
			String defaultValue, Integer length, Integer scale, String comment);

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
			List<String> groups, List<Order> orders, String redis, List<String> uniques, List<String> indexes,
			List<Join> joins, String comment);
}
