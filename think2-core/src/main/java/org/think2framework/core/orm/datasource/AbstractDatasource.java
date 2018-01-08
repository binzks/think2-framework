package org.think2framework.core.orm.datasource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.think2framework.core.orm.ClassUtils;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * 抽象数据源
 */
public abstract class AbstractDatasource implements Datasource {

    private static final Logger logger = LogManager.getLogger(AbstractDatasource.class); // log4j日志对象

    private JdbcTemplate jdbcTemplate; // spring JdbcTemplate

    public AbstractDatasource(Integer minIdle, Integer maxIdle, Integer initialSize, Integer timeout, String username,
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

	@Override
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
}
