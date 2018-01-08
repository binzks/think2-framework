package org.think2framework.core.orm.database;

import java.util.*;

import org.think2framework.core.orm.bean.Filter;
import org.think2framework.core.orm.bean.Order;
import org.think2framework.core.exception.NonExistException;
import org.think2framework.core.orm.bean.SqlObject;
import org.think2framework.core.utils.StringUtils;

/**
 * 抽象数据库实体
 */
public abstract class AbstractEntity implements Entity {

	protected String table; // 主表名称

	protected String pk; // 主键名称

	protected Boolean autoIncrement; // 是否自增长

	protected Map<String, Field> fields; // 字段

	protected Integer page; // 第几页

	protected Integer size; // 每页大小

	protected Database database; // 对应数据库

	protected Redis redis; // 对应redis，可能为null

	protected List<Filter> filters; // 过滤条件

	protected List<String> groups; // 分组

	protected List<Order> orders; // 排序

	private String columnSql; // 自定义的查询字段

	public AbstractEntity(String table, String pk, Boolean autoIncrement, Map<String, Field> fields,
			List<Filter> filters, List<String> groups, List<Order> orders, Database database, Redis redis) {
		this.table = table;
		this.pk = pk;
		this.autoIncrement = autoIncrement;
		this.fields = fields;
		this.filters = filters;
		this.groups = groups;
		this.orders = orders;
		this.database = database;
		this.redis = redis;
	}

	/**
	 * 获取字段，如果字段不存在则抛出异常
	 *
	 * @param name
	 *            字段名称
	 * @return 字段
	 */
	private Field getField(String name) {
		Field field = fields.get(name);
		if (null == field) {
			throw new NonExistException("字段[" + name + "]");
		}
		return field;
	}

	/**
	 * 生成实体对应的表创建sql
	 * 
	 * @return 创建sql
	 */
	public abstract String createSql();

	/**
	 * 根据待新增的对象生成新增sql和值，如果是map类型特殊处理
	 * 
	 * @param instance
	 *            对象数据
	 * @param id
	 *            如果不是自增长，则是guid，否则为null
	 * @return 新增sql和值
	 */
	public abstract SqlObject createInsert(Object instance, Object id);

	/**
	 * 根据待修改的数据生成修改sql和值，如果是map类型特殊处理
	 * 
	 * @param instance
	 *            对象数据
	 * @param keys
	 *            作为修改条件的关键字段
	 * @return 修改sql和值
	 */
	public abstract SqlObject createUpdate(Object instance, String... keys);

	/**
	 * 获取实体查询的时候默认的查询字段
	 * 
	 * @return 查询字段
	 */
	public abstract String getDefaultFields();

	/**
	 * 生成查询sql和值，将查询字段、关联、查询条件和分组拼成sql语句
	 * 
	 * @param fields
	 *            查询字段，默认为全实体的所有字段
	 * @return 查询sql和值
	 */
	public abstract SqlObject createSelect(String fields);

	/**
	 * 获取查询的字段，如果自定义字段不为空则返回自定义字段，如果为空则获取默认查询字段
	 * 
	 * @return 查询字段
	 */
	private String getSelectFields() {
		return StringUtils.isBlank(columnSql) ? getDefaultFields() : columnSql;
	}

