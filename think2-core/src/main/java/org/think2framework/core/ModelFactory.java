package org.think2framework.core;//package org.think2framework.core;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.think2framework.core.bean.Model;
//import org.think2framework.core.exception.NonExistException;
//import org.think2framework.core.utils.PackageUtils;
//
///**
// * 模型工厂
// */
//public class ModelFactory {
//
//	private static final Logger logger = LogManager.getLogger(ModelFactory.class);
//
//	private static Map<String, Model> models = new HashMap<>(); // 模型对应的数据库配置
//
//	/**
//	 * 获取已经添加的模型数量
//	 *
//	 * @return 模型数量
//	 */
//	public static int size() {
//		return models.size();
//	}
//
//	/**
//	 * 追加一个模型，如果已经存在则警告，不追加
//	 *
//	 * @param model
//	 *            模型
//	 */
//	public static synchronized void append(Model model) {
//		String name = model.getName();
//		if (null != models.get(name)) {
//			logger.warn("模型[{}]已经存在，忽略追加！", name);
//		} else {
//			models.put(name, model);
//			logger.debug("追加模型[{}][{}]！", name);
//		}
//	}
//
//	/**
//	 * 批量追加模型
//	 *
//	 * @param models
//	 *            模型
//	 */
//	public static synchronized void append(List<Model> models) {
//		for (Model model : models) {
//			append(model);
//		}
//	}
//
//	/**
//	 * 根据类名追加一个模型，获取类后根据类的定义生成模型
//	 *
//	 * @param clazz
//	 *            类名
//	 */
//	public static synchronized void append(String clazz) {
//		try {
//			Class<?> c = Class.forName(clazz);
//			append(c);
//		} catch (ClassNotFoundException e) {
//			throw new NonExistException(e);
//		}
//	}
//
//	/**
//	 * 根据类追加一个模型，获取类后根据类的定义生成模型
//	 *
//	 * @param clazz
//	 *            类
//	 */
//	public static synchronized void append(Class<?> clazz) {
//
//	}
//
//	/**
//	 * 扫描多个包,将所有定义了模型的类追加
//	 *
//	 * @param packageDirNames
//	 *            包名
//	 */
//	public static synchronized void scanPackages(String... packageDirNames) {
//		for (String name : packageDirNames) {
//			List<Class> list = PackageUtils.scanPackage(name);
//			for (Class<?> clazz : list) {
//				append(clazz);
//			}
//		}
//	}
//
//	/**
//	 * 根据模型名称获取一个模型
//	 *
//	 * @param name
//	 *            模型名称
//	 * @return 模型
//	 */
//	public static Model get(String name) {
//		Model model = models.get(name);
//		if (null == model) {
//			throw new NonExistException("模型[" + name + "]");
//		}
//		return model;
//	}
//
//	/**
//	 * 根据名称创建一个查询生成器
//	 *
//	 * @param name
//	 *            模型名称
//	 * @return 查询生成器
//	 */
//	public static Query createQuery(String name) {
//		Model model = get(name);
//		return model.createQuery();
//	}
//
//	/**
//	 * 根据名称创建一个写入生成器
//	 *
//	 * @param name
//	 *            名称
//	 * @return 写入生成器
//	 */
//	public static Writer createWriter(String name) {
//		Model model = get(name);
//		return model.createWriter();
//	}
//
//}
