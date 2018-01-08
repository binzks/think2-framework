//package org.think2framework.webmvc.bean;
//
//import org.think2framework.core.persistence.Tag;
//
///**
// * Created by zhoubin on 2017/6/12. 模型的字段
// */
//public class Field {
//
//	private String name; // 名称
//
//	private Tag tag = Tag.TEXT; // 标签，默认是文本
//
//	private Boolean nullable = true; // 是否可空，默认可空
//
//	private String alias = ""; // 字段别名，主要用于关联表的时候给字段设置
//
//	private String join = ""; // 字段所属关联名称,如果为空表示主表
//
//	private Integer length = 50; // 字段长度
//
//	private Integer scale = 0; // 字段精度(小数位数)，默认0
//
//	private String defaultValue = ""; // 默认值，默认空不设置默认值，now当前时间，user.id登录用户id，user.name登录用户名，其他则填值
//
//	private String comment; // 注释，如果为空则取title，如果有定义item，则将item也写入注释
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public Tag getTag() {
//		return tag;
//	}
//
//	public void setTag(Tag tag) {
//		this.tag = tag;
//	}
//
//	public Boolean getNullable() {
//		return nullable;
//	}
//
//	public void setNullable(Boolean nullable) {
//		this.nullable = nullable;
//	}
//
//	public String getAlias() {
//		return alias;
//	}
//
//	public void setAlias(String alias) {
//		this.alias = alias;
//	}
//
//	public String getJoin() {
//		return join;
//	}
//
//	public void setJoin(String join) {
//		this.join = join;
//	}
//
//	public Integer getLength() {
//		return length;
//	}
//
//	public void setLength(Integer length) {
//		this.length = length;
//	}
//
//	public Integer getScale() {
//		return scale;
//	}
//
//	public void setScale(Integer scale) {
//		this.scale = scale;
//	}
//
//	public String getDefaultValue() {
//		return defaultValue;
//	}
//
//	public void setDefaultValue(String defaultValue) {
//		this.defaultValue = defaultValue;
//	}
//
//	public String getComment() {
//		return comment;
//	}
//
//	public void setComment(String comment) {
//		this.comment = comment;
//	}
//}
