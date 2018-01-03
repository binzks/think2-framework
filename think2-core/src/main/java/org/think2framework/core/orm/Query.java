package org.think2framework.core.orm;

import java.util.*;

import org.think2framework.core.bean.Column;
import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.exception.NonExistException;
import org.think2framework.core.orm.bean.SqlObject;
import org.think2framework.core.orm.database.Database;
import org.think2framework.core.orm.database.Operator;
import org.think2framework.core.orm.database.Redis;
import org.think2framework.core.orm.database.Sort;
import org.think2framework.core.utils.JsonUtils;
import org.think2framework.core.utils.StringUtils;

/**
 * 查询生成器
 */
public class Query {

	private String table; // 主表名称

	private String pk; // 主键名称

	private Map<String, Column> columns; // 列

	private List<Filter> filters;// 过滤条件

	private int page;// 分页第几页

	private int size;// 每页大小

	private String joinSql; // 关联sql

	private List<String> groups;// 分组字段

	private List<Order> orders;// 排序

	private String defaultColumnSql; // 默认查询字段sql

	private String columnSql;// 查询的字段sql

	private Redis redis;// redis处理工具

	private Database database;// 处理数据库

	public Query(String table, String pk, Map<String, Column> columns, List<Filter> filters, List<String> groups,
			List<Order> orders, List<Join> joins, Redis redis, Database database) {
		this.table = table;
		this.pk = pk;
		this.columns = columns;
		// 添加默认过滤条件为实体的过滤条件，如果实体过滤条件为null则创建过滤条件对象
		this.filters = new ArrayList<>();
		if (null != filters) {
			this.filters.addAll(filters);
		}
		// 添加默认分组
		this.groups = new ArrayList<>();
		if (null != groups) {
			this.groups.addAll(groups);
		}
		// 添加默认排序
		this.orders = new ArrayList<>();
		if (null != orders) {
			this.orders.addAll(orders);
		}
		this.redis = redis;
		this.database = database;
		if (null == joins || joins.size() == 0) {
			this.joinSql = "";
		} else {
			StringBuilder joinSql = new StringBuilder();
			for (Join join : joins) {
				joinSql.append(join.toString()).append(" ");
				if (StringUtils.isNotBlank(join.getDatabase())) {
					joinSql.append(join.getDatabase()).append(".");
				}
				joinSql.append(join.getTable()).append(" AS ").append(join.getName()).append(" ON ")
						.append(join.getName()).append(".").append(join.getKey()).append("=");
				if (StringUtils.isBlank(join.getJoinName())) {
					joinSql.append(Database.MAIN_TABLE_ALIAS);
				} else {
					joinSql.append(join.getJoinName());
				}
				joinSql.append(".").append(join.getJoinKey());
				if (StringUtils.isNotBlank(join.getFilter())) {
					joinSql.append(" ").append(join.getFilter());
				}
			}
			this.joinSql = joinSql.toString();
		}
		if (null == columns && columns.size() == 0) {
			this.defaultColumnSql = "";
		} else {
			StringBuilder sql = new StringBuilder();
			for (Column column : columns.values()) {
				sql.append(",").append(getColumnKeySql(column.getName(), true));
			}
			this.defaultColumnSql = sql.substring(1);
		}
	}

	/**
	 * 获取字段对应的查询sql,表别名.字段名 AS 别名
	 *
	 * @param name
	 *            字段名称
	 * @param alias
	 *            是否获取别名
	 * @return 表别名.字段名 AS 别名
	 */
	private String getColumnKeySql(String name, Boolean alias) {
		Column column = columns.get(name);
		if (null == column) {
			throw new NonExistException("字段[" + name + "]");
		}
		StringBuilder sql = new StringBuilder();
		sql.append(StringUtils.isBlank(column.getJoin()) ? Database.MAIN_TABLE_ALIAS : column.getJoin()).append(".")
				.append(column.getName());
		if (StringUtils.isNotBlank(column.getAlias()) && alias) {
			sql.append(" AS ").append(column.getAlias());
		}
		return sql.toString();
	}

