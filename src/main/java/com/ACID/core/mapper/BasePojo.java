package com.ACID.core.mapper;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class BasePojo {
	
	protected String id;
	
	protected String createBy;
	
	protected Date createDate;
	
	protected Date updateDate;
	
	protected String updateBy;
	
	/**
	 * 插入标记，默认false，id 系统默认生成。 值为true时，使用用户自定id插入数据
	 */
	protected boolean newRecorde=false;
	
	protected String delFlag="0";
	
	protected String del_delFlag="1";
	
	protected String remarks;
	
	protected String orderByClause;

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@JsonIgnore
	public String getDelFlag() {
		return delFlag!=null?delFlag:"0";
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	@JsonIgnore
	public String getDel_delFlag() {
		return del_delFlag;
	}

	public void setDel_delFlag(String del_delFlag) {
		this.del_delFlag = del_delFlag;
	}

	@JsonIgnore
	public boolean isNewRecorde() {
		return newRecorde;
	}

	public void setNewRecorde(boolean newRecorde) {
		this.newRecorde = newRecorde;
	}
	
	
	
}
