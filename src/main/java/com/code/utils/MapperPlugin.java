package com.code.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.java.TopLevelEnumeration;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;


public class MapperPlugin extends PluginAdapter {


    private ShellCallback shellCallback = null;

    public MapperPlugin() {
        shellCallback = new DefaultShellCallback(false);
    }

    /**
     * 验证参数是否有效
     * @param warnings
     * @return
     */
    public boolean validate(List<String> warnings) {
        
        return true;
    }

    public static void generate() {
    	try {
    		File  configFile = new File("src/main/resources/generatorConfig.xml");
       	 List<String> warnings = new ArrayList<String>();
            boolean overwrite = true;
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	/**
	 * xml 重新或新增
	 */
	@Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		XmlElement element=document.getRootElement();
		XmlElement rootel=new XmlElement(element.getName());
		List<Element> els=element.getElements();
		List<Element> newElement=new ArrayList<>();
		for(Attribute attr:element.getAttributes()){
			rootel.addAttribute(attr);
		}
		for(Element el:els){
			
			
//			//获取xml字符串
			String xml=el.getFormattedContent(0);
//			
			System.out.println(xml);
			//排除<sql
			int sql_index=xml.indexOf("<sql");
//			
			if(sql_index>=0){
				rootel.addElement(el);
				continue;
			}
			int select_index=xml.indexOf("<select");
			int update_index=xml.indexOf("<update");
			int delete_index=xml.indexOf("<delete");
			if(select_index>=0||update_index>=0||delete_index>=0){
				Map<String, Object> map=Xmlutls.xmlToMap(xml);
				try {
					
					//修改参数中example
					String id=(String) map.get("id");
					String parameterType=map.get("parameterType").toString();
					
					//替换查询父类
					if(parameterType.endsWith("Example")){
						parameterType=PropertiesUtils.get("example.baseExampleClass");
					}
					
					String root=map.get("root").toString();
					String resultType="";//插入和更新可能没有
					if(map.containsKey("resultType")){
						resultType=map.get("resultType").toString();
					}
					
					String resultMap="";
					if(map.containsKey("resultMap")){
						resultMap=map.get("resultMap").toString();
					}
					
					String content=map.get("content").toString();
					
					//创建新的节点
					XmlElement xmlel = new XmlElement(root);
					//id
					xmlel.addAttribute(new Attribute("id", id));
					//参数
					xmlel.addAttribute(new Attribute("parameterType", parameterType));
					//返回值
					if(resultType!=null&&resultType!=""){
						xmlel.addAttribute(new Attribute("resultType", resultType));
					}

					if(resultMap!=null&&resultMap!=""){
						
						xmlel.addAttribute(new Attribute("resultMap", resultMap));
					}
					
					//content
					xmlel.addElement(new TextElement(content));
					newElement.add(xmlel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				rootel.addElement(el);
			}
		}
		
		//添加
		for(Element el:newElement){
			rootel.addElement(el);;
		}
		
		document.setRootElement(rootel);
		//扩展逻辑删除方法
		addDeleteMethod(document, introspectedTable);
		
		//宽展额外查询方法
		addFindListMethod(document, introspectedTable);
		
		return super.sqlMapDocumentGenerated(document, introspectedTable);
    }
	
	/**
	 * 新增逻辑删除代码
	 * @param document
	 * @param introspectedTable
	 */
	public void addDeleteMethod(Document document,IntrospectedTable introspectedTable){
		 XmlElement update = new XmlElement("update");
	        update.addAttribute(new Attribute("id", "deleteBylogic"));
//	        update.addAttribute(new Attribute("resultType", "java.lang.Integer"));//mybatis insert ,update 没有返回值
	        update.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
	        update.addElement(new TextElement(" update  "+ introspectedTable.getFullyQualifiedTableNameAtRuntime()+" set del_flag=#{del_delFlag},update_date = now(),update_by=#{updateBy} where id =#{id}"));
	        XmlElement parentElement = document.getRootElement();
	        parentElement.addElement(update);
	}
	
	/**
	 * 创建额外查询方法
	 * @param document
	 * @param introspectedTable
	 */
	public void addFindListMethod(Document document,IntrospectedTable introspectedTable){
		
		XmlElement select = new XmlElement("select");
		select.addAttribute(new Attribute("id", "findList"));
		select.addAttribute(new Attribute("resultType", introspectedTable.getBaseRecordType()));//mybatis insert ,update 没有返回值
		select.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
		StringBuffer sb=new StringBuffer();
		String tables_json=PropertiesUtils.get("tables");
    	List<Table> tables=	JsonUtils.jsonToList(tables_json, Table.class);
		sb.append(" select ");
		int i=0;
		boolean hasDelFag=false;
		String newline=System.getProperty("line.separator");
		for(Table table:tables){
			if(i==0){
				sb.append(" a."+table.getColumnName()+""+System.getProperty("line.separator"));
			}else{
				sb.append("      ,a."+table.getColumnName()+newline);
			}
			i++;
			if(table.getColumnName().equals("del_flag")){
				hasDelFag=true;
			}
		}
		
		//表
		sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime()+" a "+newline);
		
		//条件
		sb.append("  <where>  "+newline);
		
		if(hasDelFag){
			sb.append("    a.del_flag = #{delFlag} "+newline);
		}
		for(Table table:tables){
			if(!hasDef(table.getColumnName())){
				
				sb.append("   <if test=\""+table.getJavaColumnName()+"!= null and "+table.getJavaColumnName()+"!= ''"+"\">  "+newline);
				
				sb.append("      AND a."+table.getColumnName() +" = #{"+table.getJavaColumnName()+"} "+newline);
				
				sb.append("   </if> "+newline);
			}
		}
		
		
		sb.append("  </where>");
		
		select.addElement(new TextElement(sb.toString()));
        XmlElement parentElement = document.getRootElement();
        parentElement.addElement(select);
	}
	

	/**
	 * java文件附加内容
	 */
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        JavaFormatter javaFormatter = context.getJavaFormatter();
        List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();
        for (GeneratedJavaFile javaFile : introspectedTable.getGeneratedJavaFiles()) {
            CompilationUnit unit = javaFile.getCompilationUnit();
            FullyQualifiedJavaType baseModelJavaType = unit.getType();

            String shortName = baseModelJavaType.getShortName();

            if (shortName.endsWith("Mapper")) { 
            	String pojoName=shortName.substring(0, shortName.indexOf("Mapper"));
            	
            	 Interface anInterface = (Interface)unit;
            	 
            	 //继承，并导包
            	 Interface newFace=new Interface(anInterface.getType());
            	 newFace.setVisibility(JavaVisibility.PUBLIC);
            	 //添加版权信息
            	 new MyCommentGenerator().addAuth(newFace, PropertiesUtils.get("copyright.desc"));
            	 
            	 //无法屏蔽原有自带方法，需要重新new一个接口获取相关包
            	 for(FullyQualifiedJavaType impor: anInterface.getImportedTypes()){
            		 
            		 if(impor.getFullyQualifiedName().indexOf(pojoName)>=0&&!impor.getFullyQualifiedName().endsWith("Example")){
            			 newFace.addImportedType(impor);
            		 }
            	 }
            	 String extendMapperPackageName=PropertiesUtils.get("mapper.extendMapperPackageName");
            	 
            	 newFace.addImportedType(new FullyQualifiedJavaType(extendMapperPackageName));//导出泛型
            	 FullyQualifiedJavaType daoSuperType = new FullyQualifiedJavaType( 
            			 extendMapperPackageName.substring(extendMapperPackageName.lastIndexOf(".")+1, extendMapperPackageName.length()));
            	 //添加泛型
            	 daoSuperType.addTypeArgument(new FullyQualifiedJavaType(pojoName));
            	 
            	 newFace.addSuperInterface(daoSuperType);
            	 mapperJavaFiles.add( new GeneratedJavaFile(newFace, PropertiesUtils.get("targetProject"), javaFormatter));
            	 
            } else if (shortName.endsWith("Example")) { 
            	//废弃example 生成
            	//exampleDesc(unit, mapperJavaFiles, javaFormatter);
            }else{
            	//实体类
            	//序列化
            	resetPojo(unit, mapperJavaFiles, javaFormatter);
            	
            	//生成controller
            	
            	createController(shortName,mapperJavaFiles,javaFormatter);
            	
            	//创建接口
            	createInterface(shortName,mapperJavaFiles,javaFormatter);
            	
            	//创建接口实现类
            	createInterfaceImpl(shortName,mapperJavaFiles,javaFormatter);
            	
            	//创建枚举
            	createTableEnum(shortName, mapperJavaFiles, javaFormatter, introspectedTable);
            }
        }
        return mapperJavaFiles;
    }
    
    /**
     * 创建接口实现类
     * @param shortName
     * @param mapperJavaFiles
     * @param javaFormatter
     */
    public void createInterfaceImpl(String shortName,List<GeneratedJavaFile> mapperJavaFiles,JavaFormatter javaFormatter){
    	TopLevelClass impl=new TopLevelClass(new FullyQualifiedJavaType(PropertiesUtils.get("service.targetPackage")+"."+"Impl."+shortName+"ServiceImpl"));
    	
    	impl.setVisibility(JavaVisibility.PUBLIC);
    	impl.addAnnotation("@Service");
    	impl.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
    	
    	
    	FullyQualifiedJavaType request=  new FullyQualifiedJavaType("javax.servlet.http.HttpServletRequest");
   	  
    	
    	FullyQualifiedJavaType list=  new FullyQualifiedJavaType("java.util.List");
    	
	   	FullyQualifiedJavaType pojo=new FullyQualifiedJavaType(PropertiesUtils.get("pojo.targetPackage")+"."+toUp(shortName));
	   	  
	   	FullyQualifiedJavaType ajax=  new FullyQualifiedJavaType(PropertiesUtils.get("ajaxjson"));
	   	FullyQualifiedJavaType page=  new FullyQualifiedJavaType(PropertiesUtils.get("page"));
	   	  
	   	FullyQualifiedJavaType myLocaleResolver=new FullyQualifiedJavaType(PropertiesUtils.get("MyLocaleResolver"));
	   	  
	   	FullyQualifiedJavaType stringUtils=new FullyQualifiedJavaType(PropertiesUtils.get("StringUtils"));
	   	  
	   	FullyQualifiedJavaType  service=new FullyQualifiedJavaType(PropertiesUtils.get("service.targetPackage")+"."+shortName+"Service");
	   	  
	   	
		FullyQualifiedJavaType  mapper=new FullyQualifiedJavaType(PropertiesUtils.get("mapper.targetPackage")+"."+shortName+"Mapper");
	   	
		FullyQualifiedJavaType pageInfo=new FullyQualifiedJavaType("com.github.pagehelper.PageInfo");
		FullyQualifiedJavaType Autowired=new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired");
	   	impl.addImportedType(page);
	   	impl.addImportedType(pojo);
	   	impl.addImportedType(request);
	   	impl.addImportedType(ajax);
	   	impl.addImportedType(myLocaleResolver);
	   	impl.addImportedType(stringUtils);
	   	impl.addImportedType(service);
	   	impl.addImportedType(mapper);
	   	impl.addImportedType(pageInfo);
	   	impl.addImportedType(Autowired);
	   	impl.addImportedType(list);
	   	
	   	FullyQualifiedJavaType	baseService  =	new FullyQualifiedJavaType(PropertiesUtils.get("baseService"));
	   	
	   	//添加泛型
	   	baseService.addTypeArgument(mapper);
	   	baseService.addTypeArgument(pojo);
	   	
	   	impl.setSuperClass(baseService);
	   	
	   	//继承
	   	impl.addSuperInterface(service);
	   	
	   	Field myesolver=new Field("myLocaleResolver",myLocaleResolver);
	   	myesolver.setVisibility(JavaVisibility.PRIVATE);
	   	myesolver.addAnnotation("@Autowired");
	   	impl.addField(myesolver);
	   	
	   	
	   //暂停
	   	createdataImpl(shortName,service.getShortName(),impl);
	   	
	   	//创建view 的实现方法
	   	createViewImpl(shortName,service.getShortName(),impl);
	   	
	   	//创建get的实现方法
	   	createGetImpl(shortName,service.getShortName(),impl);
	   	
	   	//创建delete的实现方法
		createDeleteImpl(shortName,service.getShortName(),impl);
    	
    	  
  	  GeneratedJavaFile fimpl =  new GeneratedJavaFile(impl, "D:\\ccc", javaFormatter);
  	  mapperJavaFiles.add(fimpl);
    }
    
    /**
     * 创建data实现方法
     */
    public void createdataImpl(String shortName,String serviceName,TopLevelClass impl){
    	
    	Method method=new Method("data");
    	
    	method.setVisibility(JavaVisibility.PUBLIC);
    	method.addAnnotation("@Override");
    	FullyQualifiedJavaType page=  new FullyQualifiedJavaType(PropertiesUtils.get("page"));
    	FullyQualifiedJavaType request=  new FullyQualifiedJavaType("javax.servlet.http.HttpServletRequest");
     	  
	   	FullyQualifiedJavaType pojo=new FullyQualifiedJavaType(PropertiesUtils.get("pojo.targetPackage")+"."+toUp(shortName));
    	method.setReturnType(page);
    	
    	method.addParameter(new Parameter(pojo, tolow(shortName)));
    	method.addParameter(new Parameter(request, "request"));
    	
    	method.addBodyLine("return new Page(new PageInfo<>(findList("+tolow(shortName)+",request"+")));");
    	
    	impl.addMethod(method);
    }
    
    /**
     * 创建view的实现方法
     * @param shortName
     * @param serviceName
     * @param impl
     */
    public void createViewImpl(String shortName,String serviceName,TopLevelClass impl){
    	
    	Method method=new Method("view");
    	
    	method.setVisibility(JavaVisibility.PUBLIC);
    	method.addAnnotation("@Override");
    	FullyQualifiedJavaType ajax=  new FullyQualifiedJavaType(PropertiesUtils.get("ajaxjson"));
     	  
    	method.setReturnType(ajax);
    	
    	method.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
    	
    	method.addBodyLine("AjaxJson j=new AjaxJson();");
    	method.addBodyLine("j.setSuccess(false);");
    	
    	method.addBodyLine("try {");
    	method.addBodyLine("if(StringUtils.isNotBlank(id)){");
    	method.addBodyLine("j.setSuccess(true);");
    	method.addBodyLine("j.put(\""+tolow(shortName)+"\", get(id));");
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
     * delete的实现方法
     * @param shortName
     * @param serviceName
     * @param impl
     */
    public void createDeleteImpl(String shortName,String serviceName,TopLevelClass impl){
    	
    	Method method=new Method("delete");
    	
    	method.setVisibility(JavaVisibility.PUBLIC);
    	method.addAnnotation("@Override");
     	  
    	FullyQualifiedJavaType ajax=  new FullyQualifiedJavaType(PropertiesUtils.get("ajaxjson"));
     	
    	method.setReturnType(ajax);
    	
    	method.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
    	
    	method.addBodyLine("AjaxJson j=new AjaxJson();");
    	method.addBodyLine("j.setSuccess(false);");
    	
    	method.addBodyLine("try {");
    	method.addBodyLine("if(StringUtils.isNotBlank(id)){");
    	method.addBodyLine("j.setSuccess(true);");
    	
    	method.addBodyLine("deleteByLogic(new "+toUp(shortName)+"(id));");
    	
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
public void createGetImpl(String shortName,String serviceName,TopLevelClass impl){
    	
    	Method method=new Method("get");
    	
    	method.setVisibility(JavaVisibility.PUBLIC);
    	method.addAnnotation("@Override");
     	  
    	FullyQualifiedJavaType pojo=new FullyQualifiedJavaType(PropertiesUtils.get("pojo.targetPackage")+"."+toUp(shortName));
    	
    	method.setReturnType(pojo);
    	
    	method.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
    	
    	method.addBodyLine("return mapper.selectByPrimaryKey(id);");
    	
    	impl.addMethod(method);
    }
    
    
    /**
     * 创建 service 接口层
     * @param shortName
     * @param mapperJavaFiles
     * @param javaFormatter
     */
  public void  createInterface(String shortName,List<GeneratedJavaFile> mapperJavaFiles,JavaFormatter javaFormatter){
	  
	  Interface anInterface = new Interface(new FullyQualifiedJavaType(PropertiesUtils.get("service.targetPackage")+"."+shortName+"Service"));
	  
	  
	  anInterface.setVisibility(JavaVisibility.PUBLIC);
	  
	  //data接口
	  Method data=new Method("data");
	  FullyQualifiedJavaType request=  new FullyQualifiedJavaType("javax.servlet.http.HttpServletRequest");
	  
	  FullyQualifiedJavaType pojo=new FullyQualifiedJavaType(PropertiesUtils.get("pojo.targetPackage")+"."+toUp(shortName));
	  
	  FullyQualifiedJavaType ajax=  new FullyQualifiedJavaType(PropertiesUtils.get("ajaxjson"));
	  FullyQualifiedJavaType page=  new FullyQualifiedJavaType(PropertiesUtils.get("page"));
	  
	  anInterface.addImportedType(page);
	  anInterface.addImportedType(pojo);
	  anInterface.addImportedType(request);
	  anInterface.addImportedType(ajax);
	  
	  data.addParameter(new Parameter(pojo, tolow(shortName)));
	  data.addParameter(new Parameter(request, "request"));
	  
	  data.setReturnType(page);
	  data.addJavaDocLine("/** \n"
	  		+ "* @param "+tolow(shortName)+"\n"
	  		+ "* @param "+"request"+"\n"
	  		+ "**/");
	  
	  anInterface.addMethod(data);
	  
	  
	  // view方法
	  Method view=new Method("view");
	  view.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
	  view.setReturnType(ajax);
	  
	  view.addJavaDocLine("/** \n"
		  		+ "* @param "+"id"+" \n "
		  		+ "*/");
	  
	  //get方法
	  
	  Method get=new Method("get");
	  get.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
	  get.setReturnType(pojo);
	  
	  get.addJavaDocLine("/** \n"
		  		+ "* @param "+"id"+" \n "
		  		+ "*/");
	  anInterface.addMethod(get);
	  
	 
	  //delete方法
	  Method delete=new Method("get");
	  delete.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
	  delete.setReturnType(ajax);
	  
	  delete.addJavaDocLine("/** \n"
		  		+ "* @param "+"id"+" \n "
		  		+ "*/");
	  anInterface.addMethod(delete);
	  
	  //saveorupdate
	  Method saveorupdate=new Method("saveOrUpdate");
	  saveorupdate.addParameter(new Parameter(pojo, tolow(shortName)));
	  saveorupdate.setReturnType(ajax);
	  
	  saveorupdate.addJavaDocLine("/** \n"
		  		+ "* @param "+tolow(shortName)+" \n "
		  		+ "*/");
	  anInterface.addMethod(saveorupdate);
	  
	  
	  GeneratedJavaFile fanInterface =  new GeneratedJavaFile(anInterface, "D:\\ccc", javaFormatter);
	  mapperJavaFiles.add(fanInterface);
  }
    
    
    //创建controller
    public void createController(String shortName,List<GeneratedJavaFile> mapperJavaFiles,JavaFormatter javaFormatter){
    	FullyQualifiedJavaType type=new FullyQualifiedJavaType(PropertiesUtils.get("controller.targetPackage")+"."+shortName+"Controller");
    	TopLevelClass controller=new TopLevelClass(type);
    	controller.setSuperClass(new FullyQualifiedJavaType("BaseController"));
    	controller.addImportedType(new FullyQualifiedJavaType("com.code.core.controller.BaseController"));
    	
    	controller.getType().getPackageName();
    	
    	controller.setVisibility(JavaVisibility.PUBLIC);
    	controller.addAnnotation("@Controller");
    	controller.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Controller"));
    	controller.addAnnotation("@RequestMapping(\"/staff\")");
    	controller.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMapping"));
    	
    	String shiro=PropertiesUtils.get("shiro");
    	
    	String swagger=PropertiesUtils.get("swagger2");
    	
    	//导入get ，post request包
    	controller.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.GetMapping"));
    	controller.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.PostMapping"));
    	controller.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.ResponseBody"));
    	
    	controller.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
    	controller.addImportedType(new FullyQualifiedJavaType("org.springframework.validation.annotation.Validated"));
    	controller.addImportedType(new FullyQualifiedJavaType(PropertiesUtils.get("ajaxjson")));
    	
    	controller.addImportedType(new FullyQualifiedJavaType("javax.servlet.http.HttpServletRequest"));
    	//准备service
    	Field field=new Field(toUp(shortName)+"Service", new FullyQualifiedJavaType(PropertiesUtils.get("service.targetPackage")+"."+toUp(shortName)+"Service"));
    	field.addAnnotation("@Autowired");
    	field.setVisibility(JavaVisibility.PUBLIC);
    	controller.addField(field);
    	
    	//导入service包
    	controller.addImportedType(new FullyQualifiedJavaType("com.ACID.service."+toUp(shortName)+"Service"));
    	
    	
    	if("0".equals(shiro)){
    		controller.addImportedType(new FullyQualifiedJavaType("org.apache.shiro.authz.annotation.RequiresPermissions"));
    	}
    	if("0".equals(swagger)){
    		controller.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.ApiImplicitParam"));
    		controller.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.ApiImplicitParams"));
    		controller.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.ApiOperation"));
    	}
    	//view 方法
    	createViewmethod(shortName, shiro, swagger, controller);
    	
    	//delete 方法
    	createDeletemethod(shortName, shiro, swagger, controller);
    	
    	//data方法
    	createDatamethod(shortName, shiro, swagger, controller);
    	
    	//save or update
    	createSaveOrUpdatemethod(shortName, shiro, swagger, controller);
    	
    	//导入实体包
    	controller.addImportedType(new FullyQualifiedJavaType(PropertiesUtils.get("pojo.targetPackage")+"."+toUp(shortName)));
    	
    	GeneratedJavaFile fcontroller =  new GeneratedJavaFile(controller, "D:\\ccc", javaFormatter);
    	mapperJavaFiles.add(fcontroller);
    }
    
    /**
     * controller
     * 创建view 方法
     * @param shortName
     * @param shiro
     * @param swagger
     * @param controller
     */
    public void createViewmethod(String shortName,String shiro,String swagger,TopLevelClass controller){
    	Method viewMethod=new Method("view");
    	
    	
    	if("0".equals(shiro)){
    		viewMethod.addAnnotation("@RequiresPermissions(\""+shortName+":view"+"\")");
    	}
    	if("0".equals(swagger)){
    		viewMethod.addAnnotation("@ApiOperation(value=\"查询"+shortName+"\",notes=\"根据id查询"+shortName+"\")");
        	viewMethod.addAnnotation("@ApiImplicitParams({@ApiImplicitParam(name = \"id\", value = \"id\", dataType = \"String\", required = true,  paramType = \"query\")})");
    	}
    	
    	viewMethod.addAnnotation("@GetMapping(\"/view\")");
    	viewMethod.addAnnotation("@ResponseBody");
    	
    	viewMethod.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
    	viewMethod.setReturnType(new FullyQualifiedJavaType(PropertiesUtils.get("ajaxjson")));
    	viewMethod.setVisibility(JavaVisibility.PUBLIC);
    	viewMethod.addBodyLine("return " +tolow(shortName)+"Service.view(id);");
    	controller.addMethod(viewMethod);
    }
    
    /**
     * save or update
     * controller
     * @param shortName
     * @param shiro
     * @param swagger
     * @param controller
     */
    public void createSaveOrUpdatemethod(String shortName,String shiro,String swagger,TopLevelClass controller){
    	Method viewMethod=new Method("saveOrUpdate");
    	
    	if("0".equals(shiro)){
    		viewMethod.addAnnotation("@RequiresPermissions(\""+shortName+":list"+"\")");
    	}
    	if("0".equals(swagger)){
    		viewMethod.addAnnotation("@ApiOperation(value=\"分页获取"+shortName+"数据\",notes=\"分页查询"+shortName+" 数据\")");
        	viewMethod.addAnnotation("@ApiImplicitParams({"
			+"@ApiImplicitParam(\"name = \"pageNum\", value = \"pageNum\", dataType = \"int\", required = false, defaultValue = \"1\", paramType = \"query\"),"
			+"@ApiImplicitParam(name = \"pageSize\", value = \"pageSize\", dataType = \"int\", required = false, defaultValue = \"10\", paramType = \"query\"),"
			+"})");
    	}
    	viewMethod.addAnnotation("@GetMapping(\"/data\")");
    	viewMethod.addAnnotation("@ResponseBody");
    	
    	Parameter parameter=new Parameter(new FullyQualifiedJavaType(shortName), tolow(shortName));
    	parameter.addAnnotation("@Validated");
    	
    	viewMethod.addParameter(parameter);
    	controller.addImportedType(new FullyQualifiedJavaType("org.springframework.validation.annotation.Validated"));
    	
    	viewMethod.setReturnType(new FullyQualifiedJavaType(PropertiesUtils.get("ajaxjson")));
    	
    	viewMethod.setVisibility(JavaVisibility.PUBLIC);
    	viewMethod.addBodyLine("return " +tolow(shortName)+"Service.saveOrUpdate("+tolow(shortName)+");");
    	controller.addMethod(viewMethod);
    }
    
    /**
     * 分页方法
     * @param shortName
     * @param shiro
     * @param swagger
     * @param controller
     */
    public void createDatamethod(String shortName,String shiro,String swagger,TopLevelClass controller){
    	Method viewMethod=new Method("getData");
    	
    	
    	if("0".equals(shiro)){
    		viewMethod.addAnnotation("@RequiresPermissions(\""+shortName+":list"+"\")");
    	}
    	if("0".equals(swagger)){
    		viewMethod.addAnnotation("@ApiOperation(value=\"分页获取"+shortName+"数据\",notes=\"分页查询"+shortName+" 数据\")");
        	viewMethod.addAnnotation("@ApiImplicitParams({"
			+"@ApiImplicitParam(\"name = \"pageNum\", value = \"pageNum\", dataType = \"int\", required = false, defaultValue = \"1\", paramType = \"query\"),"
			+"@ApiImplicitParam(name = \"pageSize\", value = \"pageSize\", dataType = \"int\", required = false, defaultValue = \"10\", paramType = \"query\"),"
			+"})");
    	}
    	viewMethod.addAnnotation("@GetMapping(\"/data\")");
    	viewMethod.addAnnotation("@ResponseBody");
    	
    	viewMethod.addParameter(new Parameter(new FullyQualifiedJavaType("javax.servlet.http.HttpServletRequest"), "request"));
    	
    	viewMethod.addParameter(new Parameter(new FullyQualifiedJavaType(toUp(shortName)), tolow(shortName)));
    	
    	viewMethod.setReturnType(new FullyQualifiedJavaType(PropertiesUtils.get("page")));
    	controller.addImportedType(new FullyQualifiedJavaType(PropertiesUtils.get("page")));
    	viewMethod.setVisibility(JavaVisibility.PUBLIC);
    	viewMethod.addBodyLine("return " +tolow(shortName)+"Service.data("+tolow(shortName)+",request);");
    	controller.addMethod(viewMethod);
    }
    
    
    /**
     * 删除方法
     * @param shortName
     * @param shiro
     * @param swagger
     * @param controller
     */
    public void createDeletemethod(String shortName,String shiro,String swagger,TopLevelClass controller){
    	Method viewMethod=new Method("delete");
    	
    	if("0".equals(shiro)){
    		viewMethod.addAnnotation("@RequiresPermissions(\""+shortName+":del"+"\")");
    	}
    	if("0".equals(swagger)){
    		viewMethod.addAnnotation("@ApiOperation(value=\"删除"+shortName+"\",notes=\"根据id删除"+shortName+"\")");
        	viewMethod.addAnnotation("@ApiImplicitParams({@ApiImplicitParam(name = \"id\", value = \"id\", dataType = \"String\", required = true,  paramType = \"query\")})");
    	}
    	
    	viewMethod.addAnnotation("@GetMapping(\"/delete\")");
    	viewMethod.addAnnotation("@ResponseBody");
    	
    	viewMethod.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
    	viewMethod.setReturnType(new FullyQualifiedJavaType(PropertiesUtils.get("ajaxjson")));
    	viewMethod.setVisibility(JavaVisibility.PUBLIC);
    	viewMethod.addBodyLine("return " +tolow(shortName)+"Service.delete(id);");
    	controller.addMethod(viewMethod);
    }
    
    /**
     * 废弃exmaple
     * @param unit
     * @param mapperJavaFiles
     * @param javaFormatter
     */
    public void exampleDesc(CompilationUnit unit, List<GeneratedJavaFile> mapperJavaFiles,JavaFormatter javaFormatter){
    	TopLevelClass topclass = (TopLevelClass)unit;
    	Field desc=new Field();
    	desc.setFinal(true);
    	desc.addJavaDocLine("/**exmaple已被废除，不必复制至项目之中**/");
    	desc.setName("多余的类（详细联系928776712@qq.com）");
    	desc.setType(new FullyQualifiedJavaType("String"));
    	desc.setVisibility(JavaVisibility.PRIVATE);
    	topclass.addField(desc);
    	GeneratedJavaFile pojo =  new GeneratedJavaFile(topclass, PropertiesUtils.get("targetProject"), javaFormatter);
    	mapperJavaFiles.add(pojo);
    }
    
    /**
     * 重写pojo
     * @param unit
     * @param mapperJavaFiles
     * @param javaFormatter
     */
    public void resetPojo( CompilationUnit unit, List<GeneratedJavaFile> mapperJavaFiles,JavaFormatter javaFormatter){
    	TopLevelClass topclass = (TopLevelClass)unit;
    	
    	String pojoName=topclass.getType().getShortName();
    	System.out.println(pojoName);
    	//重写pojo
    	TopLevelClass newPojo=new TopLevelClass(topclass.getType());
    	
    	newPojo.setVisibility(JavaVisibility.PUBLIC);
    	
    	String basePojo=PropertiesUtils.get("pojo.rootClass");
    	//继承
    	newPojo.setSuperClass(new FullyQualifiedJavaType(basePojo.substring(basePojo.lastIndexOf(".")+1, basePojo.length())));
    	
    	//导包
    	newPojo.addImportedType(basePojo);
    	
    	String tables_json=PropertiesUtils.get("tables");
    	List<Table> tables=	JsonUtils.jsonToList(tables_json, Table.class);
    	boolean hasId=false;
    	
    	
    	boolean validImportFlag_null=false;
    	boolean validImportFlag_length=false;
    	boolean hasDate=false;
    	for(Table table:tables ){
    		boolean isDate=false;
    		if("id".equals(table.getColumnName())){
    			hasId=true;
    			continue;//进行下一次循环
    		}
    		//排除默认字段
    		if(!hasDef(table.getColumnName())){
    			//定义属性
    			Field field=new Field();
    			field.addJavaDocLine("/**"+table.getRemarks()+"*/");
    			
    			if("date".equals(table.getJavaType().toLowerCase())){
    				newPojo.addImportedType(new FullyQualifiedJavaType("java.util.Date"));
    				isDate=true;
    				hasDate=true;
    			}
    			field.setType(new FullyQualifiedJavaType(table.getJavaType()));
    			
    			field.setVisibility(JavaVisibility.PRIVATE);
    			field.setName(table.getJavaColumnName());
    			
    			
    			
    			newPojo.addField(field);
    			
    			//创建get set 
    			Method getmethod=new Method("get"+toUp(table.getJavaColumnName()));
    			getmethod.setVisibility(JavaVisibility.PUBLIC);
    			getmethod.setReturnType(new FullyQualifiedJavaType(table.getJavaType()));
    			getmethod.addBodyLine("return "+table.getJavaColumnName()+";");
    			
    				//json格式化日期
	    			if(isDate){
	    				getmethod.addAnnotation("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\",timezone=\"GMT+8\")");
	    			}
    			
    				if(1==table.getLengthLimit()){
    				
    				if("Integer".equals(table.getJavaType())||"Double".equals(table.getJavaType())||"Float".equals(table.getJavaType())){
    					//field.addAnnotation("@NotEmpty(message=\""+table.getLengthLimit_tip() +"\")");
    				}else if(isDate){
    					
    					getmethod.addAnnotation("@Null(message=\""+table.getLengthLimit_tip() +"\")");
    					  validImportFlag_null=true;
    				}else{
    					//需要验证参数
    					getmethod.addAnnotation("@Length(min="+table.getLengthLimit_min()+", max="+table.getLengthLimit_max()+", message=\""+table.getLengthLimit_tip()+"\")");
    					validImportFlag_length=true;
    				}
    			}
    			
    			
    			newPojo.addMethod(getmethod);
    			
    			Method setmethod=new Method("set"+toUp(table.getJavaColumnName()));
    			setmethod.setVisibility(JavaVisibility.PUBLIC);
    			setmethod.setReturnType(new FullyQualifiedJavaType("void"));
    			setmethod.addParameter(new Parameter(new FullyQualifiedJavaType(table.getJavaType()), table.getJavaColumnName()));
    			setmethod.addBodyLine("this."+table.getJavaColumnName()+"="+table.getJavaColumnName()+";");
    			newPojo.addMethod(setmethod);
    		}
    	}
    	
    	//导入验证注解包
    	if(validImportFlag_length){
    		newPojo.addImportedType(new FullyQualifiedJavaType("org.hibernate.validator.constraints.Length"));
    	}
    	if(validImportFlag_null){
    		newPojo.addImportedType(new FullyQualifiedJavaType("javax.validation.constraints.Null"));
    	}
    	if(hasDate){
    		newPojo.addImportedType(new FullyQualifiedJavaType("com.fasterxml.jackson.annotation.JsonFormat"));
    		
    	}
    	
    	
    	//新增默认构造器
    	Method cons1=new Method(pojoName);
    	cons1.setConstructor(true);
    	cons1.setVisibility(JavaVisibility.PUBLIC);
    	cons1.addBodyLine("");
    	newPojo.addMethod(cons1);
    	
    	//有参构造器
    	if(hasId){
    		Method cons2=new Method(pojoName);
    		cons2.setVisibility(JavaVisibility.PUBLIC);
        	cons2.setConstructor(true);
        	cons2.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
        	cons2.addBodyLine("this.id=id;");
        	newPojo.addMethod(cons2);
    	}
    	
    	//序列化
    	newPojo.addImportedType(new FullyQualifiedJavaType("java.io.Serializable"));
    	newPojo.addSuperInterface(new FullyQualifiedJavaType("Serializable"));
    	Field version=new Field();
    	version.setFinal(true);
    	version.setName("serialVersionUID = 1L");
    	version.setStatic(true);
    	version.setType(new FullyQualifiedJavaType("long"));
    	version.setVisibility(JavaVisibility.PRIVATE);
    	newPojo.addField(version);
    	
    	
    	GeneratedJavaFile fnewPojo =  new GeneratedJavaFile(newPojo, "D:\\ccc", javaFormatter);
    	mapperJavaFiles.add(fnewPojo);
    	
    	//序列化---屏蔽原来的
    	//GeneratedJavaFile pojo =  new GeneratedJavaFile(topclass,PropertiesUtils.get("targetProject"), javaFormatter);
    	//mapperJavaFiles.add(pojo);
    }
    /**
     * 生成枚举类对应数据库表字段
     * @param shortName
     * @param mapperJavaFiles
     * @param javaFormatter
     * @param introspectedTable
     */
    public void createTableEnum(String shortName, List<GeneratedJavaFile> mapperJavaFiles,JavaFormatter javaFormatter,IntrospectedTable introspectedTable){
    	//需要额外生成枚举表字段类
    	TopLevelEnumeration enumeration=new TopLevelEnumeration(new FullyQualifiedJavaType(PropertiesUtils.get("pojoenum.pojoEnumPageName")+"."+shortName+"Enum"));
    	
    	enumeration.setVisibility(JavaVisibility.PUBLIC);
    	
    	new MyCommentGenerator().addAuth(enumeration, PropertiesUtils.get("copyright.desc"));
    	//添加私有公共的方法
    	Field field=new Field("value_",new FullyQualifiedJavaType("String"));
    	field.setVisibility(JavaVisibility.PRIVATE);
    	enumeration.addField(field);
    	
    	//构造器
    	Method cons=new Method(shortName+"Enum");
    	cons.setConstructor(true);
    	cons.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "value"));
    	cons.addBodyLine("this.value_=value;");
    	enumeration.addMethod(cons);
    	//get set 方法
    	Method setmethod=new Method("setValue");
    	setmethod.setVisibility(JavaVisibility.PUBLIC);
    	//参数
    	setmethod.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "value"));
    	setmethod.addBodyLine("this.value_=value;");
    	enumeration.addMethod(setmethod);
    	
