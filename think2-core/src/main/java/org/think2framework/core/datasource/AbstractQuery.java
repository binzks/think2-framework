package org.think2framework.core.datasource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.think2framework.core.ClassUtils;
import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Order;
import org.think2framework.core.bean.SqlObject;
import org.think2framework.core.exception.MessageException;
import org.think2framework.core.exception.SystemMessage;
import org.think2framework.core.utils.StringUtils;

/**
 * 抽象查询生成器
 */
public abstract class AbstractQuery implements Query {

	private static final Logger logger = LogManager.getLogger(AbstractQuery.class); // log4j日志对象

	protected String pk; // 主键名称

	protected Integer page; // 第几页

	protected Integer size; // 每页大小

	protected Map<String, Field> fields; // 字段

	private Redis redis; // 对应redis，可能为null

	protected List<Filter> filters; // 过滤条件

	protected List<String> groups; // 分组

	protected List<Order> orders; // 排序

	private String columnSql; // 自定义的查询字段

	private JdbcTemplate jdbcTemplate; // spring jdbcTemplate

	public AbstractQuery(String pk, Map<String, Field> fields, Redis redis, List<Filter> filters, List<String> groups,
			List<Order> orders, JdbcTemplate jdbcTemplate) {
		this.pk = pk;
		this.fields = fields;
		this.redis = redis;
		this.filters = filters;
		this.groups = groups;
		this.orders = orders;
		this.jdbcTemplate = jdbcTemplate;
	}

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
	public <T> T queryForObjectBySql(String sql, Object[] args, Class<T> requiredType) {
		logger.debug("queryForObject sql: {} values: {} requiredType: {}", sql, args, requiredType);
		try {
			return jdbcTemplate.query(sql, args, rs -> {
				rs.next();
				return ClassUtils.createInstance(requiredType, rs);
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Map<String, Object> queryForMapBySql(String sql, Object... args) {
		logger.debug("queryForMap sql: {} values: {}", sql, args);
		try {
			return jdbcTemplate.queryForMap(sql, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public <T> List<T> queryForListBySql(String sql, Object[] args, Class<T> elementType) {
		logger.debug("queryForList Object sql: {} values: {} elementType: {}", sql, args, elementType);
		try {
			return jdbcTemplate.query(sql, args, (rs, rowNum) -> ClassUtils.createInstance(elementType, rs));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> queryForListBySql(String sql, Object... args) {
		logger.debug("queryForList Map sql: {} values: {}", sql, args);
		try {
			return jdbcTemplate.queryForList(sql, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
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
			throw new MessageException(SystemMessage.NON_EXIST.getCode(), "字段[" + name + "]");
		}
		return field;
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
		Integer result = queryForObjectBySql(sqlObject.getSql(), sqlObject.getValues().toArray(), Integer.class);
		if (null == result) {
			result = 0;
		}
		return result;
	}

	@Override
	public Map<String, Object> queryForMap() {
		SqlObject sqlObject = createSelect(getSelectFields());
		return queryForMapBySql(sqlObject.getSql(), sqlObject.getValues());
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
		return queryForObjectBySql(sqlObject.getSql(), sqlObject.getValues().toArray(), requiredType);
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
		return queryForListBySql(sqlObject.getSql(), sqlObject.getValues().toArray(), elementType);
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
		return queryForListBySql(sqlObject.getSql(), sqlObject.getValues());
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
