package com.code.utils;

import java.io.Serializable;

public class Table  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//数据库字段名
	private String columnName;
	
	//java属性 计出类别
	private String javaType;
	
	//字段对应的Java属性名
	private String javaColumnName;
	
	//字段是否可空 YES 可以空  NO 不可以
	private String isNullAble;
	
	//是否开启长度验证 0 不验证 1 验证
	private Integer lengthLimit;
	
	//长度最小限制
	private Integer lengthLimit_min;
	
	//长度错误提示
	private String lengthLimit_tip;
	
	//长度最大限制
	private Integer lengthLimit_max;
	
	//字段备注
	private String remarks;
	
	public Table() {
		// TODO Auto-generated constructor stub
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getJavaColumnName() {
		return javaColumnName;
	}

	public void setJavaColumnName(String javaColumnName) {
		this.javaColumnName = javaColumnName;
	}

	public String getIsNullAble() {
		return isNullAble;
	}

	public void setIsNullAble(String isNullAble) {
		this.isNullAble = isNullAble;
	}

	public Integer getLengthLimit() {
		return lengthLimit;
	}

	public void setLengthLimit(Integer lengthLimit) {
		this.lengthLimit = lengthLimit;
	}

	public Integer getLengthLimit_min() {
		return lengthLimit_min;
	}

	public void setLengthLimit_min(Integer lengthLimit_min) {
		this.lengthLimit_min = lengthLimit_min;
	}

	public String getLengthLimit_tip() {
		return lengthLimit_tip;
	}

	public void setLengthLimit_tip(String lengthLimit_tip) {
		this.lengthLimit_tip = lengthLimit_tip;
	}

	public Integer getLengthLimit_max() {
		return lengthLimit_max;
	}

	public void setLengthLimit_max(Integer lengthLimit_max) {
		this.lengthLimit_max = lengthLimit_max;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
	
	

}
