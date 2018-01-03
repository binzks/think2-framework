package org.think2framework.core.orm;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.think2framework.core.exception.DatabaseException;
import org.think2framework.core.exception.MessageException;
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
	public static synchronized void clearDatabases() {
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
	public static synchronized void appendDatabase(String type, String name, Integer minIdle, Integer maxIdle,
			Integer initialSize, Integer timeout, String db, String host, Integer port, String username,
			String password) {
		Type dbType;
		try {
			dbType = Type.valueOf(type.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new MessageException("");
		}
		if (Type.MYSQL == dbType) {
			appendMysql(name, minIdle, maxIdle, initialSize, timeout, host, port, db, username, password);
		} else if (Type.REDIS == dbType) {
			appendRedis(name, minIdle, maxIdle, timeout, host, port, db, password);
		} else if (Type.SQLSERVER == dbType) {
			appendSqlserver(name, minIdle, maxIdle, initialSize, timeout, host, port, db, username, password);
		} else if (Type.ORACLE == dbType) {
			appendOracle(name, minIdle, maxIdle, initialSize, timeout, host, port, db, username, password);
		} else if (Type.SQLITE == dbType) {
			appendSqlite(name, minIdle, maxIdle, initialSize, timeout, host, port, db, username, password);
		}
	}

	/**
	 * 追加一个mysql数据源，如果已经存在则不再重新添加并警告
	 */
	public static synchronized void appendMysql(String name, Integer minIdle, Integer maxIdle, Integer initialSize,
			Integer timeout, String host, Integer port, String database, String username, String password) {
		if (null == databases.get(name)) {
			databases.put(name, new Mysql(minIdle, maxIdle, initialSize, timeout,
					host + (null == port ? "" : ":" + port), database, username, password));
			logger.debug("Append mysql {} {} {}", host, database, username);
		} else {
			logger.warn("Mysql {} is already exist", name);
		}
	}

	/**
	 * 追加一个redis数据源，如果已经存在则不再重新添加并警告
	 */
	public static synchronized void appendRedis(String name, Integer minIdle, Integer maxIdle, Integer timeout,
			String host, Integer port, String database, String password) {
		if (null == redisMap.get(name)) {
			redisMap.put(name, new Redis(minIdle, maxIdle, timeout, database, host, port, password));
			logger.debug("Append redis {} {}", host, database);
		} else {
			logger.warn("Redis {} is already exist", name);
		}
	}

	/**
	 * 追加一个sqlserver数据源，如果已经存在则不再重新添加并警告
	 */
	public static synchronized void appendSqlserver(String name, Integer minIdle, Integer maxIdle, Integer initialSize,
			Integer timeout, String host, Integer port, String database, String username, String password) {
		if (null == databases.get(name)) {
			databases.put(name, new Sqlserver(minIdle, maxIdle, initialSize, timeout,
					host + (null == port ? "" : ":" + port), database, username, password));
			logger.debug("Append sqlserver {} {} {}", host, database, username);
		} else {
			logger.warn("Sqlserver {} is already exist", name);
		}
	}

	/**
	 * 追加一个sqlite数据源，如果已经存在则不再重新添加并警告
	 */
	public static synchronized void appendSqlite(String name, Integer minIdle, Integer maxIdle, Integer initialSize,
			Integer timeout, String host, Integer port, String database, String username, String password) {
		if (null == databases.get(name)) {
			databases.put(name, new Sqlite(minIdle, maxIdle, initialSize, timeout,
					host + (null == port ? "" : ":" + port), database, username, password));
			logger.debug("Append sqlite {} {} {}", host, database, username);
		} else {
			logger.warn("Sqlite {} is already exist", name);
		}
	}

	/**
	 * 追加一个oracle数据源，如果已经存在则不再重新添加并警告
	 */
	public static synchronized void appendOracle(String name, Integer minIdle, Integer maxIdle, Integer initialSize,
			Integer timeout, String host, Integer port, String database, String username, String password) {
		if (null == databases.get(name)) {
			databases.put(name, new Oracle(minIdle, maxIdle, initialSize, timeout,
					host + (null == port ? "" : ":" + port), database, username, password));
			logger.debug("Append oracle {} {} {}", host, database, username);
		} else {
			logger.warn("Oracle {} is already exist", name);
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
			throw new DatabaseException(Database.class.getName() + " " + name + " is not exist");
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
			throw new DatabaseException(Redis.class.getName() + " " + name + " is not exist");
		}
		return redis;
	}
}
