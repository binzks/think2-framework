package org.think2framework.core.bean;

import org.think2framework.core.persistence.FieldType;

/**
 * 视图列定义
 */
public class Column {

	private String name; // 名称

	private FieldType type = FieldType.TEXT; // 类型，默认文本

	private Boolean nullable = true; // 是否可空

	private String title; // 标题，如果为空则取名称

	private Boolean search = false; // 是否作为搜索页，默认false，TEXT查询为like

	private Boolean display = true; // 查询页面是否显示列，默认true

	private Boolean detail = true; // 显示详情页面是否显示列，默认true

	private Boolean add = true;// 添加页面是否需要添加列，默认true

	private Boolean edit = true; // 编辑页面是否需要列，默认true

	private String param; // 系统参数名称

	public Column() {
	}

	public Column(String name, FieldType type, Boolean nullable, String title, Boolean search, Boolean display,
			Boolean detail, Boolean add, Boolean edit, String param) {
		this.name = name;
		this.type = type;
		this.nullable = nullable;
		this.title = title;
		this.search = search;
		this.display = display;
		this.detail = detail;
		this.add = add;
		this.edit = edit;
		this.param = param;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	public Boolean getNullable() {
		return nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getSearch() {
		return search;
	}

	public void setSearch(Boolean search) {
		this.search = search;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public Boolean getDetail() {
		return detail;
	}

	public void setDetail(Boolean detail) {
		this.detail = detail;
	}

	public Boolean getAdd() {
		return add;
	}

	public void setAdd(Boolean add) {
		this.add = add;
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}
