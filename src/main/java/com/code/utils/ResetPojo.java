package com.code.utils;

import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class ResetPojo {

	
	/**
	 * 重写pojo
	 * 
	 * @param unit
	 * @param mapperJavaFiles
	 * @param javaFormatter
	 */
	public static  void resetPojo(CompilationUnit unit, List<GeneratedJavaFile> mapperJavaFiles, JavaFormatter javaFormatter) {
		TopLevelClass topclass = (TopLevelClass) unit;

		String pojoName = topclass.getType().getShortName();
		// 重写pojo
		TopLevelClass newPojo = new TopLevelClass(CommonJavaType.getPojo(pojoName));

		newPojo.setVisibility(JavaVisibility.PUBLIC);

		
		// 继承
		newPojo.setSuperClass(CommonJavaType.getBasePojo());

		// 导包
		newPojo.addImportedType(CommonJavaType.getBasePojo());

		String tables_json = PropertiesUtils.get("tables");
		List<Table> tables = JsonUtils.jsonToList(tables_json, Table.class);
		boolean hasId = false;

		boolean validImportFlag_null = false;//
		boolean intvalidflag=false;	//int
		boolean validImportFlag_length = false;
		boolean hasDate = false;
		for (Table table : tables) {
			boolean isDate = false;
			if ("id".equals(table.getColumnName())) {
				hasId = true;
				continue;// 进行下一次循环
			}
			// 排除默认字段
			if (!StringUtils.hasDef(table.getColumnName())) {
				// 定义属性
				Field field = new Field();
				field.addJavaDocLine("/**" + table.getRemarks() + "*/");

				if ("date".equals(table.getJavaType().toLowerCase())) {
					newPojo.addImportedType(CommonJavaType.date);
					isDate = true;
					hasDate = true;
				}
				field.setType(new FullyQualifiedJavaType(table.getJavaType()));

				field.setVisibility(JavaVisibility.PRIVATE);
				field.setName(table.getJavaColumnName());

				newPojo.addField(field);

				// 创建get set
				Method getmethod = new Method("get" + StringUtils.toUp(table.getJavaColumnName()));
				getmethod.setVisibility(JavaVisibility.PUBLIC);
				getmethod.setReturnType(new FullyQualifiedJavaType(table.getJavaType()));
				getmethod.addBodyLine("return " + table.getJavaColumnName() + ";");

				// json格式化日期
				if (isDate) {
					getmethod.addAnnotation("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\",timezone=\"GMT+8\")");
				}

				if (1 == table.getLengthLimit()) {

					if ("Integer".equals(table.getJavaType()) || "Double".equals(table.getJavaType())
							|| "Float".equals(table.getJavaType())) {
						
						if(table.getIsNullAble().toLowerCase().equals("no")){
							 field.addAnnotation("@NotNull(message=\""+table.getLengthLimit_tip()
							 +"\")");
							 validImportFlag_null=true;
						}
						
						 
					} else if (isDate) {

						getmethod.addAnnotation("@NotNull(message=\"" + table.getLengthLimit_tip() + "\")");
						validImportFlag_null = true;
					} else {
						// 需要验证参数
						getmethod.addAnnotation("@Length(min=" + table.getLengthLimit_min() + ", max="
								+ table.getLengthLimit_max() + ", message=\"" + table.getLengthLimit_tip() + "\")");
						
						getmethod.addAnnotation("@NotBlank(  message=\"" + table.getLengthLimit_tip() + "\")");
						
						validImportFlag_length = true;
					}
				}

				newPojo.addMethod(getmethod);

				Method setmethod = new Method("set" + StringUtils.toUp(table.getJavaColumnName()));
				setmethod.setVisibility(JavaVisibility.PUBLIC);
				setmethod.setReturnType(new FullyQualifiedJavaType("void"));
				setmethod.addParameter(
						new Parameter(new FullyQualifiedJavaType(table.getJavaType()), table.getJavaColumnName()));
				setmethod.addBodyLine("this." + table.getJavaColumnName() + "=" + table.getJavaColumnName() + ";");
				newPojo.addMethod(setmethod);
			}
		}

		// 导入验证注解包
		if (validImportFlag_length) {
			newPojo.addImportedType(CommonJavaType.validated_length);
			newPojo.addImportedType(CommonJavaType.validated_String_NotBlank);
		}
		
		
		
		if (validImportFlag_null) {
			newPojo.addImportedType(CommonJavaType.validated_Integer_NotNull);
		}
		
		if (hasDate) {
			newPojo.addImportedType(CommonJavaType.date_JsonFormat);
		}

		// 新增默认构造器
		Method cons1 = new Method(pojoName);
		cons1.setConstructor(true);
		cons1.setVisibility(JavaVisibility.PUBLIC);
		cons1.addBodyLine("");
		newPojo.addMethod(cons1);

		// 有参构造器
		if (hasId) {
			Method cons2 = new Method(pojoName);
			cons2.setVisibility(JavaVisibility.PUBLIC);
			cons2.setConstructor(true);
			cons2.addParameter(CommonJavaType.getIdParameter());
			cons2.addBodyLine("this.id=id;");
			newPojo.addMethod(cons2);
		}

		// 序列化
		newPojo.addImportedType(CommonJavaType.serializable);
		newPojo.addSuperInterface(CommonJavaType.serializable);
		Field version = new Field();
		version.setFinal(true);
		version.setName("serialVersionUID = 1L");
		version.setStatic(true);
		version.setType(new FullyQualifiedJavaType("long"));
		version.setVisibility(JavaVisibility.PRIVATE);
		newPojo.addField(version);

		GeneratedJavaFile fnewPojo = new GeneratedJavaFile(newPojo, PropertiesUtils.get("targetProject"), javaFormatter);
		mapperJavaFiles.add(fnewPojo);

		// 序列化---屏蔽原来的
		// GeneratedJavaFile pojo = new
		// GeneratedJavaFile(topclass,PropertiesUtils.get("targetProject"),
		// javaFormatter);
		// mapperJavaFiles.add(pojo);
	}
	
}
