package org.think2framework.core.api;

import java.util.Map;

import org.think2framework.context.ModelFactory;
import org.think2framework.core.datasource.Query;
import org.think2framework.core.utils.JsonUtils;

/**
 * 查询模型接口
 */
public class QueryModelActuator implements Actuator {

	private String model; // 获取数据的模型名称

	public QueryModelActuator(String model) {
		this.model = model;
	}

	@Override
	public String exec(Map<String, Object> params) {
		Query query = ModelFactory.createQuery(model);
		if (null != params && params.size() > 0) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.eq(entry.getKey(), entry.getValue());
			}
		}
		return JsonUtils.toString(query.queryForList());
	}

}
