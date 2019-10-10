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
		
		impl.addAnnotation("@Transactional(readOnly=true)");
		
		impl.addImportedType(CommonJavaType.annotation_Service);

		FullyQualifiedJavaType service =CommonJavaType.getService(shortName);

		impl.addImportedType(CommonJavaType.transactional);
		impl.addImportedType(CommonJavaType.page);
		impl.addImportedType(CommonJavaType.getPojo(shortName));
		impl.addImportedType(CommonJavaType.request);
		impl.addImportedType(CommonJavaType.ajax);
//		impl.addImportedType(CommonJavaType.myLocaleResolver);
		impl.addImportedType(CommonJavaType.stringUtils);
		impl.addImportedType(service);
		impl.addImportedType(CommonJavaType.getMapper(shortName));
		impl.addImportedType(CommonJavaType.Autowired);
		
		impl.addImportedType(CommonJavaType.list);
		
		impl.addImportedType(CommonJavaType.logger);
		impl.addImportedType(CommonJavaType.loggerFactory);
		
		impl.addImportedType(CommonJavaType.webErrorNotice);
		
		impl.addImportedType(CommonJavaType.getBaseService(shortName));
		
		impl.setSuperClass(CommonJavaType.getBaseService(shortName));

		// 继承
		impl.addSuperInterface(service);

		Field logger = new Field(CommonJavaType.logger_name, CommonJavaType.logger);
		logger.setInitializationString("LoggerFactory.getLogger(getClass())");
		impl.addField(logger);

		// 暂停
		createdataImpl(shortName, service.getShortName(), impl);
		
		//创建getList 的实现方法
		createGetListImpl(shortName, service.getShortName(), impl);
		

		// 创建view 的实现方法
		createViewImpl(shortName, service.getShortName(), impl);

		// 创建get的实现方法
		createGetImpl(shortName, service.getShortName(), impl);

		// 创建delete的实现方法
		createDeleteImpl(shortName, service.getShortName(), impl);
		
		// 创建findList的实现方法
		createfindList(shortName, service.getShortName(), impl);
		

		// 创建saveorUpdate方法
		createSaveOrUpdate(shortName, service.getShortName(), impl);
		
		//创建save方法
		createSave(shortName, service.getShortName(), impl);
		
		//创建delete 方法
		createDelete(shortName, service.getShortName(), impl);

		GeneratedJavaFile fimpl = new GeneratedJavaFile(impl, PropertiesUtils.get("targetProject"), javaFormatter);
		mapperJavaFiles.add(fimpl);
	}
	
	private static void createGetListImpl(String shortName, String shortName2, TopLevelClass impl) {
		Method method = new Method("getList");

		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation(CommonJavaType.override);

		method.setReturnType(CommonJavaType.ajax);

		method.addParameter(CommonJavaType.getPojoParameter(shortName));
		
		method.addParameter(CommonJavaType.getRequestParameter());

		
		
		method.addBodyLine("AjaxJson j=new AjaxJson();");
		method.addBodyLine("j.setSuccess(true);");
		method.addBodyLine("j.put( \"pageInfo\", super.findPage(" +
		StringUtils.tolow(shortName) + ",request" + "));");
		method.addBodyLine("return j;");
		impl.addMethod(method);
		
	}

	private static void createDelete(String shortName, String shortName2, TopLevelClass impl) {
		Method method = new Method("deleteByLogic");

		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation(CommonJavaType.override);
		method.addParameter(CommonJavaType.getIdParameter());
		method.addAnnotation("@Transactional(readOnly=false)");
		
		method.addBodyLine("  super.deleteByLogic( new "+StringUtils.toUp(shortName)+"(id)"+");");
		
		impl.addMethod(method);
	}

	/**
	 * 保存方法
	 * @param shortName
	 * @param shortName2
	 * @param impl
	 */
	private static void createSave(String shortName, String shortName2, TopLevelClass impl) {
		Method method = new Method("save");

		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation(CommonJavaType.override);
		method.addParameter(CommonJavaType.getPojoParameter(shortName));
		method.addAnnotation("@Transactional(readOnly=false)");
		method.setReturnType(new FullyQualifiedJavaType("int"));
		
		method.addBodyLine(" return super.save("+StringUtils.tolow(shortName)+");");
		
		impl.addMethod(method);
	}

	/**
	 * 创建findList方法
	 * @param shortName
	 * @param shortName2
	 * @param impl
	 */
	private static void createfindList(String shortName, String shortName2, TopLevelClass impl) {
		Method method = new Method("findList");

		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation(CommonJavaType.override);
		FullyQualifiedJavaType list=new FullyQualifiedJavaType("java.util.List");
		list.addTypeArgument(CommonJavaType.getPojo(shortName));
		method.setReturnType(list);
		method.addParameter(CommonJavaType.getPojoParameter(shortName));
		
		method.addBodyLine(" return mapper.findList("+StringUtils.tolow(shortName)+");");
		
		impl.addMethod(method);
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
		method.addBodyLine("j.setMsg(WebErrorNotice.SYS_OK);");
		method.addBodyLine("}else{");
		method.addBodyLine("j.setMsg(WebErrorNotice.OBJ_ArgsErr);");
		method.addBodyLine("}");
		method.addBodyLine("} catch (Exception e) {");

		method.addBodyLine("logger.error(WebErrorNotice.SYS_ErrWait.getStringCode(), e);");
		method.addBodyLine("j.setMsg(WebErrorNotice.SYS_ErrWait);");
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

		method.addAnnotation("@Transactional(readOnly=false)");
		method.setReturnType(CommonJavaType.ajax);

		method.addParameter(CommonJavaType.getIdParameter());

		method.addBodyLine("AjaxJson j=new AjaxJson();");
		method.addBodyLine("j.setSuccess(false);");

		method.addBodyLine("try {");
		method.addBodyLine("if(StringUtils.isNotBlank(id)){");
		method.addBodyLine("j.setSuccess(true);");

		method.addBodyLine("deleteByLogic(new " + StringUtils.toUp(shortName) + "(id));");

		method.addBodyLine("j.setMsg(WebErrorNotice.SYS_OK);");
		method.addBodyLine("}else{");
		method.addBodyLine("j.setMsg(WebErrorNotice.OBJ_ArgsErr);");
		method.addBodyLine("}");
		method.addBodyLine("} catch (Exception e) {");

		method.addBodyLine("logger.error(WebErrorNotice.SYS_ErrWait.getStringCode(), e);");
		method.addBodyLine("j.setMsg(WebErrorNotice.SYS_ErrWait);");
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
		

		method.addAnnotation("@Transactional(readOnly=false)");
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
		method.addBodyLine("j.setMsg(WebErrorNotice.SYS_OK);");
		
		
		method.addBodyLine("} catch (Exception e) {");

		method.addBodyLine("logger.error(WebErrorNotice.SYS_ErrWait.getStringCode(), e);");
		method.addBodyLine("j.setMsg(WebErrorNotice.SYS_ErrWait);");
		method.addBodyLine("}");
		method.addBodyLine("return j;");
		
		
		impl.addMethod(method);
	}
	
	
	
}
