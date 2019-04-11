package com.ACID.core.mapper;

public enum BaseColumns {
	id("id"),
	createBy("create_by"),
	createDate("create_date"),
	upadateBy("upadate_by"),
	updateDate("update_date"),
	remarks("remarks"),
	delFlag("del_flag");
	
	private BaseColumns(String value) {
		this.value=value;
	}
	
	private  String value;
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
