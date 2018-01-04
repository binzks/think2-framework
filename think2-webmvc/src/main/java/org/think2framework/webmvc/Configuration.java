package org.think2framework.webmvc;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.think2framework.core.ConstantFactory;
import org.think2framework.core.ModelFactory;
import org.think2framework.core.bean.Constant;
import org.think2framework.core.bean.Model;
import org.think2framework.core.exception.MessageFactory;
import org.think2framework.core.orm.DatabaseFactory;
import org.think2framework.core.utils.FileUtils;
import org.think2framework.core.utils.JsonUtils;
import org.think2framework.core.utils.NumberUtils;
import org.think2framework.core.utils.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;

public class Configuration implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.setProperty("jsse.enableSNIExtension", "false");
	}

	/**
	 * 初始化数据源配置
	 *
	 * @param datasource
	 *            数据源文件路径和名称
	 */
	public void setDatasource(String datasource) {
		// 初始化的时候会被多次调用，如果已经有配置说明初始化过，不需要再初始化
		if (DatabaseFactory.size() > 0) {
			return;
		}
		File[] files = FileUtils.getFiles(this.getClass().getResource("/").getPath() + datasource);
		if (null == files) {
			return;
		}
		// 重新加载数据库
		for (File file : files) {
			List<Map<String, Object>> dss = JsonUtils.readFileToMapList(file);
			for (Map<String, Object> ds : dss) {
				DatabaseFactory.append(StringUtils.toString(ds.get("type")), StringUtils.toString(ds.get("name")),
						NumberUtils.toInt(ds.get("minIdle"), 1), NumberUtils.toInt(ds.get("maxIdle"), 2),
						NumberUtils.toInt(ds.get("initialSize"), 2), NumberUtils.toInt(ds.get("timeout"), 300),
						StringUtils.toString(ds.get("db")), StringUtils.toString(ds.get("host")),
						NumberUtils.toInt(ds.get("port")), StringUtils.toString(ds.get("username")),
						StringUtils.toString(ds.get("password")));
			}
		}
	}

	/**
	 * 初始化常量配置
	 *
	 * @param constant
	 *            常量路径和名称
	 */
	public void setConstant(String constant) {
		// 初始化的时候会被多次调用，如果已经有配置说明初始化过，不需要再初始化
		if (ConstantFactory.size() > 0) {
			return;
		}
		File[] files = FileUtils.getFiles(this.getClass().getResource("/").getPath() + constant);
		if (null == files) {
			return;
		}
		for (File file : files) {
			List<Constant> constants = JsonUtils.readFile(file, new TypeReference<List<Constant>>() {
			});
			for (Constant c : constants) {
				ConstantFactory.append(c);
			}
		}
	}

	/**
	 * 初始化消息信息
	 *
	 * @param message
	 *            消息路径和名称
	 */
	public void setMessage(String message) {
		// 初始化的时候会被多次调用，如果已经有配置说明初始化过，不需要再初始化
		if (MessageFactory.size() > 0) {
			return;
		}
		File[] files = FileUtils.getFiles(this.getClass().getResource("/").getPath() + message);
		if (null == files) {
			return;
		}
		for (File file : files) {
			Map<String, Object> map = JsonUtils.readFileToMap(file);
			MessageFactory.append(map);
		}
	}

	/**
	 * 初始化模型信息
	 *
	 * @param model
	 *            模型路径和名称
	 */
	public void setModel(String model) {
		// 初始化的时候会被多次调用，如果已经有配置说明初始化过，不需要再初始化
		if (ModelFactory.size() > 0) {
			return;
		}
		File[] files = FileUtils.getFiles(this.getClass().getResource("/").getPath() + model);
		if (null == files) {
			return;
		}
		for (File file : files) {
			List<Model> models = JsonUtils.readFile(file, new TypeReference<List<Model>>() {
			});
			ModelFactory.append(models);
		}
	}

	// /**
	// * 扫描包，初始化模型
	// *
	// * @param packages
	// * 数组map参数，query表示查询数据源，writer表示写入数据源，packages表示扫描包名，多个,隔开
	// */
	// public void setPackages(List<Map<String, String>> packages) {
	// if (packageInit) {
	// return;
	// }
	// for (Map<String, String> map : packages) {
	// // ModelFactory.scanPackages(StringUtils.toString(map.get("query")),
	// // StringUtils.toString(map.get("writer")),
	// // StringUtils.split(StringUtils.toString(map.get("packages")), ","));
	// }
	// packageInit = true;
	// }

}
