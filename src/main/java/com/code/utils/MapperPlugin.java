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
		
		return super.sqlMapDocumentGenerated(document, introspectedTable);
    }
	
	/**
	 * 新增逻辑删除代码
	 * @param document
	 * @param introspectedTable
	 */
	public void addDeleteMethod(Document document,IntrospectedTable introspectedTable){
		 XmlElement update = new XmlElement("update");
	        update.addAttribute(new Attribute("id", "deleteBylogin"));
//	        update.addAttribute(new Attribute("resultType", "java.lang.Integer"));//mybatis insert ,update 没有返回值
	        update.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
	        update.addElement(new TextElement(" update  "+ introspectedTable.getFullyQualifiedTableNameAtRuntime()+" set del_flag=#{delFlag} where id =#{id}"));
	        XmlElement parentElement = document.getRootElement();
	        parentElement.addElement(update);
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
            	ser(unit, mapperJavaFiles, javaFormatter);
            	//创建枚举
            	createTableEnum(shortName, mapperJavaFiles, javaFormatter, introspectedTable);
            }
        }
        return mapperJavaFiles;
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
     * 实体类序列化
     * @param unit
     * @param mapperJavaFiles
     * @param javaFormatter
     */
    public void ser( CompilationUnit unit, List<GeneratedJavaFile> mapperJavaFiles,JavaFormatter javaFormatter){
    	TopLevelClass topclass = (TopLevelClass)unit;
    	topclass.addImportedType(new FullyQualifiedJavaType("java.io.Serializable"));
    	topclass.addSuperInterface(new FullyQualifiedJavaType("Serializable"));
    	Field version=new Field();
    	version.setFinal(true);
    	version.setName("serialVersionUID = 1L");
    	version.setStatic(true);
    	version.setType(new FullyQualifiedJavaType("long"));
    	version.setVisibility(JavaVisibility.PRIVATE);
    	topclass.addField(version);
    	GeneratedJavaFile pojo =  new GeneratedJavaFile(topclass, PropertiesUtils.get("targetProject"), javaFormatter);
    	mapperJavaFiles.add(pojo);
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
    
    
    
    
}