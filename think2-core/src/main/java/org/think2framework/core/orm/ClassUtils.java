package org.think2framework.core.orm;

import org.springframework.util.LinkedCaseInsensitiveMap;
import org.think2framework.core.orm.bean.SqlObject;
import org.think2framework.core.persistence.Column;
import org.think2framework.core.exception.DatabaseException;
import org.think2framework.core.utils.JsonUtils;
import org.think2framework.core.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 类工具,主要处理类字段,读取和写入,以及创建实例
 */
public class ClassUtils {

	/**
	 * bool类型的true值
	 */
	public static final Integer TRUE_VALUE = 1;

	/**
	 * bool类型的false值
	 */
	public static final Integer FALSE_VALUE = 0;

	/**
	 * 获取字段的名称,如果定义了别名以别名为准,如果定义了名称以名称为准,如果都没有以字段本身名称为准
	 * 
	 * @param field
	 *            字段
	 * @return 字段的名称
	 */
	private static String getFieldKey(Field field) {
		String key = field.getName();
		Column column = field.getAnnotation(Column.class);
		if (null != column) {
			if (StringUtils.isNotBlank(column.name())) {
				key = column.name();
			}
			if (StringUtils.isNotBlank(column.alias())) {
				key = column.alias();
			}
		}
		return key;
	}

