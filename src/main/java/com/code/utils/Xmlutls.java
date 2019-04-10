package com.code.utils;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Xmlutls {
	
	
	public static void main(String[] args) {

		String xml="<update id='updateByPrimaryKey' parameterType='com.ACID.pojo.am.staff.AmStaff' >"
				+" update am_staff"
  +" set first_name = #{firstName,jdbcType=VARCHAR},"
		  +" last_name = #{lastName,jdbcType=VARCHAR},"
    		+" english_name = #{englishName,jdbcType=VARCHAR},"
    		+" email = #{email,jdbcType=VARCHAR},"
    		+" business_no = #{businessNo,jdbcType=VARCHAR},"
    		+" job_title = #{jobTitle,jdbcType=INTEGER},"
    		+"  employment_date = #{employmentDate,jdbcType=DATE},"
    		+" resignation_date = #{resignationDate,jdbcType=DATE},"
    		+" skype = #{skype,jdbcType=VARCHAR},"
    		+" gender = #{gender,jdbcType=INTEGER},"
    		+" tel = #{tel,jdbcType=VARCHAR},"
    		+" create_date = #{createDate,jdbcType=TIMESTAMP},"
    		+" create_by = #{createBy,jdbcType=VARCHAR},"
    		+" update_date = #{updateDate,jdbcType=TIMESTAMP},"
    		+" update_by = #{updateBy,jdbcType=VARCHAR},"
    		+"   del_flag = #{delFlag,jdbcType=CHAR}"
 +" where id = #{id,jdbcType=VARCHAR}"
+"</update>";
		
		String xml2="<select id=\"selectByExample\" resultMap=\"BaseResultMap\" parameterType=\"com.ACID.pojo.am.staff.AmStaffExample\" >"
				+" select"
		  +" <if test=\"distinct\" >"
		  +"   distinct"
    		+"  </if>"
  +"  <include refid=\"Base_Column_List\" />"
		  +"  from am_staff"
		  +"  <if test=\"_parameter != null\" >"
		  +"   <include refid=\"Example_Where_Clause\" />"
    		+"  </if>"
		  +" <if test=\"orderByClause != null\" >"
		  +"  order by ${orderByClause}"
    		+"  </if>"
		  +"</select>";
		try {
		System.out.println(xmlToMap(xml2));
					
					
					} catch (Exception e) {
						e.printStackTrace();
					}
	
	}
	

	
	public static Map<String, Object> xmlToMap(String xml){
		Map<String, Object> map=new HashMap<>()
				;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// 创建DocumentBuilder
			DocumentBuilder builder = factory.newDocumentBuilder();
			// 创建解析xml的Document
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
				
			String rootName	=doc.getDocumentElement().getTagName();
				
			org.w3c.dom.NamedNodeMap nodeMap=doc.getDocumentElement().getAttributes();
			
			for(int i=0;i<nodeMap.getLength();i++){
				String  key=nodeMap.item(i).getNodeName();
				String value=doc.getDocumentElement().getAttribute(key);
				map.put(key, value);
			}
			
			//
			map.put("content", xml.subSequence(xml.indexOf(">")+1, xml.lastIndexOf("<")));
			map.put("root", rootName);
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	

}
