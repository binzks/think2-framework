package org.think2framework.ide.bean;

import org.think2framework.core.datasource.JoinType;

/**
 * 查询实体的关联条件
 */
public class Join {

	private String name;// 关联的名称，用于标识关联，获取关联表的表名和别名

	private String database;// 关联表所在数据库名称

	private String table;// 关联的表名称

	private JoinType joinType = JoinType.LEFT;// 关联类型 left join, right join, inner join

	private String key;// 关联表的字段名称

	private String joinName;// 关联的主表的关联名称，如果null或者空表示关联主表，如果不为null表示关联实体的关联表

	private String joinKey;// 关联的主表的字段名称

	private String filter;// 额外的过滤条件，手动加上在join后

	public Join() {
	}

	public Join(String name, String database, String table, JoinType joinType, String key, String joinName,
			String joinKey, String filter) {
		this.name = name;
		this.database = database;
		this.table = table;
		this.joinType = joinType;
		this.key = key;
		this.joinName = joinName;
		this.joinKey = joinKey;
		this.filter = filter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public JoinType getJoinType() {
		return joinType;
	}

	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getJoinName() {
		return joinName;
	}

	public void setJoinName(String joinName) {
		this.joinName = joinName;
	}

	public String getJoinKey() {
		return joinKey;
	}

	public void setJoinKey(String joinKey) {
		this.joinKey = joinKey;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}
