package org.think2framework.webmvc.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库表定义
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Model {

	String name(); // 名称，唯一

	String table(); // 模型对应主表表名

	String redis() default ""; // redis数据源名称，如果为空表示不使用redis

	String pk() default "id"; // 主键名称，默认id

	boolean autoIncrement() default true;// 是否是整型自增长,如果不是则为UUID类型，默认true

	String[] uniques() default {};// 唯一性约束,可以定义多个，如果一个约束有多个字段用,隔开

	String[] indexes() default {};// 索引,可以定义多个，如果一个索引多个字段用,隔开

	String comment() default "";// 注释

}
