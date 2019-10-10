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

public class CreateController {

	// 创建controller
	public static void createController(String shortName, List<GeneratedJavaFile> mapperJavaFiles,
			JavaFormatter javaFormatter) {
		TopLevelClass controller = new TopLevelClass(CommonJavaType.getController(shortName));
		controller.setSuperClass(CommonJavaType.baseController);
		controller.addImportedType(CommonJavaType.baseController);

		controller.getType().getPackageName();

		controller.setVisibility(JavaVisibility.PUBLIC);
		controller.addAnnotation(CommonJavaType.annotation_controller_name);
		controller.addImportedType(CommonJavaType.controller);

		String requestMapper = PropertiesUtils.get("controller.requestMapper");
		if (StringUtils.isBlank(requestMapper)) {
			controller.addAnnotation("@RequestMapping(\"/" + StringUtils.tolow(shortName) + "\")");
		} else {
			controller.addAnnotation("@RequestMapping(\"/" + requestMapper + "\")");
		}

		controller.addImportedType(CommonJavaType.requestMapping);

		String shiro = PropertiesUtils.get("shiro");

		String swagger = PropertiesUtils.get("swagger2");

		// 导入get ，post request包
		controller.addImportedType(CommonJavaType.getMapping);
		controller.addImportedType(CommonJavaType.postMapping);
		controller.addImportedType(CommonJavaType.responseBody);

		controller.addImportedType(CommonJavaType.Autowired);
		controller.addImportedType(CommonJavaType.validated);
		controller.addImportedType(CommonJavaType.ajax);

		controller.addImportedType(CommonJavaType.request);
		// 准备service
		Field field = new Field(StringUtils.tolow(shortName) + "Service", CommonJavaType.getService(shortName));
		field.addAnnotation(CommonJavaType.Autowired_name);
		field.setVisibility(JavaVisibility.PRIVATE);
		controller.addField(field);

		// 导入service包
		controller.addImportedType(CommonJavaType.getService(shortName));

		if ("0".equals(shiro)) {
			controller.addImportedType(CommonJavaType.requiresPermissions);
		}
		if ("0".equals(swagger)) {
			controller.addImportedType(CommonJavaType.apiImplicitParam);
			controller.addImportedType(CommonJavaType.apiImplicitParams);
			controller.addImportedType(CommonJavaType.apiOperation);
		}
		// view 方法
		createViewmethod(shortName, shiro, swagger, controller);

		// delete 方法
		createDeletemethod(shortName, shiro, swagger, controller);

		// data方法
		createDatamethod(shortName, shiro, swagger, controller);

		// list方法
		createListMethod(shortName, shiro, swagger, controller);

		// save or update
		createSaveOrUpdatemethod(shortName, shiro, swagger, controller);

		// 导入实体包
		controller.addImportedType(CommonJavaType.getPojo(shortName));

		GeneratedJavaFile fcontroller = new GeneratedJavaFile(controller, PropertiesUtils.get("targetProject"),
				javaFormatter);
		mapperJavaFiles.add(fcontroller);
	}

	/**
	 * 创建list 方法
	 * @param shortName
	 * @param shiro
	 * @param swagger
	 * @param controller
	 */
	public static void createListMethod(String shortName, String shiro, String swagger, TopLevelClass controller) {
		Method viewMethod = new Method("getList");

		if ("0".equals(shiro)) {
			viewMethod.addAnnotation("@RequiresPermissions(\"" + shortName + ":list" + "\")");
		}
		if ("0".equals(swagger)) {
			viewMethod.addAnnotation(
					"@ApiOperation(value=\"分页获取" + shortName + "数据\",notes=\"分页查询" + shortName + " 数据\")");
			viewMethod.addAnnotation("@ApiImplicitParams({"
					+ "@ApiImplicitParam(name = \"pageNum\", value = \"pageNum\", dataType = \"int\", required = false, defaultValue = \"1\", paramType = \"query\"),"
					+ StringUtils.getNewLine()
					+ "@ApiImplicitParam(name = \"pageSize\", value = \"pageSize\", dataType = \"int\", required = false, defaultValue = \"10\", paramType = \"query\"),"
					+ StringUtils.getNewLine() + "})");
		}
		viewMethod.addAnnotation("@PostMapping(\"/list\")");
		viewMethod.addAnnotation("@ResponseBody");

		viewMethod.addParameter(CommonJavaType.getRequestParameter());

		Parameter pojo = CommonJavaType.getPojoParameter(shortName);
		pojo.addAnnotation("@RequestBody");
		viewMethod.addParameter(pojo);

		viewMethod.setReturnType(CommonJavaType.ajax);
//		controller.addImportedType(CommonJavaType.page);
		viewMethod.setVisibility(JavaVisibility.PUBLIC);
		viewMethod.addBodyLine("return " + StringUtils.tolow(shortName) + "Service.getList(" + StringUtils.tolow(shortName)
				+ ",request);");
		controller.addMethod(viewMethod);

	}

