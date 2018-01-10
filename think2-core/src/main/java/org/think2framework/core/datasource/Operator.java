package org.think2framework.core.datasource;

/**
 * 数据库查询条件运算符
 */
public enum Operator {

	EQUAL("="), NOT_EQUAL("<>"), GREATER_THAN(">"), GREATER_EQUAL(">="), LESS_THAN("<"), LESS_EQUAL("<="), IS_NULL(
			"IS NULL"), NOT_NULL("IS NOT NULL"), IN(
					"IN"), NOT_IN("NOT IN"), LIKE("LIKE"), NOT_LIKE("NOT LIKE"), BETWEEN("BETWEEN ? AND ?");

	private String value;

	Operator(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
