package com.code.utils;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Parameter;

public class CommonJavaType {

	
	/**
	 * request
	 */
	public static FullyQualifiedJavaType request = new FullyQualifiedJavaType("javax.servlet.http.HttpServletRequest");
	
	
	/**
	 * 事务
	 */
	public static FullyQualifiedJavaType transactional = new FullyQualifiedJavaType("org.springframework.transaction.annotation.Transactional");
	
	
	/**
	 * request  name
	 */
	public static String request_name="request";
	
	/**
	 * 重写
	 */
	public static String override="@Override";
	
	/**
	 * 自动注解
	 */
	public static FullyQualifiedJavaType Autowired = new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired");
	
	
	/**
	 * requestBody
	 * 注解
	 */
	public static  FullyQualifiedJavaType requestBody=new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestBody");
	/**
	 * 自动注解 参数名称
	 */
	public static String Autowired_name="@Autowired";
	
	/**
	 * 自动注解 service
	 */
	public static FullyQualifiedJavaType annotation_Service=new FullyQualifiedJavaType("org.springframework.stereotype.Service");
	
	/**
	 * 自动注解 service
	 */
	public static String annotation_Service_name="@Service";
	
	/**
	 * 分页
	 */
	public static FullyQualifiedJavaType pageInfo = new FullyQualifiedJavaType("com.github.pagehelper.PageInfo");
	
	/**
	 * ajax 返回值
	 */
	public static  FullyQualifiedJavaType ajax = new FullyQualifiedJavaType(PropertiesUtils.get("ajaxjson"));
	
	/**
	 * data 方法 反回值
	 */
	public static  FullyQualifiedJavaType page = new FullyQualifiedJavaType(PropertiesUtils.get("page"));
	
	
	/**
	 * 国际化18n
	 */
	public static FullyQualifiedJavaType myLocaleResolver = new FullyQualifiedJavaType(PropertiesUtils.get("MyLocaleResolver"));
	
	/**
	 * 国际化i8n 参数名称
	 */
	public static String myLocaleResolver_name="myLocaleResolver";
	

	/**
	 * 字符串工具类
	 */
	public static FullyQualifiedJavaType stringUtils = new FullyQualifiedJavaType(PropertiesUtils.get("StringUtils"));
	
	/**
	 * baseController
	 */
	public static FullyQualifiedJavaType baseController=new FullyQualifiedJavaType("com.code.core.controller.BaseController");
	
	/**
	 *  注解 Controller
	 */
	public static FullyQualifiedJavaType controller=new FullyQualifiedJavaType("org.springframework.stereotype.Controller");
	
	/**
	 * requestMapping
	 */
	public static FullyQualifiedJavaType requestMapping=new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMapping");
	
	/**
	 * shiro 权限
	 */
	public static FullyQualifiedJavaType requiresPermissions=new FullyQualifiedJavaType("org.apache.shiro.authz.annotation.RequiresPermissions");
	
	/**
	 * apiImplicitParam
	 */
	public static FullyQualifiedJavaType apiImplicitParam=new FullyQualifiedJavaType("io.swagger.annotations.ApiImplicitParam");
	
	/**
	 * 验证 Validated
	 */
	public static FullyQualifiedJavaType validated=new FullyQualifiedJavaType("org.springframework.validation.annotation.Validated");
	
	/**
	 * 验证长度
	 */
	public static FullyQualifiedJavaType validated_length=new FullyQualifiedJavaType("org.hibernate.validator.constraints.Length");
	
	/**
	 * 验证 null
	 */
	public static FullyQualifiedJavaType validated_Null=new FullyQualifiedJavaType("javax.validation.constraints.Null");
	
	/**
	 * date格式
	 */
	public static FullyQualifiedJavaType date_JsonFormat=new FullyQualifiedJavaType("com.fasterxml.jackson.annotation.JsonFormat");
	
	/**
	 * java.io.Serializable
	 */
	public static FullyQualifiedJavaType serializable=new FullyQualifiedJavaType("java.io.Serializable");
	
	/**
	 * getMapping
	 */
	public static FullyQualifiedJavaType getMapping=new FullyQualifiedJavaType("org.springframework.web.bind.annotation.GetMapping");
	
	/**
	 * responseBody
	 */
	public static FullyQualifiedJavaType responseBody=new FullyQualifiedJavaType("org.springframework.web.bind.annotation.ResponseBody");
	
	/**
	 * postMapping
	 */
	public static FullyQualifiedJavaType postMapping=new FullyQualifiedJavaType("org.springframework.web.bind.annotation.PostMapping");
	
	
	
