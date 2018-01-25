package org.think2framework.core.api;

import java.util.Map;

/**
 * 接口执行器接口
 */
public interface Actuator {

	/**
	 * 执行接口获取字符串
	 * 
	 * @param params
	 *            接口参数
	 * @return 返回值
	 */
	String exec(Map<String, Object> params);

}
