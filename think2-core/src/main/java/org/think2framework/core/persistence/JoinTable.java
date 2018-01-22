package org.think2framework.core.persistence;

import java.lang.annotation.*;

import org.think2framework.core.datasource.JoinType;

/**
 * 查询关联注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = JoinTables.class)
public @interface JoinTable {

	String name();// 关联的名称，用于标识关联，获取关联表的表名和别名

	String database() default "";// 关联表所在数据库名称

	String table();// 关联的表名称

	JoinType joinType() default JoinType.LEFT;// 关联类型 left join, right join, inner join

	String key();// 关联表的字段名称

	String joinName() default "";// 关联的主表的关联名称，如果null或者空表示关联主表，如果不为null表示关联实体的关联表

	String joinKey();// 关联的主表的字段名称

	String filter();// 额外的过滤条件，手动加上在join后

}
