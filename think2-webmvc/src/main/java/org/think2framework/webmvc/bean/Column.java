package org.think2framework.webmvc.bean;

import java.util.List;

import org.think2framework.core.orm.bean.Item;

/**
 * Created by zhoubin on 2017/6/12. 模型的列定义
 */
public class Column {

	private String title;// 标题

	private Boolean search = false; // 是否作为搜索项，默认false，TEXT查询为like

	private Boolean display = true; // 查询是否显示列，默认true

	private Boolean detail = true; // 显示详情是否显示列，默认true

	private Boolean add = true;// 添加是否需要添加列，默认true

	private Boolean edit = true; // 编辑是否需要列，默认true

	private Boolean rowFilter = false; // 是否行级过滤，默认false，只有当类型为item或者dataItem的时候才有效

	private List<Item> items;// 单元格的item定义，主要用于状态选择、人员选择等选择类

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

	public Boolean getRowFilter() {
		return rowFilter;
	}

	public void setRowFilter(Boolean rowFilter) {
		this.rowFilter = rowFilter;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
