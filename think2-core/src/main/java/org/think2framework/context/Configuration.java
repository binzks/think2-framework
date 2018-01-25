package org.think2framework.context;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.think2framework.context.bean.*;
import org.think2framework.core.datasource.OrmFactory;
import org.think2framework.core.exception.MessageFactory;
import org.think2framework.core.utils.FileUtils;
import org.think2framework.core.utils.JsonUtils;

import com.fasterxml.jackson.core.type.TypeReference;

public class Configuration implements ApplicationContextAware {

	private static boolean datasourceInitialized = false; // 数据源是否初始化
	private static boolean messageInitialized = false; // 系统消息是否初始化
	private static boolean modelContextInitialized = false; // 系统模型语境是否初始化
	private static boolean modelInitialized = false; // 系统模型是否初始化
	private static boolean paramInitialized = false; // 系统参数是否初始化
	private static boolean apiInitialized = false; // 系统接口是否初始化

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
		if (datasourceInitialized) {
			return;
		}
		File[] files = FileUtils.getFiles(this.getClass().getResource("/").getPath() + datasource);
		if (null == files) {
			return;
		}
		// 重新加载数据库
		for (File file : files) {
			List<Datasource> dss = JsonUtils.readFile(file, new TypeReference<List<Datasource>>() {
			});
			for (Datasource ds : dss) {
				OrmFactory.append(ds.getName(), ds.getType(), ds.getMinIdle(), ds.getMaxIdle(), ds.getInitialSize(),
						ds.getTimeout(), ds.getUsername(), ds.getPassword(), ds.getHost(), ds.getPort(),
						ds.getDatabase());
			}
		}
		datasourceInitialized = true;
	}

	/**
	 * 初始化消息信息
	 *
	 * @param message
	 *            消息路径和名称
	 */
	public void setMessage(String message) {
		// 初始化的时候会被多次调用，如果已经有配置说明初始化过，不需要再初始化
		if (messageInitialized) {
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
		messageInitialized = true;
	}

	/**
	 * 初始化模型语境，模型与数据库的关系
	 * 
	 * @param modelContext
	 *            模型语境
	 */
	public void setModelContext(String modelContext) {
		// 初始化的时候会被多次调用，如果已经有配置说明初始化过，不需要再初始化
		if (modelContextInitialized) {
			return;
		}
		File[] files = FileUtils.getFiles(this.getClass().getResource("/").getPath() + modelContext);
		if (null == files) {
			return;
		}
		for (File file : files) {
			List<ModelContext> modelContexts = JsonUtils.readFile(file, new TypeReference<List<ModelContext>>() {
			});
			ModelFactory.appendContext(modelContexts);
		}
		modelContextInitialized = true;
	}

	/**
	 * 初始化模型信息
	 *
	 * @param model
	 *            模型路径和名称
	 */
	public void setModel(String model) {
		// 初始化的时候会被多次调用，如果已经有配置说明初始化过，不需要再初始化
		if (modelInitialized) {
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
		modelInitialized = true;
	}

	/**
	 * 初始化系统参数配置
	 *
	 * @param param
	 *            系统参数路径和名称
	 */
	public void setParam(String param) {
		// 初始化的时候会被多次调用，如果已经有配置说明初始化过，不需要再初始化
		if (paramInitialized) {
			return;
		}
		File[] files = FileUtils.getFiles(this.getClass().getResource("/").getPath() + param);
		if (null == files) {
			return;
		}
		for (File file : files) {
			List<Param> params = JsonUtils.readFile(file, new TypeReference<List<Param>>() {
			});
			ParamFactory.append(params);
		}
		paramInitialized = true;
	}

	/**
	 * 初始化系统接口
	 * 
	 * @param api
	 *            系统接口
	 */
	public void setApi(String api) {
		// 初始化的时候会被多次调用，如果已经有配置说明初始化过，不需要再初始化
		if (apiInitialized) {
			return;
		}
		File[] files = FileUtils.getFiles(this.getClass().getResource("/").getPath() + api);
		if (null == files) {
			return;
		}
		for (File file : files) {
			List<Api> apis = JsonUtils.readFile(file, new TypeReference<List<Api>>() {
			});
			ApiFactory.append(apis);
		}
		apiInitialized = true;
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
