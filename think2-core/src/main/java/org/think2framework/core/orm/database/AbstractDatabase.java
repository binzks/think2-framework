package org.think2framework.core.orm.database;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.think2framework.core.bean.Column;
import org.think2framework.core.orm.ClassUtils;
import org.think2framework.core.orm.bean.SqlObject;
import org.think2framework.core.exception.DatabaseException;
import org.think2framework.core.utils.StringUtils;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

public abstract class AbstractDatabase implements Database {

	private static final Logger logger = LogManager.getLogger(AbstractDatabase.class); // log4j日志对象

    private JdbcTemplate jdbcTemplate; //spring JdbcTemplate

    public AbstractDatabase(Integer minIdle, Integer maxIdle, Integer initialSize, Integer timeout, 
                  String username, String password, String driver, String url) {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setMinIdle(minIdle);
        basicDataSource.setMaxIdle(maxIdle);
        basicDataSource.setInitialSize(initialSize);
        basicDataSource.setRemoveAbandonedOnBorrow(true);
        basicDataSource.setRemoveAbandonedTimeout(timeout);
        basicDataSource.setLogAbandoned(true);
        basicDataSource.setValidationQuery("SELECT 1");
        basicDataSource.setDriverClassName(driver);
        basicDataSource.setUrl(url);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        jdbcTemplate = new JdbcTemplate(basicDataSource);
    }

	/**
	 * 获取数据库字段生成sql语句的时候前缀
	 * 
	 * @return 获取数据库字段生成sql语句的时候前缀
	 */
	public abstract String getKeyBegin();

	/**
	 * 获取数据库字段生成sql语句的时候后缀
	 * 
	 * @return 获取数据库字段生成sql语句的时候后缀
	 */
	public abstract String getKeyEnd();

	/**
	 * 生成数据库表创建sql
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
	 * @return 创建sql
	 */
	public abstract String createSql(String table, String pk, boolean autoIncrement, List<String> uniques,
			List<String> indexes, String comment, Map<String, Column> columns);

	@Override
	public boolean createTable(String table, String pk, boolean autoIncrement, List<String> uniques,
			List<String> indexes, String comment, Map<String, Column> columns) {
		Boolean result = false;
		try {
			jdbcTemplate.queryForList("SELECT 1 FROM " + getKeyBegin() + table + getKeyEnd() + " WHERE 1=2");
		} catch (Exception e) {
			if (null != e.getCause()) {
				String msg = e.getCause().getMessage();
				if (msg.contains("no such table") || (msg.contains("Table") && msg.contains("doesn't exist"))) {
					String createSql = createSql(table, pk, autoIncrement, uniques, indexes, comment, columns);
					logger.debug("Create table sql:" + createSql);
					jdbcTemplate.execute(createSql);
					result = true;
				} else {
					throw new DatabaseException(e);
				}
			} else {
				throw new DatabaseException(e);
			}
		}
		return result;
	}

	@Override
	public int delete(String table, String pk, List<Object> values, String... keys) {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ").append(getKeyBegin()).append(table).append(getKeyEnd()).append(" WHERE 1=1");
		if (null == keys || keys.length == 0) {
			sql.append(" AND ").append(getKeyBegin()).append(pk).append(getKeyEnd()).append(" =?");
		} else {
			for (String key : keys) {
				sql.append(" AND ").append(getKeyBegin()).append(key).append(getKeyEnd()).append(" =?");
			}
		}
		logger.debug("delete sql: {} values: {}", sql.toString(), values);
		return jdbcTemplate.update(sql.toString(), values.toArray());
	}

	@Override
	public Object insert(String sql, Boolean autoIncrement, Object... args) {
		logger.debug("Insert sql: {} values: {} autoIncrement: {}", sql, args, autoIncrement);
		try {
			if (autoIncrement) {
				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(con -> {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					for (int i = 0; i < args.length; i++) {
						ps.setObject(i + 1, ClassUtils.getDatabaseValue(args[i]));
					}
					return ps;
				}, keyHolder);
				return keyHolder.getKey().intValue();
			} else {
				jdbcTemplate.update(sql, args);
				return "";
			}
		} catch (DuplicateKeyException e) {// 如果是唯一性约束错误，转化为中文
			String message = e.getMessage();
			throw new DatabaseException(
					"数据" + StringUtils.substring(message, StringUtils.indexOf(message, "Duplicate entry '") + 17,
							StringUtils.indexOf(message, "' for key")) + "已存在！");
		}
	}

