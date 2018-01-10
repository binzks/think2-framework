package org.think2framework.core.datasource;

import java.util.List;
import java.util.Map;

import org.think2framework.core.persistence.Operator;

/**
 * 写入生成器接口
 */
public interface Query {

	String MAIN_TABLE_ALIAS = "t"; // sql语句中主表的别名

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
	 * 获取查询数据返回的数量
	 *
	 * @return 数量
	 */
	int queryForCount();

	/**
	 * 获取查询数据返回的数量
	 *
	 * @param field
	 *            字段名称
	 * @return 数量
	 */
	int queryForCount(String field);

	/**
	 * 获取一个map数据
	 *
	 * @return map
	 */
	Map<String, Object> queryForMap();

	/**
	 * 获取一个map数据，如果启用了redis则先从redis获取，获取后数据存入redis缓存
	 *
	 * @param key
	 *            redis数据的key
	 * @param valid
	 *            redis数据失效时间，0表示永久有效
	 * @return map
	 */
	Map<String, Object> queryForMap(String key, Integer valid);

	/**
	 * 获取一个数据对象
	 *
	 * @param requiredType
	 *            对象类
	 * @return requiredType数据实体
	 */
	<T> T queryForObject(Class<T> requiredType);

	/**
	 * 获取一个数据对象，如果启用了redis则先从redis获取，获取后数据存入redis缓存
	 *
	 * @param requiredType
	 *            对象类
	 * @param key
	 *            redis数据的key
	 * @param valid
	 *            redis数据失效时间，0表示永久有效
	 * @return requiredType数据实体
	 */
	<T> T queryForObject(Class<T> requiredType, String key, Integer valid);

	/**
	 * 获取一个数据数组
	 *
	 * @param elementType
	 *            对象类
	 * @return 数据实体数组
	 */
	<T> List<T> queryForList(Class<T> elementType);

	/**
	 * 获取一个数据数组，如果启用了redis则先从redis获取，获取后数据存入redis缓存
	 *
	 * @param elementType
	 *            对象类
	 * @param key
	 *            redis数据的key
	 * @param valid
	 *            redis数据失效时间，0表示永久有效
	 * @return 数据实体数组
	 */
	<T> List<T> queryForList(Class<T> elementType, String key, Integer valid);

	/**
	 * 获取一个map数组
	 *
	 * @return map数组
	 */
	List<Map<String, Object>> queryForList();

	/**
	 * 获取一个map数组，如果启用了redis则先从redis获取，获取后数据存入redis缓存
	 *
	 * @param key
	 *            redis数据的key
	 * @param valid
	 *            redis数据失效时间，0表示永久有效
	 * @return map数组
	 */
	List<Map<String, Object>> queryForList(String key, Integer valid);

	/**
	 * 添加一个redis数据
	 *
	 * @param key
	 *            redis数据的key
	 * @param valid
	 *            redis数据失效时间，0表示永久有效
	 * @param value
	 *            缓存数据
	 * @return 是否缓存成功
	 */
	void setRedis(String key, Integer valid, Object value);

	/**
	 * 从redis获取缓存数据
	 *
	 * @param key
	 *            redis数据的key
	 * @return redis缓存数据
	 */
	String getRedis(String key);

	/**
	 * 从redis删除缓存数据
	 *
	 * @param keys
	 *            redis数据的key
	 */
	void deleteRedis(String... keys);

	/**
	 * 批量删除redis缓存，所有key前缀是prefix都删除
	 *
	 * @param prefix
	 *            redis数据的key的前缀
	 */
	void deleteKeys(String prefix);

	/**
	 * 批量设置要查询的字段，设置的时候清空之前设置的字段
	 *
	 * @param names
	 *            要查询的字段名称
	 */
	void fields(String... names);

	/**
	 * 清空所有查询设置
	 */
	void clear();

	/**
	 * 添加分页
	 *
	 * @param page
	 *            第几页
	 * @param size
	 *            每页大小
	 */
	void page(int page, int size);

	/**
	 * 添加group by条件，没有顺序，并且字段名不能重复,多次调用以最后一次为准
	 *
	 * @param keys
	 *            group by字段名称
	 */
	void group(String... keys);

	/**
	 * 添加倒叙排序，按照添加先后顺序排序
	 *
	 * @param keys
	 *            order by字段名称
	 */
	void desc(String... keys);

	/**
	 * 添加正序排序，按照添加先后顺序排序
	 *
	 * @param keys
	 *            order by字段名称
	 */
	void asc(String... keys);

	/**
	 * 追加一个过滤条件
	 *
	 * @param key
	 *            过滤字段名称
	 * @param operator
	 *            运算符
	 * @param values
	 *            过滤值
	 */
	void filter(String key, Operator operator, Object... values);

	/**
	 * and主键等于条件
	 *
	 * @param value
	 *            过滤值
	 */
	void eq(Object value);

	/**
	 * and等于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	void eq(String key, Object value);

	/**
	 * and不等于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	void ne(String key, Object value);

	/**
	 * and大于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	void gt(String key, Object value);

	/**
	 * and大于等于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	void ge(String key, Object value);

	/**
	 * and小于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	void lt(String key, Object value);

	/**
	 * and小于等于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	void le(String key, Object value);

	/**
	 * and null过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 */
	void isNull(String key);

	/**
	 * and not null过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 */
	void notNull(String key);

	/**
	 * and in过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param values
	 *            过滤值
	 */
	void in(String key, Object... values);

	/**
	 * and not in过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param values
	 *            过滤值
	 */
	void notIn(String key, Object... values);

	/**
	 * and between过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param begin
	 *            开始值
	 * @param end
	 *            结束值
	 */
	void between(String key, Object begin, Object end);

	/**
	 * and like过滤条件，过滤值两边都加上%过滤
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	void like(String key, Object value);

	/**
	 * and not like过滤条件，过滤值两边都加上%过滤
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	void notLike(String key, Object value);

	/**
	 * and like过滤条件，左匹配
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	void leftLike(String key, Object value);

	/**
	 * and like过滤条件，右匹配
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	void rightLike(String key, Object value);
}
