package org.think2framework.core.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.think2framework.core.utils.JsonUtils;
import org.think2framework.core.utils.NumberUtils;
import org.think2framework.core.utils.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;

/**
 * redis数据库接口实现
 */
public class Redis {

	private static final Logger logger = LogManager.getLogger(Redis.class); // log4j日志对象

	private String host; // 地址

	private JedisPool jedisPool; // jedis连接池

	public Redis(Integer minIdle, Integer maxIdle, Integer timeout, String db, String host, Integer port,
			String password) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(timeout);
		jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, NumberUtils.toInt(db));
		this.host = host;
	}

	/**
	 * 设置无有效期redis缓存，如果关键字或者值不存在则不存储
	 *
	 * @param key
	 *            关键字
	 * @param value
	 *            值
	 */
	public void set(String key, Object value) {
		set(key, 0, value);
	}

	/**
	 * 设置redis缓存，如果seconds大于0则有有效期，小于等于0则无有效期，如果关键字或者值不存在则不存储
	 *
	 * @param key
	 *            关键字
	 * @param seconds
	 *            有效期，单位秒
	 * @param value
	 *            值
	 */
	public void set(String key, int seconds, Object value) {
		if (StringUtils.isBlank(key) || null == value) {
			return;
		}
		Jedis jedis = jedisPool.getResource();
		try {
			String v = JsonUtils.toString(value);
			if (seconds > 0) {
				jedis.setex(key, seconds, v);
			} else {
				jedis.set(key, v);
			}
			logger.debug("Redis {} set key {}", host, key);
		} catch (Exception e) {
			logger.error("Redis {} set key {} error {}", host, key, e.getMessage());
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
	}

	/**
	 * 获取redis缓存
	 *
	 * @param key
	 *            关键字
	 * @return 值
	 */
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			String value = jedis.get(key);
			logger.debug("Redis {} get key {}", host, key);
			return value;
		} catch (Exception e) {
			logger.error("Redis {} get key {} error {}", host, key, e.getMessage());
			return null;
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
	}

	/**
	 * 获取redis缓存，并解析成对象
	 *
	 * @param key
	 *            关键字
	 * @param valueType
	 *            类
	 * @return 对象
	 */
	public <T> T get(String key, Class<T> valueType) {
		String data = get(key);
		if (null == data) {
			return null;
		} else {
			return JsonUtils.readString(data, valueType);
		}
	}

	/**
	 * 获取redis缓存的值，并转化为数组
	 *
	 * @param key
	 *            关键字
	 * @param elementType
	 *            类
	 * @return 数组
	 */
	public <T> List<T> getList(String key, Class<T> elementType) {
		String data = get(key);
		if (null == data) {
			return null;
		} else {
			return JsonUtils.readStringToList(data, elementType);
		}
	}

	/**
	 * 获取redis缓存，并转换为map
	 *
	 * @param key
	 *            关键字
	 * @return map
	 */
	public Map<String, Object> getMap(String key) {
		String data = get(key);
		if (null == data) {
			return null;
		} else {
			return JsonUtils.readStringToMap(data);
		}
	}

	/**
	 * 获取redis缓存，并转化为map数组
	 *
	 * @param key
	 *            关键字
	 * @return map数组
	 */
	public List<Map<String, Object>> getMapList(String key) {
		String data = get(key);
		if (null == data) {
			return null;
		} else {
			return JsonUtils.readStringToMapList(data);
		}
	}

	/**
	 * 删除redis缓存
	 *
	 * @param key
	 *            关键字，可以多个
	 */
	public void delete(String... key) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.del(key);
			logger.debug("Redis {} delete key {}", host, key);
		} catch (Exception e) {
			logger.error("Redis {} delete key {} error {}", host, key, e.getMessage());
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
	}

	/**
	 * 批量删除redis缓存，所有key前缀是prefix都删除
	 *
	 * @param prefix
	 *            关键字前缀
	 */
	public void deleteKeys(String prefix) {
		Jedis jedis = jedisPool.getResource();
		try {
			Set<String> keys = jedis.keys(prefix + "*");
			for (String key : keys) {
				jedis.del(key);
				logger.debug("Redis {} delete key {}", host, key);
			}
		} catch (Exception e) {
			logger.error("Redis {} delete keys {}* error {}" + host, prefix, e.getMessage());
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
	}
}
