package org.think2framework.context.bean;

/**
 * 系统模型语境，定义模型对应的数据库和缓存redis
 */
public class ModelContext {

	private String model; // 模型名称

	private String query; // 查询数据源名称

	private String writer; // 写入数据源名称

	private String redis; // redis缓存数据源名称

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getRedis() {
		return redis;
	}

	public void setRedis(String redis) {
		this.redis = redis;
	}
}
