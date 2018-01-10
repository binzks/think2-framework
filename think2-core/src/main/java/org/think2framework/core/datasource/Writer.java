package org.think2framework.core.datasource;

import java.util.List;

public interface Writer {

	/**
	 * 创建实体对应的数据库主表，如果创建返回true，表已存在没有创建返回false
	 *
	 * @return 是否创建了表
	 */
	boolean create();

	/***
	 * 新增数据，返回新增数据的数据id，如果是map类型则转换为map获取,其他根据字段定义获取
	 *
	 * @param instance
	 *            待新增的数据
	 * @return 返回新增数据的id
	 */
	Object insert(Object instance);

	/**
	 * 批量新增,以第一条数据生成新增sql语句,所有批量数据必须有相同的结构
	 *
	 * @param list
	 *            批量新增数据
	 */
	<T> int[] insert(List<T> list);

	/**
	 * 修改数据，数据key字段必须有值，如果是map类型则转换为map获取，其他根据字段定义获取，不修改数据值为null的列
	 *
	 * @param instance
	 *            待修改的数据
	 * @param keys
	 *            过滤字段
	 * @return 返回影响的数据行数
	 */
	int update(Object instance, String... keys);

	/**
	 * 批量修改,以第一条数据生成修改sql语句,所有批量数据必须有相同的结构
	 *
	 * @param list
	 *            批量修改数据
	 */
	<T> int[] update(List<T> list, String... keys);

	/**
	 * 保存数据,如果数据不存在则新增,存在则修改,判断依据key字段的值
	 *
	 * @param instance
	 *            待保存的数据
	 * @param keys
	 *            过滤字段的名称
	 * @return 新增数据返回id, 修改数据返回-1
	 */
	Object save(Object instance, String... keys);

	/**
	 * 批量保存数据,如果数据不存在则新增,存在则修改,判断依据key字段值
	 *
	 * @param list
	 *            保存数据数组
	 * @param keys
	 *            关键字段
	 * @return 保存结果, 新增数据返回id, 修改数据返-1
	 */
	<T> String[] save(List<T> list, String... keys);

	/**
	 * 根据主键值删除数据
	 *
	 * @param id
	 *            要删除数据的id
	 * @return 返回影响的行数
	 */
	int delete(Object id);

	/**
	 * 根据字段值删除数据
	 *
	 * @param key
	 *            字段名称
	 * @param value
	 *            字段值
	 * @return 返回影响的行数
	 */
	int delete(String key, Object value);

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
	 * 执行一个自定义批量写入sql
	 *
	 * @param sql
	 *            sql语句
	 * @param batchArgs
	 *            参数值
	 * @return 返回影响的行数数组
	 */
	int[] batchUpdate(String sql, List<Object[]> batchArgs);

}