	@Override
	public int update(String sql, Object... args) {
		logger.debug("Update sql: {} values: {}", sql, args);
		return jdbcTemplate.update(sql, args);
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) {
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
	public Map<String, Object> queryForMap(String sql, Object... args) {
		logger.debug("queryForMap sql: {} values: {}", sql, args);
		try {
			return jdbcTemplate.queryForMap(sql, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) {
		logger.debug("queryForList Object sql: {} values: {} elementType: {}", sql, args, elementType);
		try {
			List<T> list = jdbcTemplate.query(sql, args, (rs, rowNum) -> {
				return ClassUtils.createInstance(elementType, rs);
			});
			return list;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		logger.debug("queryForList Map sql: {} values: {}", sql, args);
		try {
			return jdbcTemplate.queryForList(sql, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		logger.debug("batchUpdate sql: {} values: {}", sql, batchArgs);
		return jdbcTemplate.batchUpdate(sql, batchArgs);
	}

	@Override
	public Object insert(Object instance, String table, String pk, boolean autoIncrement, Map<String, Column> columns) {
		// 如果不是自增长的则添加一个uuid
		Object id = null;
		if (!autoIncrement) {
			id = UUID.randomUUID().toString();
		}
		SqlObject sqlObject = ClassUtils.createInsert(instance, table, pk, null != id ? id.toString() : null, columns,
				getKeyBegin(), getKeyEnd());
		id = insert(sqlObject.getSql(), autoIncrement, sqlObject.getValues());
		return id;
	}

	@Override
	public int update(Object instance, String table, String pk, Map<String, Column> columns, String... keys) {
		SqlObject sqlObject = ClassUtils.createUpdate(instance, table, pk, getKeyBegin(), getKeyEnd(), columns, keys);
		return update(sqlObject.getSql(), sqlObject.getValues());
	}

	@Override
	public Object save(Object instance, String table, String pk, boolean autoIncrement, Map<String, Column> columns,
			String... keys) {
		if (update(instance, table, pk, columns, keys) == 1) {
			return -1;
		} else {
			return insert(instance, table, pk, autoIncrement, columns);
		}
	}

	@Override
	public <T> int[] batchInsert(List<T> list, String table, String pk, boolean autoIncrement,
			Map<String, Column> columns) {
		String sql = null;
		List<Object[]> batchArgs = new ArrayList<>();
		for (Object object : list) {
			// 如果不是自增长的则添加一个uuid
			Object id = null;
			if (!autoIncrement) {
				id = UUID.randomUUID().toString();
			}
			SqlObject sqlObject = ClassUtils.createInsert(object, table, pk, null != id ? id.toString() : null, columns,
					getKeyBegin(), getKeyEnd());
			if (StringUtils.isBlank(sql)) {
				sql = sqlObject.getSql();
			}
			batchArgs.addAll(Collections.singleton(sqlObject.getValues().toArray()));
		}
		return batchUpdate(sql, batchArgs);
	}

	@Override
	public <T> int[] batchUpdate(List<T> list, String table, String pk, Map<String, Column> columns, String... keys) {
		String sql = null;
		List<Object[]> batchArgs = new ArrayList<>();
		for (Object object : list) {
			SqlObject sqlObject = ClassUtils.createUpdate(object, table, pk, getKeyBegin(), getKeyEnd(), columns, keys);
			if (StringUtils.isBlank(sql)) {
				sql = sqlObject.getSql();
			}
			batchArgs.addAll(Collections.singleton(sqlObject.getValues().toArray()));
		}
		return batchUpdate(sql, batchArgs);
	}
}
