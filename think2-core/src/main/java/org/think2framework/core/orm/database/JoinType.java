package org.think2framework.core.orm.database;

/**
 * 数据库关联类型
 */
public enum JoinType {

	INNER("INNER JOIN"), LEFT("LEFT JOIN"), RIGHT("RIGHT JOIN");

	private String value;

	JoinType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