	/**
	 * apiImplicitParams
	 */
	public static FullyQualifiedJavaType apiImplicitParams=new FullyQualifiedJavaType("io.swagger.annotations.ApiImplicitParams");
	
	/**
	 * apiOperation
	 */
	public static FullyQualifiedJavaType apiOperation=new FullyQualifiedJavaType("io.swagger.annotations.ApiOperation");
	
	/**
	 * 注解 Controller的名称
	 */
	public static String annotation_controller_name="@Controller";
	
	/**
	 * list 
	 */
	public static FullyQualifiedJavaType list = new FullyQualifiedJavaType("java.util.List");
	
	/**
	 * data
	 */
	public static FullyQualifiedJavaType date= new FullyQualifiedJavaType("java.util.Date");
	
	/**
	 * slf4j   Logger
	 */
	public static FullyQualifiedJavaType logger= new FullyQualifiedJavaType("org.slf4j.Logger");
	
	/**
	 * slf4j   Logger 属性名
	 */
	public static String logger_name="logger";
	
	/**
	 * String
	 */
	public static FullyQualifiedJavaType String= new FullyQualifiedJavaType("String");
	
	
	/**
	 * slf4j   LoggerFactory
	 */
	public static FullyQualifiedJavaType loggerFactory= new FullyQualifiedJavaType("org.slf4j.LoggerFactory");
	
	/**
	 * 错误
	 */
	public static FullyQualifiedJavaType webErrorNotice= new FullyQualifiedJavaType("com.code.utils.WebErrorNotice");
	
	/**
	 * 获取实体类
	 * @param shortName
	 * @return
	 */
	public static FullyQualifiedJavaType getPojo(String shortName){
		
		return  new FullyQualifiedJavaType(
				PropertiesUtils.get("pojo.targetPackage") + "." + StringUtils.toUp(shortName));
	}
	
	/**
	 * 获取service impl 实现类
	 * @param shortName
	 * @return
	 */
	public static FullyQualifiedJavaType getImpl(String shortName){
		
		return new FullyQualifiedJavaType(
				PropertiesUtils.get("service.targetPackage") + "." + "impl." + shortName + "ServiceImpl");
	}
	
	/**
	 * 获取Controller
	 * @param shortName
	 * @return
	 */
	public static FullyQualifiedJavaType getController(String shortName){
		
		return  new FullyQualifiedJavaType(
				PropertiesUtils.get("controller.targetPackage") + "." + shortName + "Controller");
	}
	
	/**
	 * 获取 baseService
	 * @param shortName
	 * @return
	 */
	public static FullyQualifiedJavaType getBaseService(String shortName){
		
		FullyQualifiedJavaType baseService = new FullyQualifiedJavaType(PropertiesUtils.get("baseService"));

		// 添加泛型
		baseService.addTypeArgument(getMapper(shortName));
		baseService.addTypeArgument(CommonJavaType.getPojo(shortName));
		return baseService;
	}
	
	/**
	 * 获取mapper
	 * @param shortName
	 * @return
	 */
	public static FullyQualifiedJavaType getMapper(String shortName){
		return  new FullyQualifiedJavaType(
				PropertiesUtils.get("mapper.targetPackage") + "." + shortName + "Mapper");
	}
	
	/**
	 * 获取service
	 * @param shortName
	 * @return
	 */
	public static FullyQualifiedJavaType getService(String shortName){
		
		return  new FullyQualifiedJavaType(
				PropertiesUtils.get("service.targetPackage") + "." + shortName + "Service");
	}
	
	/**
	 * 获取basepojo
	 * @return
	 */
	public static FullyQualifiedJavaType getBasePojo(){
		String basePojo = PropertiesUtils.get("pojo.rootClass");
		return new FullyQualifiedJavaType(basePojo);
	}
	
	/**
	 * 获取枚举
	 * @param shortName
	 * @return
	 */
	public static FullyQualifiedJavaType getPojoEnum(String shortName){
		return new FullyQualifiedJavaType(
				PropertiesUtils.get("pojoenum.pojoEnumPageName") + "." + shortName + "Enum");
	}
	
	/**
	 * 获取request 参数
	 * @return
	 */
	public static Parameter getRequestParameter(){
		return new Parameter(request, request_name);
	}
	
	/**
	 * 获取pojo 参数
	 * @param shortName
	 * @return
	 */
	public static Parameter getPojoParameter(String shortName){
		return new Parameter(getPojo(shortName), StringUtils.tolow(shortName));
	}
	
	/**
	 * 获取 id 参数
	 * @return
	 */
	public static Parameter getIdParameter(){
		return new Parameter(new FullyQualifiedJavaType("String"), "id");
	}
	

	
}