	/**
	 * 获取一个类的字段，如果存在父类则循环获取父类的字段，如果子类和父类存在相同名称的字段则以子类为准，父类的不获取
	 *
	 * @param clazz
	 *            类
	 * @return 字段数组
	 */
	public static List<Field> getFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		Collections.addAll(fields, clazz.getDeclaredFields());
		return getFields(fields, clazz);
	}

	/**
	 * 获取一个类的字段，如果存在父类则循环获取父类的字段，如果子类和父类存在相同名称的字段则以子类为准，父类的不获取
	 * 
	 * @param fields
	 *            已经添加的字段
	 * @param clazz
	 *            类
	 * @return 字段数组
	 */
	public static List<Field> getFields(List<Field> fields, Class<?> clazz) {
		Class<?> superClass = clazz.getSuperclass();
		if (Object.class == superClass) {
			return fields;
		} else {
			Field[] superFields = superClass.getDeclaredFields();
			// 如果子类不存在字段则追加
			for (Field field : superFields) {
				try {
					clazz.getDeclaredField(field.getName());
				} catch (NoSuchFieldException e) {
					fields.add(field);
				}
			}
			return getFields(fields, superClass);
		}
	}

	/**
	 * 从实例获取一个字段，如果不是object对象则从父类获取字段
	 * 
	 * @param clazz
	 *            类
	 * @param name
	 *            字段名称
	 * @return 字段
	 */
	private static Field getField(Class<?> clazz, String name) {
		Field field = null;
		try {
			field = clazz.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			List<Field> fields = getFields(clazz);
			for (Field f : fields) {
				String key = getFieldKey(f);
				if (key.equals(name)) {
					field = f;
					break;
				}
			}
			if (null == field) {
				throw new DatabaseException(clazz.getName() + " field " + name + " is not exist");
			}
		}
		return field;
	}

	/**
	 * 获取实例的一个字段值,如果是map类型使用get,如果是其他则使用反射获取字段值
	 * 
	 * @param instance
	 *            实例
	 * @param name
	 *            字段名称
	 * @return 字段值
	 */
	public static Object getFieldValue(Object instance, String name) {
		Class clazz = instance.getClass();
		Object value;
		if (HashMap.class == clazz || LinkedHashMap.class == clazz || LinkedCaseInsensitiveMap.class == clazz) {
			Map map = (Map) instance;
			value = map.get(name);
		} else {
			Field field = getField(instance.getClass(), name);
			try {
				field.setAccessible(true);
				value = field.get(instance);
			} catch (IllegalAccessException e) {
				throw new DatabaseException(e);
			}
		}
		return getDatabaseValue(value);
	}

	/**
	 * 从数据库获取的值转化为java的值,bool类型0，1转为bool值，json数据和复杂类型从json字符串转为对象
	 * 
	 * @param value
	 *            数据库值
	 * @return java值
	 */
	public static Object getJavaValue(Field field, Object value) {
		if (null == value || null == field) {
			return null;
		}
		if (field.getType() == Boolean.class) {
			return TRUE_VALUE.toString().equalsIgnoreCase(StringUtils.toString(value));
		} else if (field.getType().isArray() || (field.getGenericType() instanceof ParameterizedType)) {
			return JsonUtils.readString(StringUtils.toString(value), field.getGenericType());
		} else {
			return value;
		}
	}

	/**
	 * 将一个java值转化为数据库存储的值，如果是bool则转化为整型，如果是数组和其他复杂类型转化成json字符串
	 * 
	 * @param value
	 *            java值
	 * @return 数据库值
	 */
	public static Object getDatabaseValue(Object value) {
		if (null == value) {
			return null;
		}
		if (Boolean.class == value.getClass()) {
			return Boolean.parseBoolean(StringUtils.toString(value)) ? TRUE_VALUE : FALSE_VALUE;
		} else if (ArrayList.class == value.getClass() || value instanceof Object[]
				|| value instanceof ParameterizedType) {
			return JsonUtils.toString(value);
		} else {
			return value;
		}
	}

	/**
	 * 根据sql的resultSet和类创建一个类的实例
	 * 
	 * @param clazz
	 *            类
	 * @param rs
	 *            sql结果集
	 * @return 类实例
	 */
	public static <T> T createInstance(Class<T> clazz, ResultSet rs) {
		try {
			T instance = clazz.newInstance();
			List<Field> fields = getFields(clazz);
			for (Field field : fields) {
				String name = getFieldKey(field);
				try {
					field.setAccessible(true);
					field.set(instance, getJavaValue(field, rs.getObject(name)));
				} catch (SQLException e) {
					if (e.getSQLState().equals("S1000")) { // 表示ResultSet是空的,没有数据
						return null;
					} else if (!e.getSQLState().equals("S0022")) { // S0022表示字段name不存在
						throw new DatabaseException(e);
					}
				}
			}
			return instance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new DatabaseException(e);
		}
	}

	/**
	 * 创建一个新增，返回新增sql语句和值
	 * 
	 * @param instance
	 *            待新增的数据对象
	 * @param table
	 *            表名
	 * @param pk
	 *            主键名称
	 * @param id
	 *            id值，如果是自增长则为null
	 * @param columns
	 *            列
	 * @param keyBegin
	 *            关键字前缀
	 * @param keyEnd
	 *            关键字后缀
	 * @return sql和值
	 */
	public static SqlObject createInsert(Object instance, String table, String pk, String id,
			Map<String, org.think2framework.core.bean.Column> columns, String keyBegin, String keyEnd) {
		StringBuffer fieldSql = new StringBuffer();
		StringBuffer valueSql = new StringBuffer();
		List<Object> values = new ArrayList<>();
		if (null != id) {
			fieldSql.append(",").append(keyBegin).append(pk).append(keyEnd);
			valueSql.append(",?");
			values.add(id);
		}
		for (org.think2framework.core.bean.Column column : columns.values()) {
			// 如果字段不是主键,则添加
			String key = column.getName();
			if (!key.equals(pk)) {
				Object value = getFieldValue(instance, key);
				// 不新增null值
				if (null == value) {
					continue;
				}
				fieldSql.append(",").append(keyBegin).append(key).append(keyEnd);
				valueSql.append(",?");
				values.add(value);
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ").append(keyBegin).append(table).append(keyEnd).append("(")
				.append(fieldSql.toString().substring(1)).append(") VALUES (").append(valueSql.toString().substring(1))
				.append(")");
		return new SqlObject(sql.toString(), values);
	}

	/**
	 * 创建一个修改sql和值
	 * 
	 * @param instance
	 *            待修改的字段
	 * @param table
	 *            表名
	 * @param pk
	 *            主键名称
	 * @param keyBegin
	 *            关键字前缀
	 * @param keyEnd
	 *            关键字后缀
	 * @param columns
	 *            列
	 * @param keys
	 *            修改关键字
	 * @return sql和值
	 */
	public static SqlObject createUpdate(Object instance, String table, String pk, String keyBegin, String keyEnd,
			Map<String, org.think2framework.core.bean.Column> columns, String... keys) {
		StringBuffer fieldSql = new StringBuffer();
		StringBuilder keySql = new StringBuilder();
		List<Object> values = new ArrayList<>();
		for (org.think2framework.core.bean.Column column : columns.values()) {
			String key = column.getName();
			// 如果字段不是主键,则修改
			if (!key.equals(pk)) {
				Object value = ClassUtils.getFieldValue(instance, key);
				// 不修改null的字段
				if (null == value) {
					continue;
				}
				fieldSql.append(",").append(keyBegin).append(key).append(keyEnd).append("=?");
				values.add(value);
			}
		}
		if (null == keys || keys.length == 0) {
			keySql.append(" AND ").append(keyBegin).append(pk).append(keyEnd).append("=?");
			values.add(ClassUtils.getFieldValue(instance, pk));
		} else {
			for (String key : keys) {
				keySql.append(" AND ").append(keyBegin).append(key).append(keyEnd).append("=?");
				values.add(ClassUtils.getFieldValue(instance, key));
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ").append(keyBegin).append(table).append(keyEnd).append(" SET ")
				.append(fieldSql.toString().substring(1)).append(" WHERE 1=1").append(keySql.toString());
		return new SqlObject(sql.toString(), values);
	}
}
