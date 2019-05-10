package com.code.core.beanvalidator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.code.utils.AjaxJson;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * JSR303 Validator(Hibernate Validator)工具类.
 * 
 * ConstraintViolation中包含propertyPath, message 和invalidValue等信息.
 * 提供了各种convert方法，适合不同的i18n需求:
 * 1. List<String>, String内容为message
 * 2. List<String>, String内容为propertyPath + separator + message
 * 3. Map<propertyPath, message>
 * 
 * 详情见wiki: https://github.com/springside/springside4/wiki/HibernateValidator
 * @author calvin
 * @version 2013-01-15
 */
@Component
public class BeanValidators {
	

	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
	
	
	
	/**
	 * 调用JSR303的validate方法, 验证失败时抛出ConstraintViolationException.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  void validateWithException(Validator validator, Object object, Class<?>... groups)
			throws ConstraintViolationException {
		Set constraintViolations = validator.validate(object, groups);
		if (!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

	/**
	 * 辅助方法, 转换ConstraintViolationException中的Set<ConstraintViolations>中为List<message>.
	 */
	public  List<String> extractMessage(ConstraintViolationException e) {
		return extractMessage(e.getConstraintViolations());
	}

	/**
	 * 辅助方法, 转换Set<ConstraintViolation>为List<message>
	 */
	@SuppressWarnings("rawtypes")
	public  List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations) {
		List<String> errorMessages = Lists.newArrayList();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.add(violation.getMessage());
		}
		return errorMessages;
	}

	/**
	 * 辅助方法, 转换ConstraintViolationException中的Set<ConstraintViolations>为Map<property, message>.
	 */
	public  Map<String, String> extractPropertyAndMessage(ConstraintViolationException e) {
		return extractPropertyAndMessage(e.getConstraintViolations());
	}

	/**
	 * 辅助方法, 转换Set<ConstraintViolation>为Map<property, message>.
	 */
	@SuppressWarnings("rawtypes")
	public  Map<String, String> extractPropertyAndMessage(Set<? extends ConstraintViolation> constraintViolations) {
		Map<String, String> errorMessages = Maps.newHashMap();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
		}
		return errorMessages;
	}

	/**
	 * 辅助方法, 转换ConstraintViolationException中的Set<ConstraintViolations>为List<propertyPath message>.
	 */
	public  List<String> extractPropertyAndMessageAsList(ConstraintViolationException e) {
		return extractPropertyAndMessageAsList(e.getConstraintViolations(), " ");
	}

	/**
	 * 辅助方法, 转换Set<ConstraintViolations>为List<propertyPath message>.
	 */
	@SuppressWarnings("rawtypes")
	public  List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations) {
		return extractPropertyAndMessageAsList(constraintViolations, " ");
	}

	/**
	 * 辅助方法, 转换ConstraintViolationException中的Set<ConstraintViolations>为List<propertyPath +separator+ message>.
	 */
	public  List<String> extractPropertyAndMessageAsList(ConstraintViolationException e, String separator) {
		return extractPropertyAndMessageAsList(e.getConstraintViolations(), separator);
	}

	/**
	 * 辅助方法, 转换Set<ConstraintViolation>为List<propertyPath +separator+ message>.
	 */
	@SuppressWarnings("rawtypes")
	public  List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations,
			String separator) {
		List<String> errorMessages = Lists.newArrayList();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.add(violation.getPropertyPath() + separator + violation.getMessage());
		}
		return errorMessages;
	}
	
	
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	public boolean beanValidator(AjaxJson j, Object object, Class<?>... groups) {
		try{
			validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = extractPropertyAndMessageAsList(ex, ": ");
			//错误信息处理
			int i=0;
			StringBuffer sb=new StringBuffer();
			if(list!=null){
				for(String l:list){
					if(i==0){
						sb.append(l);
					}else{
						sb.append(","+l);
					}
					i++;
				}
			}
			j.setMsg(sb.toString());
			return false;
		}
		return true;
	}
	
	
}