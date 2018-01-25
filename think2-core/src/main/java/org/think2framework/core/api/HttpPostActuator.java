package org.think2framework.core.api;

import java.util.Map;

import org.think2framework.core.utils.HttpClientUtils;

/**
 * http post接口
 */
public class HttpPostActuator implements Actuator {

	private String url;// 接口地址

	private String encoding; // 编码

	private Map<String, String> header; // http头

	public HttpPostActuator(String url, String encoding, Map<String, String> header) {
		this.url = url;
		this.encoding = encoding;
		this.header = header;
	}

	@Override
	public String exec(Map<String, Object> params) {
		return HttpClientUtils.post(url, params, encoding, header);
	}
}
