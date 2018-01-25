package org.think2framework.core.api;

import java.util.Map;

import org.think2framework.core.utils.HttpClientUtils;

/**
 * webservice接口
 */
public class WebserviceActuator implements Actuator {

	private String url; // 请求地址

	private String encoding; // 编码

	private String body; // 请求实体xml

	private Map<String, String> header; // 请求头

	public WebserviceActuator(String url, String encoding, String body, Map<String, String> header) {
		this.url = url;
		this.encoding = encoding;
		this.body = body;
		this.header = header;
	}

	@Override
	public String exec(Map<String, Object> params) {
		String data = "";
		if (null != params && params.size() > 0) {

		}
		return HttpClientUtils.post(url, String.format(body, data), encoding, header);
	}
}
