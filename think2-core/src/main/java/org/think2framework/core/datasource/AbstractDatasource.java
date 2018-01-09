package org.think2framework.core.datasource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 抽象数据源
 */
public abstract class AbstractDatasource implements Datasource {

	protected JdbcTemplate jdbcTemplate; // spring JdbcTemplate

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

}