	/**
	 * 获取数量
	 *
	 * @param field
	 *            统计字段名
	 * @return 数量
	 */
	public int queryForCount(String field) {
		String key = field;
		if (!"*".equals(key)) {
			key = getColumnKeySql(field, false);
		}
		SqlObject sqlObject = database.createSelect(table, joinSql, "COUNT(" + key + ")", filters, groups, orders, page,
				size);
		Integer result = queryForObject(sqlObject.getSql(), sqlObject.getValues().toArray(), Integer.class);
		if (null == result) {
			result = 0;
		}
		return result;
	}

	/**
	 * 获取数量
	 *
	 * @return 数量
	 */
	public int queryForCount() {
		return queryForCount("*");
	}

	/**
	 * 获取单体map数据
	 *
	 * @return map数据
	 */
	public Map<String, Object> queryForMap() {
		SqlObject sqlObject = database.createSelect(table, joinSql, getColumnSql(), filters, groups, orders, page,
				size);
		return queryForMap(sqlObject.getSql(), sqlObject.getValues());
	}

	/**
	 * 获取单体map数据，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis关键字
	 * @param valid
	 *            redis缓存时间，0为永久有效
	 * @return map数据
	 */
	public Map<String, Object> queryForMap(String key, Integer valid) {
		Map<String, Object> map = null;
		if (null != redis) {
			map = redis.getMap(key);
		}
		if (null == map) {
			map = queryForMap();
			if (null != redis) {
				redis.set(key, valid, map);
			}
		}
		return map;
	}

	/**
	 * 获取单体对象数据
	 *
	 * @param requiredType
	 *            数据对象类
	 * @return 数据对象
	 */
	public <T> T queryForObject(Class<T> requiredType) {
		SqlObject sqlObject = database.createSelect(table, joinSql, getColumnSql(), filters, groups, orders, page,
				size);
		return queryForObject(sqlObject.getSql(), sqlObject.getValues().toArray(), requiredType);
	}

	/**
	 * 获取单体对象数据，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis关键字
	 * @param valid
	 *            redis缓存时间，0为永久有效
	 * @param requiredType
	 *            数据对象类
	 * @return 数据对象
	 */
	public <T> T queryForObject(String key, Integer valid, Class<T> requiredType) {
		T object = null;
		if (null != redis) {
			object = redis.get(key, requiredType);
		}
		if (null == object) {
			object = queryForObject(requiredType);
			if (null != redis) {
				redis.set(key, valid, object);
			}
		}
		return object;
	}

	/**
	 * 获取数据对象数组
	 *
	 * @param elementType
	 *            数据对象类
	 * @return 数据数组
	 */
	public <T> List<T> queryForList(Class<T> elementType) {
		SqlObject sqlObject = database.createSelect(table, joinSql, getColumnSql(), filters, groups, orders, page,
				size);
		return queryForList(sqlObject.getSql(), sqlObject.getValues().toArray(), elementType);
	}

	/**
	 * 获取数据对象数组，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis关键字
	 * @param valid
	 *            redis缓存时间，0为永久有效
	 * @param elementType
	 *            数据对象类
	 * @return 数据数组
	 */
	public <T> List<T> queryForList(String key, Integer valid, Class<T> elementType) {
		List<T> list = null;
		if (null != redis) {
			list = redis.getList(key, elementType);
		}
		if (null == list) {
			list = queryForList(elementType);
			if (null != redis) {
				redis.set(key, valid, list);
			}
		}
		return list;
	}

	/**
	 * 获取map数据数组
	 *
	 * @return map数组
	 */
	public List<Map<String, Object>> queryForList() {
		SqlObject sqlObject = database.createSelect(table, joinSql, getColumnSql(), filters, groups, orders, page,
				size);
		return queryForList(sqlObject.getSql(), sqlObject.getValues());
	}

	/**
	 * 获取数据，返回map数组的json字符串，如果有redis缓存则走缓存机制
	 *
	 * @param key
	 *            redis关键字
	 * @param valid
	 *            redis缓存时间，0为永久有效
	 * @return map数组的json字符串
	 */
	public String query(String key, Integer valid) {
		String data = null;
		if (null != redis) {
			data = redis.get(key);
		}
		if (null == data) {
			List<Map<String, Object>> list = queryForList();
			data = JsonUtils.toString(list);
			if (null != redis) {
				redis.set(key, valid, list);
			}
		}
		return data;
	}

