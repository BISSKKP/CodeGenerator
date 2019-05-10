package com.code.utils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * $.ajax后需要接受的JSON
 * 
 * @howard
 * 
 */
public class AjaxJson implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean success = true;// 是否成功
	
	// 响应业务状态
	private Integer status;
	
	private String errorCode = "-1";//  返回1000时，表示用户需要登陆，返回2000时登陆超时 重新登陆
	private String msg = "操作成功";//
	//private LinkedHashMap<String, Object> body = new LinkedHashMap<String, Object>();//封装json的map
	private Map<String, Object> body = new HashMap<String, Object>();//封装json的map
	public Map<String, Object> getBody() {
		return body;
	}

	public void setBody(HashMap<String, Object> body) {
		this.body = body;
	}

	public void put(String key, Object value){//向json中添加属性，在js中访问，请调用data.map.key
		body.put(key, value);
	}
	
	public void remove(String key){
		body.remove(key);
	}
	
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {//向json中添加属性，在js中访问，请调用data.msg
		this.msg = msg;
	}


	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	@JsonIgnore//返回对象时忽略此属性
	public String getJsonStr() {//返回json字符串数组，将访问msg和key的方式统一化，都使用data.key的方式直接访问。

		String json =JsonUtils.objectToJson(this);
		return json;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
	
	public static void main(String[] args) {
		AjaxJson j=new AjaxJson();
		System.out.println(j.getJsonStr());
	}

	public Integer getStatus() {
		if(null==status){
			this.status=200;
		}
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public AjaxJson() {
	}
	
	public AjaxJson(boolean success) {
		this.success=success;
	}
	
	/**
	 * 快速返回一个错误信息
	 * @param status
	 * @param errorCode 可以 null
	 * 
	 * @param msg
	 * @return
	 */
	public static AjaxJson  error(Integer status,String errorCode,String msg){
		AjaxJson j=new AjaxJson();
		j.setMsg(msg);
		if(null!=errorCode){
			j.setErrorCode(errorCode);
		}
		j.setStatus(status);
		j.setSuccess(false);
		return j;
	}
	
	/**
	 * 快速返回一个成功信息
	 * @param msg
	 * @return
	 */
	public static AjaxJson  success(String key,Object value){
		AjaxJson j=new AjaxJson();
		j.put(key, value);
		return j;
	}
	
	
}
