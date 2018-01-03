package org.think2framework.core.orm;

import org.think2framework.core.bean.Column;
import org.think2framework.core.orm.database.Database;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 写入生成器
 */
public class Writer {

	private String table; // 表名

	private String pk; // 主键名称

	private boolean autoIncrement; // 是否自增长

	private List<String> uniques; // 唯一性约束

	private List<String> indexes; // 索引

	private String comment; // 表的注释

	private Map<String, Column> columns; // 列

	private Database database; // 处理数据库

	public Writer(String table, String pk, boolean autoIncrement, List<String> uniques, List<String> indexes,
			String comment, Map<String, Column> columns, Database database) {
		this.table = table;
		this.pk = pk;
		this.autoIncrement = autoIncrement;
		this.uniques = uniques;
		this.indexes = indexes;
		this.comment = comment;
		this.columns = columns;
		this.database = database;
	}

	/**
	 * 如果模型对应的表不存在则创建,已经存在则不处理,如果表有变动则返回true,如果没有变动则返回false
	 *
	 * @return 是否创建了表
	 */
	public boolean createTable() {
		return database.createTable(table, pk, autoIncrement, uniques, indexes, comment, columns);
	}

	/***
	 * 新增数据，返回新增数据的数据id，如果是map类型则转换为map获取,其他根据字段定义获取
	 *
	 * @param instance
	 *            待新增的数据
	 * @return 返回新增数据的id
	 */
	public Object insert(Object instance) {
		return database.insert(instance, table, pk, autoIncrement, columns);
	}

	/**
	 * 修改数据，数据key字段必须有值，如果是map类型则转换为map获取，其他根据字段定义获取，不修改数据值为null的列
	 *
	 * @param instance
	 *            待修改的数据
	 * @param keys
	 *            过滤字段
	 * @return 返回影响的数据行数
	 */
	public int update(Object instance, String... keys) {
		return database.update(instance, table, pk, columns, keys);
	}

	/**
	 * 保存数据,如果数据不存在则新增,存在则修改,判断依据key字段的值
	 *
	 * @param instance
	 *            待保存的数据
	 * @param keys
	 *            过滤字段的名称
	 * @return 新增数据返回id, 修改数据返回-1
	 */
	public Object save(Object instance, String... keys) {
		if (update(instance, keys) == 1) {
			return -1;
		} else {
			return insert(instance);
		}
	}

	/**
	 * 批量保存数据,如果数据不存在则新增,存在则修改,判断依据key字段值
	 *
	 * @param list
	 *            保存数据数组
	 * @param keys
	 *            关键字段
	 * @return 保存结果, 新增数据返回id, 修改数据返-1
	 */
	public <T> String[] batchSave(List<T> list, String... keys) {
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = save(list.get(i), keys).toString();
		}
		return result;
	}

	/**
	 * 批量新增,以第一条数据生成新增sql语句,所有批量数据必须有相同的结构
	 *
	 * @param list
	 *            批量新增数据
	 */
	public <T> int[] batchInsert(List<T> list) {
		return database.batchInsert(list, table, pk, autoIncrement, columns);
	}

	/**
	 * 批量修改,以第一条数据生成修改sql语句,所有批量数据必须有相同的结构
	 *
	 * @param list
	 *            批量修改数据
	 */
	public <T> int[] batchUpdate(List<T> list) {
		return database.batchUpdate(list, table, pk, columns);
	}

	/**
	 * 根据主键值删除数据
	 *
	 * @param id
	 *            要删除数据的id
	 * @return 返回影响的行数
	 */
	public int deleteById(Object id) {
		return delete(Collections.singletonList(id));
	}

	/**
	 * 根据字段值删除数据
	 *
	 * @param key
	 *            字段名称
	 * @param value
	 *            字段值
	 * @return 返回影响的行数
	 */
	public int delete(String key, Object value) {
		return delete(Collections.singletonList(value), key);
	}

	/**
	 * 删除数据
	 *
	 * @param values
	 *            过滤数据对应的条件字段值
	 * @param keys
	 *            过滤条件字段列表
	 * @return 影响行数
	 */
	public int delete(List<Object> values, String... keys) {
		return database.delete(table, pk, values, keys);
	}

	/**
	 * 执行一个自增长的sql,主键自增长
	 *
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @return 自增长主键值
	 */
	public Object insert(String sql, Object... args) {
		return database.insert(sql, true, args);
	}

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
	public Object insert(String sql, Boolean autoIncrement, Object... args) {
		return database.insert(sql, autoIncrement, args);
	}

	/**
	 * 执行一个自定义写入sql
	 *
	 * @param sql
	 *            sql语句
	 */
	public int update(String sql, Object... args) {
		return database.update(sql, args);
	}

	/**
	 * 执行一个自定义批量写入sql
	 *
	 * @param sql
	 *            sql语句
	 * @param batchArgs
	 *            参数值
	 * @return 返回影响的行数数组
	 */
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		return database.batchUpdate(sql, batchArgs);
	}

}
