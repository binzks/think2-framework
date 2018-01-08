package org.think2framework.core.orm.database;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.think2framework.core.orm.ClassUtils;

public class Database {

	private static final Logger logger = LogManager.getLogger(Database.class); // log4j日志对象

	private JdbcTemplate jdbcTemplate; // spring JdbcTemplate

	public Database(Integer minIdle, Integer maxIdle, Integer initialSize, Integer timeout, String username,
                    String password, String driver, String url) {
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
		logger.debug("Insert sql: {} values: {} autoIncrement: {}", sql, args, autoIncrement);
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
	}

	/**
	 * 执行一个自定义写入sql
	 *
	 * @param sql
	 *            sql语句
	 */
	public int update(String sql, Object... args) {
		logger.debug("Update sql: {} values: {}", sql, args);
		return jdbcTemplate.update(sql, args);
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
		logger.debug("queryForMap sql: {} values: {}", sql, args);
		try {
			return jdbcTemplate.queryForMap(sql, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
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
		logger.debug("queryForList Map sql: {} values: {}", sql, args);
		try {
			return jdbcTemplate.queryForList(sql, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
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
		logger.debug("batchUpdate sql: {} values: {}", sql, batchArgs);
		return jdbcTemplate.batchUpdate(sql, batchArgs);
	}

}