    	//get方法
    	Method getmethod=new Method("getValue");
    	getmethod.setVisibility(JavaVisibility.PUBLIC);
    	getmethod.setReturnType(new FullyQualifiedJavaType("String"));
    	getmethod.addBodyLine("return value_;");
    	enumeration.addMethod(getmethod);
    	//枚举
    	for(IntrospectedColumn column:introspectedTable.getAllColumns()){
    		try {
    			String cname=column.getActualColumnName();
    			if(!hasDef(cname)){
    				String name=StringUtils.toCamelCase(cname);//大小写转换
        			enumeration.addEnumConstant(name+"(\""+cname+"\")");
    			}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	GeneratedJavaFile mapperJavafile =  new GeneratedJavaFile(enumeration, PropertiesUtils.get("targetProject"), javaFormatter);
    	mapperJavaFiles.add(mapperJavafile);
    }
    
    /**
     * 禁止生成默认枚举
     * @param name
     * @return
     */
    public boolean hasDef(String name){
    	return indexDef( PropertiesUtils.get("pojo.rootClass.columns").toString(), name);
    }
    /**
     * value 含有 str时返回true
     * @param value
     * @param str
     * @return
     */
    public boolean indexDef(String value,String str){
    	if(value.indexOf(str)>=0){
    		return true;
    	}
    	return false;
    }
    
    /**
     * 首字母大写
     * @param str
     * @return
     */
    public String toUp(String str){
    	
    	StringBuffer sb=new StringBuffer();
    	for(int i=0;i<str.length();i++){
    		if(i==0){
    			sb.append((str.charAt(i)+"").toUpperCase());
    		}else{
    			sb.append(str.charAt(i));
    		}
    	}
    	return sb.toString();
    }
    
    /**
     * 首字母小写
     * @param str
     * @return
     */
    public String tolow(String str){
    	
    	StringBuffer sb=new StringBuffer();
    	for(int i=0;i<str.length();i++){
    		if(i==0){
    			sb.append((str.charAt(i)+"").toLowerCase());
    		}else{
    			sb.append(str.charAt(i));
    		}
    	}
    	return sb.toString();
    }
    
}