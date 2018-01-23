package org.think2framework.core.bean;

import java.util.List;
import java.util.Map;

/**
 * 模型对应的页面视图
 */
public class View {

	private String title; // 标题

	private Integer rowSize; // 列表页面每次显示多少条数据

	private Map<String, Column> columns; // 列定义

	private List<Action> actions; // 操作按钮

	public View() {
	}

	public View(String title, Integer rowSize, Map<String, Column> columns, List<Action> actions) {
		this.title = title;
		this.rowSize = rowSize;
		this.columns = columns;
		this.actions = actions;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getRowSize() {
		return rowSize;
	}

	public void setRowSize(Integer rowSize) {
		this.rowSize = rowSize;
	}

	public Map<String, Column> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, Column> columns) {
		this.columns = columns;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
}
