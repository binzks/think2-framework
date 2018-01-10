package org.think2framework.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.think2framework.core.datasource.Datasource;
import org.think2framework.core.datasource.Redis;
import org.think2framework.core.datasource.mysql.Mysql;
import org.think2framework.core.datasource.oracle.Oracle;
import org.think2framework.core.datasource.sqlite.Sqlite;
import org.think2framework.core.datasource.sqlserver.Sqlserver;
import org.think2framework.core.exception.MessageException;
import org.think2framework.core.exception.SystemMessage;

/**
 * 数据源工厂
 */
public class DatasourceFactory {

	private static final Logger logger = LogManager.getLogger(DatasourceFactory.class);

	private static Map<String, Datasource> datasourceMap = new HashMap<>(); // 数据源

	private static Map<String, Redis> redisMap = new HashMap<>(); // redis

	/**
	 * 追加一个数据源，如果已经存在则警告，不追加
	 *
	 * @param datasource
	 *            数据源
	 */
	public static synchronized void append(org.think2framework.core.bean.Datasource datasource) {
		String name = datasource.getName();
		if (null != datasourceMap.get(name) || null != redisMap.get(name)) {
			logger.warn("数据源[{}]已经存在，忽略追加！", name);
		} else {
			switch (datasource.getType()) {
			case MYSQL:
				datasourceMap.put(name,
						new Mysql(datasource.getMinIdle(), datasource.getMaxIdle(), datasource.getInitialSize(),
								datasource.getTimeout(), datasource.getUsername(), datasource.getPassword(),
								datasource.getHost(), datasource.getPort(), datasource.getDatabase()));
			case REDIS:
				redisMap.put(name,
						new Redis(datasource.getMinIdle(), datasource.getMaxIdle(), datasource.getTimeout(),
								datasource.getDatabase(), datasource.getHost(), datasource.getPort(),
								datasource.getPassword()));
			case ORACLE:
				datasourceMap.put(name,
						new Oracle(datasource.getMinIdle(), datasource.getMaxIdle(), datasource.getInitialSize(),
								datasource.getTimeout(), datasource.getUsername(), datasource.getPassword(),
								datasource.getHost(), datasource.getDatabase()));
			case SQLITE:
				datasourceMap.put(name,
						new Sqlite(datasource.getMinIdle(), datasource.getMaxIdle(), datasource.getInitialSize(),
								datasource.getTimeout(), datasource.getUsername(), datasource.getPassword(),
								datasource.getHost(), datasource.getPort(), datasource.getDatabase()));
			case SQLERVER:
				datasourceMap.put(name,
						new Sqlserver(datasource.getMinIdle(), datasource.getMaxIdle(), datasource.getInitialSize(),
								datasource.getTimeout(), datasource.getUsername(), datasource.getPassword(),
								datasource.getHost(), datasource.getPort(), datasource.getDatabase()));
			}
			logger.debug("追加数据源[{}]", name);
		}
	}

	/**
	 * 批量追加数据源
	 *
	 * @param dsList
	 *            数据源
	 */
	public static synchronized void append(List<org.think2framework.core.bean.Datasource> dsList) {
		for (org.think2framework.core.bean.Datasource datasource : dsList) {
			append(datasource);
		}
	}

	/**
	 * 根据名称获取一个数据源
	 *
	 * @param name
	 *            数据源名称
	 * @return 数据源
	 */
	public static Datasource get(String name) {
		Datasource datasource = datasourceMap.get(name);
		if (null == datasource) {
			throw new MessageException(SystemMessage.NON_EXIST.getCode(), "数据源" + name + "");
		}
		return datasource;
	}

	/**
	 * 根据名称获取一个redis
	 *
	 * @param name
	 *            redis名称
	 * @return redis
	 */
	public static Redis getRedis(String name) {
		Redis redis = redisMap.get(name);
		if (null == redis) {
			throw new MessageException(SystemMessage.NON_EXIST.getCode(), "Redis" + name + "");
		}
		return redis;
	}

}
