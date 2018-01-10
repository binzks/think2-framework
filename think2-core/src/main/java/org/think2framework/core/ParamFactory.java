//package org.think2framework.core;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.think2framework.core.bean.Param;
//import org.think2framework.core.datasource.Query;
//import org.think2framework.core.exception.MessageException;
//import org.think2framework.core.exception.SystemMessage;
//import org.think2framework.core.utils.StringUtils;
//
///**
// * 参数工厂，参数分为固定参数和可变参数，固定参数从常量配置获取对应分组，可变参数从数据库获取
// */
//public class ParamFactory {
//
//	private static final Logger logger = LogManager.getLogger(ParamFactory.class);
//
//	private static Map<String, Param> paramMap = new HashMap<>(); // 基本参数配置
//
//	/**
//	 * 追加一个系统参数，如果参数没有定义模型则表示纯静态参数，添加到constants
//	 *
//	 * @param param
//	 */
//	public static synchronized void append(Param param) {
//		String name = param.getName();
//		if (null != paramMap.get(name)) {
//			logger.warn("系统参数[{}]已经存在，忽略追加！", name);
//		} else {
//			boolean append = true;
//			if (StringUtils.isNotBlank(param.getModel())) {
//				if (StringUtils.isBlank(param.getKey()) || StringUtils.isBlank(param.getDisplay())) {
//					append = false;
//					logger.warn("系统参数[{}]定义了模型，没有定义取值字段或者显示字段！", name);
//				}
//			}
//			if (append) {
//				paramMap.put(name, param);
//				logger.debug("追加参数[{}]！", name);
//			}
//		}
//	}
//
//	/**
//	 * 根据参数名称获取一个参数的所有值，如果定义了取值model则从model获取新的值
//	 *
//	 * @param name
//	 *            参数名称
//	 * @return 参数值map
//	 */
//	public static Map<String, String> get(String name) {
//		Param param = paramMap.get(name);
//		if (null == param) {
//			throw new MessageException(SystemMessage.NON_EXIST.getCode(), "系统参数" + name);
//		}
//		Map<String, String> map = new HashMap<>(param.getValues());
//		if (StringUtils.isNotBlank(param.getModel())) {
//			String key = param.getKey();
//			String display = param.getDisplay();
//			Query query = OrmFactory.createQuery(param.getModel());
//			query.fields(key, display);
//			List<Map<String, Object>> list = query.queryForList();
//			if (null != list) {
//				for (Map<String, Object> m : list) {
//					map.put(StringUtils.toString(m.get(key)), StringUtils.toString(m.get(display)));
//				}
//			}
//		}
//		return map;
//	}
//
//	/**
//	 * 获取一个参数对应的值
//	 *
//	 * @param name
//	 *            参数名称
//	 * @param display
//	 *            具体取值的显示名称
//	 * @return 参数值
//	 */
//	public static String get(String name, String display) {
//		Map<String, String> map = get(name);
//		String value = map.get(display);
//		if (StringUtils.isBlank(value)) {
//			throw new MessageException(SystemMessage.NON_EXIST.getCode(), "系统参数" + name + "值" + display);
//		}
//		return value;
//	}
//}
