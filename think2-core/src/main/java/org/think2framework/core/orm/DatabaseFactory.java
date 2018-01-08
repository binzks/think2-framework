package org.think2framework.core.orm;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.think2framework.core.exception.NonExistException;
import org.think2framework.core.orm.datasource.Datasource;
import org.think2framework.core.orm.datasource.Redis;
import org.think2framework.core.orm.datasource.Type;
import org.think2framework.core.orm.datasource.mysql.Mysql;
import org.think2framework.core.orm.datasource.oracle.Oracle;
import org.think2framework.core.orm.datasource.sqlite.Sqlite;
import org.think2framework.core.orm.datasource.sqlserver.Sqlserver;

/**
 * 数据持久化工厂
 */
public class DatabaseFactory {

	private static final Logger logger = LogManager.getLogger(DatabaseFactory.class);

	private static Map<String, Datasource> datasourceMap = new HashMap<>();// 存储数据库的map

	private static Map<String, Redis> redisMap = new HashMap<>(); // redis数据库map

	/**
	 * 追加一个数据源
	 *
	 * @param type
	 *            数据库类型
	 * @param name
	 *            数据源名称
	 * @param minIdle
	 *            数据源最小空闲连接
	 * @param maxIdle
	 *            数据源最大空闲连接
	 * @param initialSize
	 *            数据源初始化连接数
	 * @param timeout
	 *            数据源超时时间(以秒数为单位)
	 * @param db
	 *            数据库名称
	 * @param host
	 *            数据库地址
	 * @param port
	 *            数据库端口
	 * @param username
	 *            数据库用户名
	 * @param password
	 *            数据库密码
	 */
	public static synchronized void append(String type, String name, Integer minIdle, Integer maxIdle,
			Integer initialSize, Integer timeout, String db, String host, Integer port, String username,
			String password) {
		if (null != datasourceMap.get(name)) {
			logger.warn("数据源[{}]已经存在，忽略追加！", name);
		} else {
			try {
				Type dbType = Type.valueOf(type.toUpperCase());
				if (Type.MYSQL == dbType) {
					datasourceMap.put(name,
							new Mysql(minIdle, maxIdle, initialSize, timeout, username, password, host, port, db));
					logger.debug("追加mysql[{}][{}][{}]！", host, db, username);
				} else if (Type.REDIS == dbType) {
					redisMap.put(name, new Redis(minIdle, maxIdle, timeout, db, host, port, password));
					logger.debug("追加redis[{}][{}]！", host, db);
				} else if (Type.SQLSERVER == dbType) {
					datasourceMap.put(name,
							new Sqlserver(minIdle, maxIdle, initialSize, timeout, username, password, host, port, db));
					logger.debug("追加sqlserver[{}][{}][{}]！", host, db, username);
				} else if (Type.ORACLE == dbType) {
					datasourceMap.put(name,
							new Oracle(minIdle, maxIdle, initialSize, timeout, username, password, host, db));
					logger.debug("追加oracle[{}][{}][{}]！", host, db, username);
				} else if (Type.SQLITE == dbType) {
					datasourceMap.put(name,
							new Sqlite(minIdle, maxIdle, initialSize, timeout, username, password, host, port, db));
					logger.debug("追加sqlite[{}][{}][{}]！", host, db, username);
				}
			} catch (IllegalArgumentException e) {
				logger.warn("不支持的数据库类型[{}]，忽略追加！", type);
			}
		}
	}

	/**
	 * 根据名称获取数据库
	 *
	 * @param name
	 *            数据源名称
	 * @return 数据库
	 */
	public static Datasource get(String name) {
		Datasource datasource = datasourceMap.get(name);
		if (null == datasource) {
			throw new NonExistException("数据源[" + name + "]");
		}
		return datasource;
	}

	/**
	 * 根据名称获取redis
	 *
	 * @param name
	 *            redis名称
	 * @return redis
	 */
	public static Redis getRedis(String name) {
		Redis redis = redisMap.get(name);
		if (null == redis) {
			throw new NonExistException("Redis[" + name + "]");
		}
		return redis;
	}

	/**
	 * 获取工厂已有的数据库的数量
	 * 
	 * @return 数据库数量
	 */
	public static int size() {
		return datasourceMap.size() + redisMap.size();
	}
}
