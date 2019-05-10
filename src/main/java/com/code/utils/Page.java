package com.code.utils;

import java.io.Serializable;
import java.util.List;

import com.github.pagehelper.PageInfo;


public class Page implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<?> list;
	
	
	
	private boolean rel;
	
	
	
	private String msg;
	
	private long count;
	
	
	
	public Page() {
	}
	public Page(List<?> list,Integer count) {
		this.list=list;
		this.count=count;
		this.msg="OK";
		this.rel=true;
	} 
	public Page(Boolean rel,String msg,List<?> list,Integer count) {
		this.list=list;
		this.count=count;
		this.msg=msg;
		this.rel=rel;
	}
	
	public Page(PageInfo<?> pageInfo){
		if(pageInfo!=null){
			this.list=pageInfo.getList();
			this.count=pageInfo.getTotal();
			this.msg="OK";
			this.rel=true;
		}else{
			this.rel=false;
			this.msg="暂无数据";
		}
	}
	
	

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public boolean isRel() {
		return rel;
	}

	public void setRel(boolean rel) {
		this.rel = rel;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
}
