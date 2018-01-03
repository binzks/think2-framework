package org.think2framework.core.persistence;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.think2framework.core.Constants;

/**
 * 模型列定义
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Column {

	String name() default "";// 字段名称，默认空的时候取java定义字段的名称

	String title() default "";// 标题，如果为空则取name

	String tag() default Constants.FIELD_TEXT;// 字段标签，默认是文本，如果字段定义是整型或者长整型则自动修改为整型，如果是浮点则修改为浮点

	boolean nullable() default true;// 字段是否可空，默认可空

	String alias() default "";// 字段别名，主要用于关联表的时候给字段设置

	String join() default "";// 字段所属关联名称,如果为空表示主表字段

	int length() default 50;// 字段长度，默认50

	int scale() default 0;// 字段精度(小数位数)，默认0

	String defaultValue() default "";// 字段默认值，默认空不设置默认值，now当前时间，user.id登录用户id，user.name登录用户名，其他则填值

	boolean search() default false;// 是否作为搜索项，默认false

	boolean display() default true;// 查询是否显示列，默认true

	boolean detail() default true;// 显示详情是否显示列，默认true

	boolean add() default true;// 添加是否需要添加列，默认true

	boolean edit() default true;// 编辑是否需要列，默认true

	boolean rowFilter() default false;// 是否行级过滤，默认false，只有当类型为item或者dataItem的时候才有效

	String comment() default "";// 字段注释，如果为空则取title，如果有定义item，则将item也写入注释

}
