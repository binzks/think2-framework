package org.think2framework.context.bean;

import java.util.Map;

import org.think2framework.core.api.ApiType;

/**
 * api接口定义
 */
public class Api {

	private String id; // 接口id

	private Boolean access; // 外网是否可访问

	private ApiType type; // 接口类型

	private String url; // 接口地址，如果是模型类的表示模型名称

	private String encoding; // 编码，默认utf-8

	private String body; // 如果是webservice，表示请求的body字符串，其中%s表示需要添加参数

	private Map<String, String> header; // 请求需要加的头

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getAccess() {
		return access;
	}

	public void setAccess(Boolean access) {
		this.access = access;
	}

	public ApiType getType() {
		return type;
	}

	public void setType(ApiType type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
}
