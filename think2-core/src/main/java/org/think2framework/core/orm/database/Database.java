package org.think2framework.core.orm.database;

import java.util.List;
import java.util.Map;

import org.think2framework.core.bean.Column;
import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Order;
import org.think2framework.core.orm.bean.SqlObject;

/**
 * 数据库接口
 */
public interface Database {

	String MAIN_TABLE_ALIAS = "t"; // sql语句中主表的别名

	/**
	 * 如果模型对应的表不存在则创建,已经存在则不处理,如果表有变动则返回true,如果没有变动则返回false
	 * 
	 * @param table
	 *            数据库表名
	 * @param pk
	 *            主键名称
	 * @param autoIncrement
	 *            是否自增长
	 * @param uniques
	 *            唯一性约束
	 * @param indexes
	 *            索引
	 * @param comment
	 *            注释
	 * @param columns
	 *            列
	 * @return 是否创建了表
	 */
	boolean createTable(String table, String pk, boolean autoIncrement, List<String> uniques, List<String> indexes,
			String comment, Map<String, Column> columns);

	/**
	 * 删除数据
	 *
	 * @param values
	 *            过滤数据对应的条件字段值
	 * @param keys
	 *            过滤条件字段列表
	 * @return 影响行数
	 */
	int delete(String table, String pk, List<Object> values, String... keys);

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
	 * 新增数据，返回新增数据的数据id，如果是map类型则转换为map获取,其他根据字段定义获取
	 *
	 * @param instance
	 *            待新增的数据
	 * @param table
	 *            数据库表名
	 * @param pk
	 *            主键名称
	 * @param autoIncrement
	 *            是否自增长
	 * @param columns
	 *            列
	 * @return 新增数据id
	 */
	Object insert(Object instance, String table, String pk, boolean autoIncrement, Map<String, Column> columns);

	/**
	 * 修改数据，如果是map类型则转换为map获取，其他根据字段定义获取，不修改数据值为null的列，如果keys没有值则以主键作为修改条件
	 *
	 * @param instance
	 *            待修改的数据
	 * @param table
	 *            数据库表名
	 * @param pk
	 *            主键名称
	 * @param columns
	 *            列
	 * @param keys
	 *            过滤字段
	 * @return 返回影响的数据行数
	 */
	int update(Object instance, String table, String pk, Map<String, Column> columns, String... keys);

	/**
	 * 保存数据,如果数据不存在则新增,存在则修改,判断依据key字段的值，如果keys不存在则判断主键
	 *
	 * @param instance
	 *            待保存的数据
	 * @param table
	 *            数据库表名
	 * @param pk
	 *            主键名称
	 * @param autoIncrement
	 *            是否自增长
	 * @param columns
	 *            列
	 * @param keys
	 *            过滤字段的名称
	 * @return 新增数据返回id, 修改数据返回-1
	 */
	Object save(Object instance, String table, String pk, boolean autoIncrement, Map<String, Column> columns,
			String... keys);

	/**
	 * 批量新增数据，返回新增数据的数据id，如果是map类型则转换为map获取,其他根据字段定义获取，sql语句以第一个数据库对象为准
	 *
	 * @param list
	 *            待新增的数据
	 * @param table
	 *            数据库表名
	 * @param pk
	 *            主键名称
	 * @param autoIncrement
	 *            是否自增长
	 * @param columns
	 *            列
	 * @return 新增数据id
	 */
	<T> int[] batchInsert(List<T> list, String table, String pk, boolean autoIncrement, Map<String, Column> columns);

	/**
	 * 批量修改数据库，返回数据变化数量，如果是map类型则转换为map获取,其他根据字段定义获取，sql语句以第一个数据库对象为准
	 *
	 * @param list
	 *            待修改的数据库
	 * @param table
	 *            数据库表名
	 * @param pk
	 *            主键名称
	 * @param columns
	 *            列
	 * @param keys
	 *            修改关键字
	 * @return 受影响的数据数量
	 */
	<T> int[] batchUpdate(List<T> list, String table, String pk, Map<String, Column> columns, String... keys);

	/**
	 * 创建一个查询，生成sql语句和值
	 *
	 * @param table
	 *            数据库表名
	 * @param joinSql
	 *            关联sql
	 * @param fields
	 *            查询字段
	 * @param filters
	 *            过滤条件
	 * @param group
	 *            分组
	 * @param orders
	 *            排序
	 * @param page
	 *            查询第几页
	 * @param size
	 *            每页大小
	 * @return sql和值
	 */
	SqlObject createSelect(String table, String joinSql, String fields, List<Filter> filters, List<String> group,
			List<Order> orders, Integer page, Integer size);
}
