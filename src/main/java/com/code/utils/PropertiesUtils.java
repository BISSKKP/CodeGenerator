package com.code.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
	public  static Properties prop   = new Properties();
	 
	 private static final String tempFile_comment="临时生成文件";
	static {
		 InputStream inputStream = PropertiesUtils.class.getResourceAsStream("/config.properties");
		             try {
						prop.load(inputStream);
					} catch (IOException e) {
						e.printStackTrace();
					}
	}
	
	public static String get(String value){
		return prop.get(value).toString();
	}
	
	/**
	 * 返回默认copy路径
	 */
	public static String  getTemplateFilePath(){
		return get("copyPath");
	}
	
	public static void setValue(String key,String value){
		prop.setProperty(key, value);
	}
	
	/**
	 * 将配置文件保存
	 *保存在默认路径
	 */
	@SuppressWarnings("deprecation")
	public static void save(String filePath,String comment){
		try {
			File f=new File(filePath);
			File pf=f.getParentFile();
			if(!pf.exists()){
				pf.mkdirs();
			}
			FileOutputStream out=	new FileOutputStream(f);
			prop.save(out, comment);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存在默认位置文件
	 */
	public static void save(){
		save(getTemplateFilePath(),tempFile_comment);
	}
	
	/**
	 * 删除文件默认位置文件
	 */
	public static void delete(){
		delete(getTemplateFilePath());	
	}
	
	/**
	 *删除文件
	 * @param filePath
	 */
	public static void delete(String filePath){
		try {
			File f=new File(filePath);
			if(f.exists()){
				f.delete();
			}
		//	prop.load(PropertiesUtils.class.getResourceAsStream("/config.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
