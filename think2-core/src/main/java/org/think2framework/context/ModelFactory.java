package org.think2framework.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.think2framework.context.bean.Model;
import org.think2framework.context.bean.ModelContext;
import org.think2framework.core.OrmFactory;
import org.think2framework.core.datasource.Query;
import org.think2framework.core.datasource.Writer;
import org.think2framework.core.exception.MessageException;
import org.think2framework.core.exception.SystemMessage;
import org.think2framework.core.utils.PackageUtils;
import org.think2framework.core.utils.StringUtils;

/**
 * 模型工厂
 */
public class ModelFactory {

	private static final Logger logger = LogManager.getLogger(ModelFactory.class);

	private static Map<String, ModelContext> modelContextMap = new HashMap<>(); // 模型对应的语境，数据库配置


	/**
	 * 追加一个模型语境，如果已经存在则警告，不追加
	 *
	 * @param modelContext
	 *            模型
	 */
	public static synchronized void appendContext(ModelContext modelContext) {
		String name = modelContext.getModel();
		if (null != modelContextMap.get(name)) {
			logger.warn("模型语境[{}]已经存在，忽略追加！", name);
		} else {
			modelContextMap.put(name, modelContext);
			logger.debug("追加模型语境[{}][{}]！", name);
		}
	}

	/**
	 * 批量追加模型
	 *
	 * @param models
	 *            模型
	 */
	public static synchronized void append(List<Model> models) {
		for (Model model : models) {
//			append(model);
		}
	}

	/**
	 * 根据类名追加一个模型，获取类后根据类的定义生成模型
	 *
	 * @param clazz
	 *            类名
	 */
	public static synchronized void append(String clazz) {
		try {
			Class<?> c = Class.forName(clazz);
			append(c);
		} catch (ClassNotFoundException e) {
			throw new MessageException(SystemMessage.NON_EXIST.getCode(), "类" + clazz);
		}
	}

	/**
	 * 根据类追加一个模型，获取类后根据类的定义生成模型
	 *
	 * @param clazz
	 *            类
	 */
	public static synchronized void append(Class<?> clazz) {

	}

	/**
	 * 扫描多个包,将所有定义了模型的类追加
	 *
	 * @param packageDirNames
	 *            包名
	 */
	public static synchronized void scanPackages(String... packageDirNames) {
		for (String name : packageDirNames) {
			List<Class> list = PackageUtils.scanPackage(name);
			for (Class<?> clazz : list) {
				append(clazz);
			}
		}
	}

	/**
	 * 根据名称获取一个模型语境，获取数据库配置
	 * 
	 * @param name
	 *            名称
	 * @return 模型语境
	 */
	private static ModelContext getModelContext(String name) {
		ModelContext modelContext = modelContextMap.get(name);
		if (null == modelContext) {
			throw new MessageException(SystemMessage.NON_EXIST.getCode(), "模型语境" + name);
		}
		return modelContext;
	}

	/**
	 * 根据模型名称创建一个查询生成器
	 *
	 * @param name
	 *            模型名称
	 * @return 查询生成器
	 */
	public static Query createQuery(String name) {
		ModelContext modelContext = getModelContext(name);
		return OrmFactory.createQuery(name, modelContext.getQuery(), modelContext.getRedis());
	}

	/**
	 * 根据模型名称创建一个写入生成器，使用写入数据源，如果写入数据源不存在则使用查询数据源
	 *
	 * @param name
	 *            模型名称
	 * @return 写入生成器
	 */
	public static Writer createWriter(String name) {
		ModelContext modelContext = getModelContext(name);
		String writer = modelContext.getWriter();
		if (StringUtils.isBlank(writer)) {
			writer = modelContext.getQuery();
		}
		return OrmFactory.createWriter(name, writer);
	}

}
