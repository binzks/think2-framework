package org.think2framework.core.api;

import java.util.Map;

import org.think2framework.core.utils.HttpClientUtils;

/**
 * http get接口
 */
public class HttpGetActuator implements Actuator {

	private String url; // 接口地址

	private String encoding; // 编码

	private Map<String, String> header; // http头

	public HttpGetActuator(String url, String encoding, Map<String, String> header) {
		this.url = url;
		this.encoding = encoding;
		this.header = header;
	}

	@Override
	public String exec(Map<String, Object> params) {
		return HttpClientUtils.get(url, params, encoding, header);
	}
}
