package com.code.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.code.utils.AjaxJson;
import com.code.utils.GetTableInfo;
import com.code.utils.MapperPlugin;
import com.code.utils.PropertiesUtils;

@Controller
public class BaseController {


	@Autowired
	private GetTableInfo getTableInfo;
	
	@RequestMapping("/test")
	@ResponseBody
	public AjaxJson test(String data){
		AjaxJson j=new AjaxJson();
		
		j.put("msg", data);
		
		return j;
	}
	

	/**
	 * 生成代码
	 * 
	 * @param value
	 * @return
	 */
	@RequestMapping("/generate")
	@ResponseBody
	public Map<String, Object> generate(String targetProject, String pojoTargetPackage, String pojoRootClass,
			String pojoenumPojoEnumPageName, String exampleBaseExampleClass, String xmlTargetPackage,
			String mapperTargetPackage, String extendMapperPackageName, String table, String copyrightDesc,
			String tables, String shiro, String swagger2, String controllerTargetPackage, String serviceTargetPackage,
			String controllerRequestMapper) {
		Map<String, Object> map = new HashMap<>();
		boolean success = false;
		String msg = "生成异常";
		try {

			setProperties(targetProject, pojoTargetPackage, pojoRootClass, pojoenumPojoEnumPageName,
					exampleBaseExampleClass, xmlTargetPackage, mapperTargetPackage, extendMapperPackageName, table,
					copyrightDesc, tables, shiro, swagger2, controllerTargetPackage, serviceTargetPackage,
					controllerRequestMapper);

			// 生成临时文件
			PropertiesUtils.save();
			// //生成代码
			MapperPlugin.generate();
			// //删除临时文件
			PropertiesUtils.delete();
			msg = "OK";
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("success", success);
		map.put("msg", msg);
		return map;
	}

	@GetMapping("/getProperties")
	@ResponseBody
	public Properties getProperties() {
		return PropertiesUtils.prop;
	}

	/**
	 * 设置值
	 * 
	 * @param targetProject
	 * @param pojoTargetPackage
	 * @param pojoRootClass
	 * @param pojoenumPojoEnumPageName
	 * @param exampleBaseExampleClass
	 * @param xmlTargetPackage
	 * @param mapperTargetPackage
	 * @param extendMapperPackageName
	 * @param table
	 * @param copyrightDesc
	 * @param controllerTargetPackage
	 */
	private void setProperties(String targetProject, String pojoTargetPackage, String pojoRootClass,
			String pojoenumPojoEnumPageName, String exampleBaseExampleClass, String xmlTargetPackage,
			String mapperTargetPackage, String extendMapperPackageName, String table, String copyrightDesc,
			String tables, String shiro, String swagger2, String controllerTargetPackage, String serviceTargetPackage,
			String controllerRequestMapper) {

		if (!StringUtils.isEmpty(targetProject)) {
			PropertiesUtils.setValue("targetProject", targetProject);

			// 目录不存在时代码不会生成
			try {
				File f = new File(targetProject);
				if (!f.exists()) {
					f.mkdirs();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(pojoTargetPackage)) {
			PropertiesUtils.setValue("pojo.targetPackage", pojoTargetPackage);
		}
		if (!StringUtils.isEmpty(pojoRootClass)) {
			PropertiesUtils.setValue("pojo.rootClass", pojoRootClass);
		}
		if (!StringUtils.isEmpty(pojoenumPojoEnumPageName)) {
			PropertiesUtils.setValue("pojoenum.pojoEnumPageName", pojoenumPojoEnumPageName);
		}
		if (!StringUtils.isEmpty(exampleBaseExampleClass)) {
			PropertiesUtils.setValue("example.baseExampleClass", exampleBaseExampleClass);
		}
		if (!StringUtils.isEmpty(xmlTargetPackage)) {
			PropertiesUtils.setValue("xml.targetPackage", xmlTargetPackage);
		}
		if (!StringUtils.isEmpty(mapperTargetPackage)) {
			PropertiesUtils.setValue("mapper.targetPackage", mapperTargetPackage);
		}
		if (!StringUtils.isEmpty(extendMapperPackageName)) {
			PropertiesUtils.setValue("mapper.extendMapperPackageName", extendMapperPackageName);
		}

		if (!StringUtils.isEmpty(table)) {
			PropertiesUtils.setValue("table", table);
		}

		if (!StringUtils.isEmpty(copyrightDesc)) {
			PropertiesUtils.setValue("copyright.desc", copyrightDesc);
		}

		if (!StringUtils.isEmpty(tables)) {
			PropertiesUtils.setValue("tables", tables);
		}

		if (!StringUtils.isEmpty(shiro)) {
			PropertiesUtils.setValue("shiro", shiro);
		}

		if (!StringUtils.isEmpty(shiro)) {
			PropertiesUtils.setValue("swagger2", swagger2);
		}

		if (!StringUtils.isEmpty(controllerTargetPackage)) {
			PropertiesUtils.setValue("controller.targetPackage", controllerTargetPackage);
		}

		if (!StringUtils.isEmpty(serviceTargetPackage)) {
			PropertiesUtils.setValue("service.targetPackage", serviceTargetPackage);
		}

		if (!StringUtils.isEmpty(controllerRequestMapper)) {
			PropertiesUtils.setValue("controller.requestMapper", controllerRequestMapper);
		}
	}


	@GetMapping("/getTables")
	@ResponseBody
	public List<String> getTables() throws Exception {

		return getTableInfo.printTableNames();
	}

	@GetMapping("/getTableDetail")
	@ResponseBody
	public List<Map<String, Object>> getTableDetail(String tableName) throws Exception {

		return getTableInfo.printColumnInfo(tableName);
	}

}
