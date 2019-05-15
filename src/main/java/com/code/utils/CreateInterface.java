package com.code.utils;

import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

public class CreateInterface {

	/**
	 * 创建 service 接口层
	 * 
	 * @param shortName
	 * @param mapperJavaFiles
	 * @param javaFormatter
	 */
	public static void createInterface(String shortName, List<GeneratedJavaFile> mapperJavaFiles,
			JavaFormatter javaFormatter) {

		Interface anInterface = new Interface(CommonJavaType.getService(shortName));

		anInterface.setVisibility(JavaVisibility.PUBLIC);

		// data接口
		Method data = new Method("data");



		anInterface.addImportedType(CommonJavaType.page);
		anInterface.addImportedType(CommonJavaType.getPojo(shortName));
		anInterface.addImportedType(CommonJavaType.request);
		anInterface.addImportedType(CommonJavaType.ajax);
		anInterface.addImportedType(CommonJavaType.list);

		data.addParameter(CommonJavaType.getPojoParameter(shortName));
		data.addParameter(CommonJavaType.getRequestParameter());

		data.setReturnType(CommonJavaType.page);
		data.addJavaDocLine("/**");
		data.addJavaDocLine(" * @param " + StringUtils.tolow(shortName) );
		data.addJavaDocLine(" * @param " + "request" );
		data.addJavaDocLine(" */");
		
		anInterface.addMethod(data);
		
		
		// get方法

				Method get = new Method("get");
				get.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
				get.setReturnType(CommonJavaType.getPojo(shortName));

				get.addJavaDocLine("/**");
				get.addJavaDocLine(" * @param " + "id" );
				get.addJavaDocLine(" */");
				anInterface.addMethod(get);
		

		// view方法
		Method view = new Method("view");
		view.addParameter(CommonJavaType.getIdParameter());
		view.setReturnType(CommonJavaType.ajax);
		view.setVisibility(JavaVisibility.PUBLIC);
		view.addJavaDocLine("/**");
		view.addJavaDocLine(" * @param " + "id" );
		view.addJavaDocLine(" */");
		anInterface.addMethod(view);

		
		//findList方法
		Method findList=new Method("findList");
		findList.addParameter(CommonJavaType.getPojoParameter(shortName));
		findList.setVisibility(JavaVisibility.PUBLIC);
		FullyQualifiedJavaType list=new FullyQualifiedJavaType("java.util.List");
		list.addTypeArgument(CommonJavaType.getPojo(shortName));
		
		findList.setReturnType(list);
		findList.addJavaDocLine("/**");
		findList.addJavaDocLine(" * @param " + StringUtils.tolow(shortName) );
		findList.addJavaDocLine(" */");
		anInterface.addMethod(findList);
		

		
		// delete方法
		Method delete = new Method("delete");
		delete.addParameter(CommonJavaType.getIdParameter());
		delete.setReturnType(CommonJavaType.ajax);

		delete.addJavaDocLine("/**");
		delete.addJavaDocLine(" * @param " + "id" );
		delete.addJavaDocLine(" */");
		delete.setVisibility(JavaVisibility.PUBLIC);
		anInterface.addMethod(delete);
		
		//创建 deleteByLogic
		
		Method deleteByLogic = new Method("deleteByLogic");
		deleteByLogic.addParameter(CommonJavaType.getIdParameter());

		deleteByLogic.addJavaDocLine("/**");
		deleteByLogic.addJavaDocLine(" * @param " + "id" );
		deleteByLogic.addJavaDocLine(" */");
		deleteByLogic.setVisibility(JavaVisibility.PUBLIC);
		anInterface.addMethod(deleteByLogic);
		

		// saveorupdate
		Method saveorupdate = new Method("saveOrUpdate");
		saveorupdate.addParameter(CommonJavaType.getPojoParameter(shortName));
		saveorupdate.setReturnType(CommonJavaType.ajax);

		saveorupdate.setVisibility(JavaVisibility.PUBLIC);
		saveorupdate.addJavaDocLine("/**");
		saveorupdate.addJavaDocLine(" * @param " +  StringUtils.tolow(shortName));
		saveorupdate.addJavaDocLine(" */");
		anInterface.addMethod(saveorupdate);
		
		//save方法
		Method save=new Method("save");
		save.setVisibility(JavaVisibility.PUBLIC);
		save.setReturnType(new FullyQualifiedJavaType("int"));
		save.addParameter(CommonJavaType.getPojoParameter(shortName));
		save.addJavaDocLine("/**");
		save.addJavaDocLine(" *  save | update ");
		save.addJavaDocLine(" * @param " +  StringUtils.tolow(shortName));
		save.addJavaDocLine(" */");
		anInterface.addMethod(save);
		

		GeneratedJavaFile fanInterface = new GeneratedJavaFile(anInterface, PropertiesUtils.get("targetProject"), javaFormatter);
		mapperJavaFiles.add(fanInterface);
		

		
	}
	
}
