package org.think2framework.context.bean;

import org.think2framework.core.persistence.FieldType;

/**
 * 模型的单元格
 */
public class Cell {

	private String name; // 名称

	private FieldType type = FieldType.TEXT; // 类型，默认文本

	private Boolean nullable = true; // 是否可空

	private String join; // 关联名称

	private String alias; // 别名

	private String defaultValue; // 默认值

	private Integer length; // 字段长度

	private Integer scale = 0; // 如果是小数，则填小数位数

	private String comment; // 注释

	private String title; // 标题，如果为空则取名称

	private Boolean search = false; // 是否作为搜索页，默认false，TEXT查询为like

	private Boolean display = true; // 查询页面是否显示列，默认true

	private Boolean detail = true; // 显示详情页面是否显示列，默认true

	private Boolean add = true;// 添加页面是否需要添加列，默认true

	private Boolean edit = true; // 编辑页面是否需要列，默认true

	private String param; // 系统参数名称

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getNullable() {
		return nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public String getJoin() {
		return join;
	}

	public void setJoin(String join) {
		this.join = join;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
