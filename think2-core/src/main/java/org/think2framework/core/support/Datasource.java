package org.think2framework.core.support;

/**
 * 数据源定义
 */
public enum Datasource {

	MYSQL("mysql", "mysql");

	private String name;

	private String value;

	private Datasource(String name, String value) {
		this.name = name;
		this.value = value;
	}




//    public Datasource valueOf(String value){
//	    return null;
//    }
}
