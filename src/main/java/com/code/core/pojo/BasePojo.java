package com.code.core.pojo;

import java.util.Date;
/**
 * 公共实体继承类
 * @author ACID
 *
 */
public class BasePojo {
	

	protected String id;
	
	protected String createBy;
	
	protected Date createDate;
	
	protected Date updateDate;
	
	protected String updateBy;
	
	protected String delFlag="0";
	
	protected String del_delFlag="1";
	
	/**
	 * 插入标记，默认false，id 系统默认生成。 值为true时，使用用户自定id插入数据
	 */
	protected boolean newRecorde=false;
	
	protected String remarks;
	
	protected String orderByClause;
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

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

	public String getDelFlag() {
		return delFlag;
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

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public String getDel_delFlag() {
		return del_delFlag;
	}

	public void setDel_delFlag(String del_delFlag) {
		this.del_delFlag = del_delFlag;
	}

	public boolean isNewRecorde() {
		return newRecorde;
	}

	public void setNewRecorde(boolean newRecorde) {
		this.newRecorde = newRecorde;
	}
	
	/**
	 * 插入之前
	 */
	public void preInsert(){
		this.createDate=new Date();
		this.updateDate=new Date();
	}
	
	/**
	 * 更新之前
	 */
	public void preUpdate(){
		this.updateDate=new Date();
		
	}
	
	
	
	
	
	
}