	@Override
	public boolean create() {
		Boolean result = false;
		try {
			database.queryForList("SELECT 1 FROM " + table + " WHERE 1=2");
		} catch (Exception e) {
			if (null != e.getCause()) {
				String msg = e.getCause().getMessage();
				if (msg.contains("no such table") || (msg.contains("Table") && msg.contains("doesn't exist"))) {
					database.update(createSql());
					result = true;
				} else {
					throw new RuntimeException(e);
				}
			} else {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	@Override
	public Object insert(Object instance) {
		Object id = null;
		if (autoIncrement) {
			id = UUID.randomUUID().toString();
		}
		SqlObject sqlObject = createInsert(instance, id);
		return database.insert(sqlObject.getSql(), autoIncrement, sqlObject.getValues());
	}

	@Override
	public <T> int[] insert(List<T> list) {
		String sql = null;
		List<Object[]> batchArgs = new ArrayList<>();
		for (Object object : list) {
			Object id = null;
			if (autoIncrement) {
				id = UUID.randomUUID().toString();
			}
			SqlObject sqlObject = createInsert(object, id);
			if (StringUtils.isBlank(sql)) {
				sql = sqlObject.getSql();
			}
			batchArgs.addAll(Collections.singleton(sqlObject.getValues().toArray()));
		}
		return database.batchUpdate(sql, batchArgs);
	}

	@Override
	public int update(Object instance, String... keys) {
		SqlObject sqlObject = createUpdate(instance, keys);
		return database.update(sqlObject.getSql(), sqlObject.getValues());
	}

	@Override
	public <T> int[] update(List<T> list, String... keys) {
		String sql = null;
		List<Object[]> batchArgs = new ArrayList<>();
		for (Object object : list) {
			SqlObject sqlObject = createUpdate(object, keys);
			if (StringUtils.isBlank(sql)) {
				sql = sqlObject.getSql();
			}
			batchArgs.addAll(Collections.singleton(sqlObject.getValues().toArray()));
		}
		return database.batchUpdate(sql, batchArgs);
	}

	@Override
	public Object save(Object instance, String... keys) {
		if (update(instance, keys) == 1) {
			return -1;
		} else {
			return insert(instance);
		}
	}

	@Override
	public <T> String[] save(List<T> list, String... keys) {
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = save(list.get(i), keys).toString();
		}
		return result;
	}

	@Override
	public int delete(Object id) {
		return delete(pk, id);
	}

	@Override
	public int queryForCount() {
		return queryForCount("*");
	}

	@Override
	public int queryForCount(String field) {
		String key = field;
		if (!"*".equals(key)) {
			Field f = getField(field);
			key = f.key();
		}
		SqlObject sqlObject = createSelect(key);
		Integer result = database.queryForObject(sqlObject.getSql(), sqlObject.getValues().toArray(), Integer.class);
		if (null == result) {
			result = 0;
		}
		return result;
	}

	@Override
	public Map<String, Object> queryForMap() {
		SqlObject sqlObject = createSelect(getSelectFields());
		return database.queryForMap(sqlObject.getSql(), sqlObject.getValues());
	}

	@Override
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

	@Override
	public <T> T queryForObject(Class<T> requiredType) {
		SqlObject sqlObject = createSelect(getSelectFields());
		return database.queryForObject(sqlObject.getSql(), sqlObject.getValues().toArray(), requiredType);
	}

	@Override
	public <T> T queryForObject(Class<T> requiredType, String key, Integer valid) {
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

	@Override
	public <T> List<T> queryForList(Class<T> elementType) {
		SqlObject sqlObject = createSelect(getSelectFields());
		return database.queryForList(sqlObject.getSql(), sqlObject.getValues().toArray(), elementType);
	}

	@Override
	public <T> List<T> queryForList(Class<T> elementType, String key, Integer valid) {
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

	@Override
	public List<Map<String, Object>> queryForList() {
		SqlObject sqlObject = createSelect(getSelectFields());
		return database.queryForList(sqlObject.getSql(), sqlObject.getValues());
	}

	@Override
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

	@Override
	public void setRedis(String key, Integer valid, Object value) {
		if (null != redis) {
			redis.set(key, valid, value);
		}
	}

	@Override
	public String getRedis(String key) {
		if (null != redis) {
			return redis.get(key);
		} else {
			return null;
		}
	}

	@Override
	public void deleteRedis(String... keys) {
		if (null != redis) {
			redis.delete(keys);
		}
	}

	@Override
	public void deleteKeys(String prefix) {
		if (null != redis) {
			redis.deleteKeys(prefix);
		}
	}

	@Override
	public void fields(String... names) {
		StringBuilder sql = new StringBuilder();
		for (String name : names) {
			Field field = getField(name);
			sql.append(",").append(field.aliasKey());
		}
		columnSql = StringUtils.substring(sql.toString(), 1);
	}

	@Override
	public void clear() {
		this.filters.clear();
		this.columnSql = "";
	}

	@Override
	public void page(int page, int size) {
		this.page = page;
		this.size = size;
	}

	@Override
	public void group(String... keys) {
		for (String key : keys) {
			Field field = getField(key);
			groups.add(field.key());
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
			Field field = getField(key);
			realKeys.add(field.key());
		}
		orders.add(new Order(realKeys, sort));
	}

	@Override
	public void desc(String... keys) {
		sort(Sort.DESC, keys);
	}

	@Override
	public void asc(String... keys) {
		sort(Sort.ASC, keys);
	}

	@Override
	public void filter(String key, Operator operator, Object... values) {
		Field field = getField(key);
		this.filters.add(new Filter(field.key(), operator, Arrays.asList(values)));
	}

	@Override
	public void eq(Object value) {
		filter(pk, Operator.EQUAL, value);
	}

	@Override
	public void eq(String key, Object value) {
		filter(key, Operator.EQUAL, value);
	}

	@Override
	public void ne(String key, Object value) {
		filter(key, Operator.NOT_EQUAL, value);
	}

	@Override
	public void gt(String key, Object value) {
		filter(key, Operator.GREATER_THAN, value);
	}

	@Override
	public void ge(String key, Object value) {
		filter(key, Operator.GREATER_EQUAL, value);
	}

	@Override
	public void lt(String key, Object value) {
		filter(key, Operator.LESS_THAN, value);
	}

	@Override
	public void le(String key, Object value) {
		filter(key, Operator.LESS_EQUAL, value);
	}

	@Override
	public void isNull(String key) {
		filter(key, Operator.IS_NULL);
	}

	@Override
	public void notNull(String key) {
		filter(key, Operator.NOT_NULL);
	}

	@Override
	public void in(String key, Object... values) {
		filter(key, Operator.IN, values);
	}

	@Override
	public void notIn(String key, Object... values) {
		filter(key, Operator.NOT_IN, values);
	}

	@Override
	public void between(String key, Object begin, Object end) {
		filter(key, Operator.BETWEEN, begin, end);
	}

	@Override
	public void like(String key, Object value) {
		filter(key, Operator.LIKE, value);
	}

	@Override
	public void notLike(String key, Object value) {
		filter(key, Operator.NOT_LIKE, value);
	}

	@Override
	public void leftLike(String key, Object value) {
		filter(key, Operator.LIKE, "%" + value);
	}

	@Override
	public void rightLike(String key, Object value) {
		filter(key, Operator.LIKE, value + "%");
	}
}
