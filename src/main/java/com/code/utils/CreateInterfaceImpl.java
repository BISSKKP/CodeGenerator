package com.code.utils;

import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class CreateInterfaceImpl {
	/**
	 * 创建接口实现类
	 * 
	 * @param shortName
	 * @param mapperJavaFiles
	 * @param javaFormatter
	 */
	public static void createInterfaceImpl(String shortName, List<GeneratedJavaFile> mapperJavaFiles,
			JavaFormatter javaFormatter) {
		TopLevelClass impl = new TopLevelClass(CommonJavaType.getImpl(shortName));

		impl.setVisibility(JavaVisibility.PUBLIC);
		
		impl.addAnnotation(CommonJavaType.annotation_Service_name);
		
		impl.addImportedType(CommonJavaType.annotation_Service);

		FullyQualifiedJavaType service =CommonJavaType.getService(shortName);

		
		impl.addImportedType(CommonJavaType.page);
		impl.addImportedType(CommonJavaType.getPojo(shortName));
		impl.addImportedType(CommonJavaType.request);
		impl.addImportedType(CommonJavaType.ajax);
		impl.addImportedType(CommonJavaType.myLocaleResolver);
		impl.addImportedType(CommonJavaType.stringUtils);
		impl.addImportedType(service);
		impl.addImportedType(CommonJavaType.getMapper(shortName));
		impl.addImportedType(CommonJavaType.Autowired);
		
		
		impl.addImportedType(CommonJavaType.getBaseService(shortName));
		
		impl.setSuperClass(CommonJavaType.getBaseService(shortName));

		// 继承
		impl.addSuperInterface(service);

		Field myesolver = new Field(CommonJavaType.myLocaleResolver_name, CommonJavaType.myLocaleResolver);
		myesolver.setVisibility(JavaVisibility.PRIVATE);
		myesolver.addAnnotation(CommonJavaType.Autowired_name);
		impl.addField(myesolver);

		// 暂停
		createdataImpl(shortName, service.getShortName(), impl);

		// 创建view 的实现方法
		createViewImpl(shortName, service.getShortName(), impl);

		// 创建get的实现方法
		createGetImpl(shortName, service.getShortName(), impl);

		// 创建delete的实现方法
		createDeleteImpl(shortName, service.getShortName(), impl);

		// 创建saveorUpdate方法
		createSaveOrUpdate(shortName, service.getShortName(), impl);

		GeneratedJavaFile fimpl = new GeneratedJavaFile(impl, PropertiesUtils.get("targetProject"), javaFormatter);
		mapperJavaFiles.add(fimpl);
	}
	
	/**
	 * 创建data实现方法
	 */
	public static void createdataImpl(String shortName, String serviceName, TopLevelClass impl) {

		Method method = new Method("data");

		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation(CommonJavaType.override);

		method.setReturnType(CommonJavaType.page);

		method.addParameter(CommonJavaType.getPojoParameter(shortName));
		
		method.addParameter(CommonJavaType.getRequestParameter());

		method.addBodyLine("return new Page(findPage(" + StringUtils.tolow(shortName) + ",request" + "));");

		impl.addMethod(method);
	}
	
	/**
	 * 创建view的实现方法
	 * 
	 * @param shortName
	 * @param serviceName
	 * @param impl
	 */
	public static void createViewImpl(String shortName, String serviceName, TopLevelClass impl) {

		Method method = new Method("view");

		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation(CommonJavaType.override);

		method.setReturnType(CommonJavaType.ajax);

		method.addParameter(CommonJavaType.getIdParameter());

		method.addBodyLine("AjaxJson j=new AjaxJson();");
		method.addBodyLine("j.setSuccess(false);");

		method.addBodyLine("try {");
		method.addBodyLine("if(StringUtils.isNotBlank(id)){");
		method.addBodyLine("j.setSuccess(true);");
		method.addBodyLine("j.put(\"" + StringUtils.tolow(shortName) + "\", get(id));");
		method.addBodyLine("j.setMsg(\"OK\");");
		method.addBodyLine("}else{");
		method.addBodyLine("j.setMsg(myLocaleResolver.getMessage(\"obj.notFound\"));");
		method.addBodyLine("}");
		method.addBodyLine("} catch (Exception e) {");

		method.addBodyLine("e.printStackTrace();");
		method.addBodyLine("j.setMsg(myLocaleResolver.getMessage(\"sys.err.waiting\"));");
		method.addBodyLine("}");
		method.addBodyLine("return j;");
		impl.addMethod(method);
	}
	
	/**
	 * get的实现方法
	 * @param shortName
	 * @param serviceName
	 * @param impl
	 */
	public static void createGetImpl(String shortName, String serviceName, TopLevelClass impl) {

		Method method = new Method("get");

		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation(CommonJavaType.override);


		method.setReturnType(CommonJavaType.getPojo(shortName));

		method.addParameter(CommonJavaType.getIdParameter());

		method.addBodyLine("return mapper.selectByPrimaryKey(id);");

		impl.addMethod(method);
	}
	
	/**
	 * delete的实现方法
	 * 
	 * @param shortName
	 * @param serviceName
	 * @param impl
	 */
	public static void createDeleteImpl(String shortName, String serviceName, TopLevelClass impl) {

		Method method = new Method("delete");

		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation(CommonJavaType.override);


		method.setReturnType(CommonJavaType.ajax);

		method.addParameter(CommonJavaType.getIdParameter());

		method.addBodyLine("AjaxJson j=new AjaxJson();");
		method.addBodyLine("j.setSuccess(false);");

		method.addBodyLine("try {");
		method.addBodyLine("if(StringUtils.isNotBlank(id)){");
		method.addBodyLine("j.setSuccess(true);");

		method.addBodyLine("deleteByLogic(new " + StringUtils.toUp(shortName) + "(id));");

		method.addBodyLine("j.setMsg(\"OK\");");
		method.addBodyLine("}else{");
		method.addBodyLine("j.setMsg(myLocaleResolver.getMessage(\"obj.notFound\"));");
		method.addBodyLine("}");
		method.addBodyLine("} catch (Exception e) {");

		method.addBodyLine("e.printStackTrace();");
		method.addBodyLine("j.setMsg(myLocaleResolver.getMessage(\"sys.err.waiting\"));");
		method.addBodyLine("}");
		method.addBodyLine("return j;");
		impl.addMethod(method);
	}
	
	/**
	 * saveorudapte的实现方法
	 * @param shortName
	 * @param serviceName
	 * @param impl
	 */
	public static void createSaveOrUpdate(String shortName, String serviceName, TopLevelClass impl) {
		
		Method method = new Method("saveOrUpdate");

		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation("@Override");
		

		
		FullyQualifiedJavaType ajax = new FullyQualifiedJavaType(PropertiesUtils.get("ajaxjson"));

		method.setReturnType(ajax);
		FullyQualifiedJavaType pojo = new FullyQualifiedJavaType(
				PropertiesUtils.get("pojo.targetPackage") + "." + StringUtils.toUp(shortName));

		method.addParameter(new Parameter(pojo, StringUtils.tolow(shortName)));
		//method.addParameter(new Parameter(CommonJavaType.request, CommonJavaType.request_name));
		
		method.addBodyLine("AjaxJson j=new AjaxJson();");
		method.addBodyLine("j.setSuccess(false);");

		method.addBodyLine("try {");
		
		method.addBodyLine("save("+StringUtils.tolow(shortName)+");");
		
		method.addBodyLine("j.put(\"" + StringUtils.tolow(shortName) + "\", "+StringUtils.tolow(shortName)+");");
		
		
		method.addBodyLine("j.setSuccess(true);");
		method.addBodyLine("j.setMsg(\"OK\");");
		
		
		method.addBodyLine("} catch (Exception e) {");

		method.addBodyLine("e.printStackTrace();");
		method.addBodyLine("j.setMsg(myLocaleResolver.getMessage(\"sys.err.waiting\"));");
		method.addBodyLine("}");
		method.addBodyLine("return j;");
		
		
		impl.addMethod(method);
	}
	
	
	
}
