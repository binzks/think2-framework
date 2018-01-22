package org.think2framework.core.persistence;

/**
 * 字段类型
 */
public enum FieldType {

	INT("整型"), TEXT("文本"), DATE("日期"), DATETIME("时间"), TIMESTAMP("时间戳"), HTML("Html"), FLOAT("小数"), BOOL(
			"Bool"), TEXTAREA("长文本"), PASSWORD("密码"), ITEM("单选"), ITEM_INT("数字单选"), MULTIPLE("多选"), DATA_ITEM_INT(
					"数据源数字单选"), DATA_ITEM("数据源单选"), DATA_MULTIPLE("数据源多选"), IMAGE(
							"图片"), FILE("文件"), EMAIL("邮箱"), MOBILE("座机"), TELEPHONE("手机号码"), JSON("Json");

	private String value;

	FieldType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
