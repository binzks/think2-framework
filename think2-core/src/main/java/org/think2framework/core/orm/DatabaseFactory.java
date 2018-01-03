package org.think2framework.core.orm;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.think2framework.core.exception.NonExistException;
import org.think2framework.core.orm.database.*;

/**
 * 数据持久化工厂
 */
public class DatabaseFactory {

	private static final Logger logger = LogManager.getLogger(DatabaseFactory.class);

	private static Map<String, Database> databases = new HashMap<>();// 存储数据库的map

	private static Map<String, Redis> redisMap = new HashMap<>(); // redis数据库map

	/**
	 * 清理数据库配置
	 */
	public static synchronized void clear() {
		databases.clear();
		logger.debug("All databases cleared successfully");
	}

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
		if (null != databases.get(name)) {
			logger.warn("数据库[{}]已经存在，忽略追加！", name);
		} else {
			try {
				Type dbType = Type.valueOf(type.toUpperCase());
				if (Type.MYSQL == dbType) {
					databases.put(name, new Mysql(minIdle, maxIdle, initialSize, timeout,
							host + (null == port ? "" : ":" + port), db, username, password));
					logger.debug("追加mysql[{}][{}][{}]！", host, db, username);
				} else if (Type.REDIS == dbType) {
					redisMap.put(name, new Redis(minIdle, maxIdle, timeout, db, host, port, password));
					logger.debug("追加redis[{}][{}]！", host, db);
				} else if (Type.SQLSERVER == dbType) {
					databases.put(name, new Sqlserver(minIdle, maxIdle, initialSize, timeout,
							host + (null == port ? "" : ":" + port), db, username, password));
					logger.debug("追加sqlserver[{}][{}][{}]！", host, db, username);
				} else if (Type.ORACLE == dbType) {
					databases.put(name, new Oracle(minIdle, maxIdle, initialSize, timeout,
							host + (null == port ? "" : ":" + port), db, username, password));
					logger.debug("追加oracle[{}][{}][{}]！", host, db, username);
				} else if (Type.SQLITE == dbType) {
					databases.put(name, new Sqlite(minIdle, maxIdle, initialSize, timeout,
							host + (null == port ? "" : ":" + port), db, username, password));
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
	public static Database getDatabase(String name) {
		Database database = databases.get(name);
		if (null == database) {
			logger.error("获取不存在的数据库[{}]", name);
			throw new NonExistException("数据库[" + name + "]");
		}
		return database;
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
			logger.error("获取不存在的redis[{}]", name);
			throw new NonExistException("Redis[" + name + "]");
		}
		return redis;
	}
}
