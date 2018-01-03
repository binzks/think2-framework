package org.think2framework.core.persistence;

import org.think2framework.core.Constants;

import java.lang.annotation.*;

/**
 * 查询排序定义
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = Orders.class)
public @interface Order {

	/**
	 * 排序字段，可以多个
	 * 
	 * @return 排序字段
	 */
	String[] keys();

	/**
	 * 排序类型asc、desc
	 * 
	 * @return 排序类型
	 */
	String type() default Constants.ORDER_TYPE_DESC;

}
