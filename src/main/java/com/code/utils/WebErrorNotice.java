package com.code.utils;

import java.util.EnumSet;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
/**
 *  
 * 服务器返回特定状态码 和消息
 * 状态码组成格式 模块代码（2位）+错误序号（4位）=状态码（6位）
 * 现规定 状态码起始值 必须大于等于 10 0000
 * 所有业务状态码不得小于       50 0000
 * 
 * @author howard
 *
 */
public enum WebErrorNotice {
	
	SYS_OK(1,"OK"),
	/**
	 * sys 模块 10 
	 * 系统模块100000
	 */
	SYS_Busy(100000,"sys.busy"),
	/**
	 * sys 模块 10
	 * 系统异常
	 */
	SYS_ErrWait(100001,"sys.err.waiting"),
	
	/**
	 *  obj 模块 11
	 * 对象为空
	 */
	OBJ_NotFound(110001,"obj.notFound"),
	/**
	 * obj 模块 11
	 * 参数异常（为空）
	 */
	OBJ_ArgsErr(110002,"obj.argsErr")
	;
	
	
	/**
	 * 错误码
	 */
	private int code;
	
	/**
	 * 错误提示
	 */
	private String msg;
	
	
	private WebErrorNotice(int code,String msg) {
		this.code=code;
		this.msg=msg;
	}
	
	private MessageSource messageSource;
	
	public WebErrorNotice setMessageSource(MessageSource messageSource){
		this.messageSource=messageSource;
		return this;
	}
	 //通过静态内部类的方式注入bean，并赋值到枚举中
    @Component
    public static class ReportTypeServiceInjector {
        @Autowired
        private MessageSource messageSource;

        @PostConstruct
        public void postConstruct() {
            for (WebErrorNotice rt : EnumSet.allOf(WebErrorNotice.class))
                rt.setMessageSource(messageSource);
        }
    }

	public int getCode() {
		return code;
	}
	
	public String getStringCode() {
		return code+"";
	}


	public void setCode(int code) {
		this.code = code;
	}
	

	public String getMsg() {
		return messageSource==null?msg:messageSource.getMessage(msg,null,msg, LocaleContextHolder.getLocale());
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
