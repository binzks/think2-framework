package org.think2framework.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.think2framework.context.bean.Api;
import org.think2framework.core.api.*;
import org.think2framework.core.exception.MessageException;
import org.think2framework.core.exception.SystemMessage;

/**
 * api接口工厂
 */
public class ApiFactory {

	private static final Logger logger = LogManager.getLogger(ApiFactory.class);

	private static Map<String, Actuator> actuatorMap = new HashMap<>(); // 接口执行器

	/**
	 * 批量追加api接口
	 * 
	 * @param apis
	 *            api接口定义
	 */
	public static synchronized void append(List<Api> apis) {
		if (null == apis) {
			return;
		}
		for (Api api : apis) {
			append(api);
		}
	}

	/**
	 * 追加一个api接口
	 * 
	 * @param api
	 *            api接口定义
	 */
	public static synchronized void append(Api api) {
		String id = api.getId();
		if (null != actuatorMap.get(id)) {
			logger.warn("API[{}]已经存在，忽略追加！", id);
		} else {
			switch (api.getType()) {
			case QUERY_MODEL:
				actuatorMap.put(id, new QueryModelActuator(api.getUrl()));
				break;
			case WRITER_MODEL:
				actuatorMap.put(id, new WriterModelActuator(api.getUrl()));
				break;
			case HTTP_POST:
				actuatorMap.put(id, new HttpPostActuator(api.getUrl(), api.getEncoding(), api.getHeader()));
				break;
			case HTTP_GET:
				actuatorMap.put(id, new HttpGetActuator(api.getUrl(), api.getEncoding(), api.getHeader()));
				break;
			case WEBSERVICE:
				actuatorMap.put(id,
						new WebserviceActuator(api.getUrl(), api.getEncoding(), api.getBody(), api.getHeader()));
				break;
			}
			logger.debug("追加接口[{}]！", id);
		}
	}

	/**
	 * 根据id获取一个接口
	 * 
	 * @param id
	 *            接口id
	 * @return 接口
	 */
	private static Actuator get(String id) {
		Actuator actuator = actuatorMap.get(id);
		if (null == actuator) {
			throw new MessageException(SystemMessage.NON_EXIST.getCode(), "API" + id);
		}
		return actuator;
	}

	/**
	 * 执行一个接口
	 * 
	 * @param id
	 *            接口id
	 * @return 接口执行结果
	 */
	public static String exec(String id) {
		return exec(id, null);
	}

	/**
	 * 执行一个接口，带参数
	 * 
	 * @param id
	 *            接口id
	 * @param map
	 *            参数
	 * @return 接口执行结果
	 */
	public static String exec(String id, Map<String, Object> map) {
		Actuator actuator = get(id);
		return actuator.exec(map);
	}
}