	/**
	 * controller 创建view 方法
	 * 
	 * @param shortName
	 * @param shiro
	 * @param swagger
	 * @param controller
	 */
	public static void createViewmethod(String shortName, String shiro, String swagger, TopLevelClass controller) {
		Method viewMethod = new Method("view");

		if ("0".equals(shiro)) {
			viewMethod.addAnnotation("@RequiresPermissions(\"" + shortName + ":view" + "\")");
		}
		if ("0".equals(swagger)) {
			viewMethod.addAnnotation("@ApiOperation(value=\"查询" + shortName + "\",notes=\"根据id查询" + shortName + "\")");
			viewMethod.addAnnotation(
					"@ApiImplicitParams({@ApiImplicitParam(name = \"id\", value = \"id\", dataType = \"String\", required = true,  paramType = \"query\")})");
		}

		viewMethod.addAnnotation("@GetMapping(\"/view\")");
		viewMethod.addAnnotation("@ResponseBody");

		viewMethod.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
		viewMethod.setReturnType(new FullyQualifiedJavaType(PropertiesUtils.get("ajaxjson")));
		viewMethod.setVisibility(JavaVisibility.PUBLIC);
		viewMethod.addBodyLine("return " + StringUtils.tolow(shortName) + "Service.view(id);");
		controller.addMethod(viewMethod);
	}

	/**
	 * 删除方法
	 * 
	 * @param shortName
	 * @param shiro
	 * @param swagger
	 * @param controller
	 */
	public static void createDeletemethod(String shortName, String shiro, String swagger, TopLevelClass controller) {
		Method viewMethod = new Method("delete");

		if ("0".equals(shiro)) {
			viewMethod.addAnnotation("@RequiresPermissions(\"" + shortName + ":del" + "\")");
		}
		if ("0".equals(swagger)) {
			viewMethod.addAnnotation("@ApiOperation(value=\"删除" + shortName + "\",notes=\"根据id删除" + shortName + "\")");
			viewMethod.addAnnotation(
					"@ApiImplicitParams({@ApiImplicitParam(name = \"id\", value = \"id\", dataType = \"String\", required = true,  paramType = \"query\")})");
		}

		viewMethod.addAnnotation("@GetMapping(\"/delete\")");
		viewMethod.addAnnotation("@ResponseBody");

		viewMethod.addParameter(CommonJavaType.getIdParameter());
		viewMethod.setReturnType(CommonJavaType.ajax);
		viewMethod.setVisibility(JavaVisibility.PUBLIC);
		viewMethod.addBodyLine("return " + StringUtils.tolow(shortName) + "Service.delete(id);");
		controller.addMethod(viewMethod);
	}

	/**
	 * 分页方法
	 * 
	 * @param shortName
	 * @param shiro
	 * @param swagger
	 * @param controller
	 */
	public static void createDatamethod(String shortName, String shiro, String swagger, TopLevelClass controller) {
		Method viewMethod = new Method("getData");

		if ("0".equals(shiro)) {
			viewMethod.addAnnotation("@RequiresPermissions(\"" + shortName + ":list" + "\")");
		}
		if ("0".equals(swagger)) {
			viewMethod.addAnnotation(
					"@ApiOperation(value=\"分页获取" + shortName + "数据\",notes=\"分页查询" + shortName + " 数据\")");
			viewMethod.addAnnotation("@ApiImplicitParams({"
					+ "@ApiImplicitParam(name = \"pageNum\", value = \"pageNum\", dataType = \"int\", required = false, defaultValue = \"1\", paramType = \"query\"),"
					+ StringUtils.getNewLine()
					+ "@ApiImplicitParam(name = \"pageSize\", value = \"pageSize\", dataType = \"int\", required = false, defaultValue = \"10\", paramType = \"query\"),"
					+ StringUtils.getNewLine() + "})");
		}
		viewMethod.addAnnotation("@PostMapping(\"/data\")");
		viewMethod.addAnnotation("@ResponseBody");

		viewMethod.addParameter(CommonJavaType.getRequestParameter());

		Parameter pojo = CommonJavaType.getPojoParameter(shortName);
		pojo.addAnnotation("@RequestBody");
		viewMethod.addParameter(pojo);

		viewMethod.setReturnType(CommonJavaType.page);
		controller.addImportedType(CommonJavaType.page);
		viewMethod.setVisibility(JavaVisibility.PUBLIC);
		viewMethod.addBodyLine("return " + StringUtils.tolow(shortName) + "Service.data(" + StringUtils.tolow(shortName)
				+ ",request);");
		controller.addMethod(viewMethod);
	}

	/**
	 * save or update controller
	 * 
	 * @param shortName
	 * @param shiro
	 * @param swagger
	 * @param controller
	 */
	public static void createSaveOrUpdatemethod(String shortName, String shiro, String swagger,
			TopLevelClass controller) {
		Method viewMethod = new Method("saveOrUpdate");

		if ("0".equals(shiro)) {
			viewMethod.addAnnotation("@RequiresPermissions(\"" + shortName + ":edit" + "\")");
		}
		if ("0".equals(swagger)) {
			viewMethod.addAnnotation(
					"@ApiOperation(value=\"保存或修改" + shortName + "数据\",notes=\"添加|保存" + shortName + " 数据\")");

		}
		viewMethod.addAnnotation("@PostMapping(\"/saveOrUpdate\")");
		viewMethod.addAnnotation("@ResponseBody");

		Parameter parameter = new Parameter(new FullyQualifiedJavaType(shortName), StringUtils.tolow(shortName));

		parameter.addAnnotation("@RequestBody");

		controller.addImportedType(CommonJavaType.requestBody);

		parameter.addAnnotation("@Validated");

		viewMethod.addParameter(parameter);
		controller.addImportedType(CommonJavaType.validated);

		viewMethod.setReturnType(new FullyQualifiedJavaType(PropertiesUtils.get("ajaxjson")));

		viewMethod.setVisibility(JavaVisibility.PUBLIC);
		viewMethod.addBodyLine("return " + StringUtils.tolow(shortName) + "Service.saveOrUpdate("
				+ StringUtils.tolow(shortName) + ");");
		controller.addMethod(viewMethod);
	}
}