	/**
	 * 获取map数据数组，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis关键字
	 * @param valid
	 *            redis缓存时间，0为永久有效
	 * @return map数组
	 */
	public List<Map<String, Object>> queryForList(String key, Integer valid) {
		List<Map<String, Object>> list = null;
		if (null != redis) {
			list = redis.getMapList(key);
		}
		if (null == list) {
			list = queryForList();
			if (null != redis) {
				redis.set(key, valid, list);
			}
		}
		return list;
	}

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
	public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) {
		return database.queryForObject(sql, args, requiredType);
	}

	/**
	 * 执行一个自定义sql,获取单体类对象，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis关键字
	 * @param valid
	 *            redis缓存时间，0为永久有效
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @param requiredType
	 *            类
	 * @return 类对象
	 */
	public <T> T queryForObject(String key, Integer valid, String sql, Object[] args, Class<T> requiredType) {
		T object = null;
		if (null != redis) {
			object = redis.get(key, requiredType);
		}
		if (null == object) {
			object = queryForObject(sql, args, requiredType);
			if (null != redis) {
				redis.set(key, valid, object);
			}
		}
		return object;
	}

	/**
	 * 执行一个自定义sql,获取map对象
	 *
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @return map
	 */
	public Map<String, Object> queryForMap(String sql, Object... args) {
		return database.queryForMap(sql, args);
	}

	/**
	 * 执行一个自定义sql,获取map对象，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis关键字
	 * @param valid
	 *            redis缓存时间，0为永久有效
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @return map
	 */
	public Map<String, Object> queryForMap(String key, Integer valid, String sql, Object... args) {
		Map<String, Object> map = null;
		if (null != redis) {
			map = redis.getMap(key);
		}
		if (null == map) {
			map = queryForMap(sql, args);
			if (null != redis) {
				redis.set(key, valid, map);
			}
		}
		return map;
	}

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
	public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) {
		return database.queryForList(sql, args, elementType);
	}

	/**
	 * 执行一个自定义sql,获取对象数组，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis关键字
	 * @param valid
	 *            redis缓存时间，0为永久有效
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @param elementType
	 *            类
	 * @return 类对象数组
	 */
	public <T> List<T> queryForList(String key, Integer valid, String sql, Object[] args, Class<T> elementType) {
		List<T> list = null;
		if (null != redis) {
			list = redis.getList(key, elementType);
		}
		if (null == list) {
			list = queryForList(sql, args, elementType);
			if (null != redis) {
				redis.set(key, valid, list);
			}
		}
		return list;
	}

	/**
	 * 执行一个自定义sql,获取map数组
	 *
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数
	 * @return map数组
	 */
	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		return database.queryForList(sql, args);
	}

	/**
	 * 执行一个sql语句，获取map数组，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis缓存key
	 * @param valid
	 *            redis缓存时间，0为永久有效
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数
	 * @return map数组
	 */
	public List<Map<String, Object>> queryForList(String key, Integer valid, String sql, Object... args) {
		List<Map<String, Object>> list = null;
		if (null != redis) {
			list = redis.getMapList(key);
		}
		if (null == list) {
			list = queryForList(sql, args);
			if (null != redis) {
				redis.set(key, valid, list);
			}
		}
		return list;
	}

	/**
	 * 获取查询的字段sql，如果未设置则查询全部字段
	 *
	 * @return 查询字段sql
	 */
	private String getColumnSql() {
		return StringUtils.isBlank(columnSql) ? defaultColumnSql : columnSql;
	}

	/**
	 * 设置自定义查询字段，不进行相关校验，由使用者自定义任何形式的字段
	 *
	 * @param fields
	 *            自定义字段
	 */
	public void customFields(String fields) {
		this.columnSql = fields;
	}

	/**
	 * 批量设置要查询的字段，设置的时候清空之前设置的字段
	 *
	 * @param names
	 *            要查询的字段名称
	 */
	public void fields(String... names) {
		StringBuilder sql = new StringBuilder();
		for (String name : names) {
			sql.append(",").append(getColumnKeySql(name, true));
		}
		columnSql = StringUtils.substring(sql.toString(), 1);
	}

	/**
	 * 清空所有查询设置
	 */
	public void clear() {
		this.filters.clear();
		this.columnSql = "";
	}

	/**
	 * 添加分页
	 *
	 * @param page
	 *            第几页
	 * @param size
	 *            每页大小
	 */
	public void page(int page, int size) {
		this.page = page;
		this.size = size;
	}

	/**
	 * 添加group by条件，没有顺序，并且字段名不能重复,多次调用以最后一次为准
	 *
	 * @param keys
	 *            group by字段名称
	 */
	public void group(String... keys) {
		for (String key : keys) {
			groups.add(getColumnKeySql(key, false));
		}
	}

	/**
	 * 添加一个排序
	 * 
	 * @param sort
	 *            排序类型
	 * @param keys
	 *            排序字段
	 */
	private void sort(Sort sort, String... keys) {
		List<String> realKeys = new ArrayList<>();
		for (String key : keys) {
			realKeys.add(getColumnKeySql(key, false));
		}
		orders.add(new Order(realKeys, sort));
	}

	/**
	 * 添加倒叙排序，按照添加先后顺序排序
	 *
	 * @param keys
	 *            order by字段名称
	 */
	public void desc(String... keys) {
		sort(Sort.DESC, keys);
	}

	/**
	 * 添加正序排序，按照添加先后顺序排序
	 *
	 * @param keys
	 *            order by字段名称
	 */
	public void asc(String... keys) {
		sort(Sort.ASC, keys);
	}

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
	public void filter(String key, Operator operator, Object... values) {
		this.filters.add(new Filter(getColumnKeySql(key, false), operator, Arrays.asList(values)));
	}

	/**
	 * and主键等于条件
	 *
	 * @param value
	 *            过滤值
	 */
	public void eq(Object value) {
		this.filters.add(new Filter(pk, Operator.EQUAL, Arrays.asList(value)));
	}

	/**
	 * and等于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void eq(String key, Object value) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.EQUAL, Arrays.asList(value)));
	}

	/**
	 * and不等于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void ne(String key, Object value) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.NOT_EQUAL, Arrays.asList(value)));
	}

	/**
	 * and大于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void gt(String key, Object value) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.GREATER_THAN, Arrays.asList(value)));
	}

	/**
	 * and大于等于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void ge(String key, Object value) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.GREATER_EQUAL, Arrays.asList(value)));
	}

	/**
	 * and小于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void lt(String key, Object value) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.LESS_THAN, Arrays.asList(value)));
	}

	/**
	 * and小于等于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void le(String key, Object value) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.LESS_EQUAL, Arrays.asList(value)));
	}

	/**
	 * and null过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 */
	public void isNull(String key) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.IS_NULL, null));
	}

	/**
	 * and not null过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 */
	public void notNull(String key) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.NOT_NULL, null));
	}

	/**
	 * and in过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param values
	 *            过滤值
	 */
	public void in(String key, Object... values) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.IN, Arrays.asList(values)));
	}

	/**
	 * and not in过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param values
	 *            过滤值
	 */
	public void notIn(String key, Object... values) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.NOT_IN, Arrays.asList(values)));
	}

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
	public void between(String key, Object begin, Object end) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.BETWEEN, Arrays.asList(begin, end)));
	}

	/**
	 * and like过滤条件，过滤值两边都加上%过滤
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void like(String key, Object value) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.LIKE, Collections.singletonList(value)));
	}

	/**
	 * and not like过滤条件，过滤值两边都加上%过滤
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void notLike(String key, Object value) {
		this.filters.add(new Filter(getColumnKeySql(key, false), Operator.NOT_LIKE, Collections.singletonList(value)));
	}

	/**
	 * and like过滤条件，左匹配
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void leftLike(String key, Object value) {
		this.filters
				.add(new Filter(getColumnKeySql(key, false), Operator.LIKE, Collections.singletonList("%" + value)));
	}

	/**
	 * and like过滤条件，右匹配
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void rightLike(String key, Object value) {
		this.filters
				.add(new Filter(getColumnKeySql(key, false), Operator.LIKE, Collections.singletonList(value + "%")));
	}

}