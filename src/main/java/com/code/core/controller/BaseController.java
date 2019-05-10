package com.code.core.controller;



import org.springframework.beans.factory.annotation.Autowired;

import com.code.core.beanvalidator.BeanValidators;


public class BaseController {
	
	/**
	 * 参数验证
	 */
	@Autowired
	protected BeanValidators beanValidators;
	
	

}
