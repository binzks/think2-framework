//package org.think2framework.core;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.think2framework.core.datasource.DatabaseType;
//import org.think2framework.core.datasource.Datasource;
//import org.think2framework.core.datasource.Redis;
//import org.think2framework.core.datasource.mysql.Mysql;
//import org.think2framework.core.datasource.oracle.Oracle;
//import org.think2framework.core.datasource.sqlite.Sqlite;
//import org.think2framework.core.datasource.sqlserver.Sqlserver;
//import org.think2framework.core.exception.MessageException;
//import org.think2framework.core.exception.SystemMessage;
//
///**
// * 数据源工厂
// */
//public class DatasourceFactory {
//
//	private static final Logger logger = LogManager.getLogger(DatasourceFactory.class);
//
//	private static Map<String, Datasource> datasourceMap = new HashMap<>(); // 数据源
//
//	private static Map<String, Redis> redisMap = new HashMap<>(); // redis
//
//	/**
//	 * 追加一个数据源，如果已经存在则警告，不追加
//	 *
//	 * @param name
//	 *            数据源名称
//	 * @param databaseType
//	 *            数据库类型
//	 * @param minIdle
//	 *            数据源最小空闲连接
//	 * @param maxIdle
//	 *            数据源最大空闲连接
//	 * @param initialSize
//	 *            数据源初始化连接数
//	 * @param timeout
//	 *            数据源超时时间(以秒数为单位)
//	 * @param username
//	 *            数据库用户名
//	 * @param password
//	 *            数据库密码
//	 * @param host
//	 *            数据库地址
//	 * @param port
//	 *            数据库端口
//	 * @param database
//	 *            数据库名称
//	 */
//	public static synchronized void append(String name, DatabaseType databaseType, Integer minIdle, Integer maxIdle,
//			Integer initialSize, Integer timeout, String username, String password, String host, Integer port,
//			String database) {
//		if (null != datasourceMap.get(name) || null != redisMap.get(name)) {
//			logger.warn("数据源[{}]已经存在，忽略追加！", name);
//		} else {
//			switch (databaseType) {
//			case MYSQL:
//				datasourceMap.put(name,
//						new Mysql(minIdle, maxIdle, initialSize, timeout, username, password, host, port, database));
//			case REDIS:
//				redisMap.put(name, new Redis(minIdle, maxIdle, timeout, database, host, port, password));
//			case ORACLE:
//				datasourceMap.put(name,
//						new Oracle(minIdle, maxIdle, initialSize, timeout, username, password, host, database));
//			case SQLITE:
//				datasourceMap.put(name,
//						new Sqlite(minIdle, maxIdle, initialSize, timeout, username, password, host, port, database));
//			case SQLSERVER:
//				datasourceMap.put(name, new Sqlserver(minIdle, maxIdle, initialSize, timeout, username, password, host,
//						port, database));
//			}
//			logger.debug("追加数据源[{}]", name);
//		}
//	}
//
//	/**
//	 * 根据名称获取一个数据源
//	 *
//	 * @param name
//	 *            数据源名称
//	 * @return 数据源
//	 */
//	public static Datasource get(String name) {
//		Datasource datasource = datasourceMap.get(name);
//		if (null == datasource) {
//			throw new MessageException(SystemMessage.NON_EXIST.getCode(), "数据源" + name + "");
//		}
//		return datasource;
//	}
//
//	/**
//	 * 根据名称获取一个redis
//	 *
//	 * @param name
//	 *            redis名称
//	 * @return redis
//	 */
//	public static Redis getRedis(String name) {
//		Redis redis = redisMap.get(name);
//		if (null == redis) {
//			throw new MessageException(SystemMessage.NON_EXIST.getCode(), "Redis" + name + "");
//		}
//		return redis;
//	}
//
//}
