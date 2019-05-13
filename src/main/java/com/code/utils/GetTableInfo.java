package com.code.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class GetTableInfo {
	
	 //输出表名
    public  List<String> printTableNames()throws Exception{
    	List<String> list=new ArrayList<>();
        //获取表名的结果集
        ResultSet rs = getDatabaseMetaData().getTables(null, null, null, new String[]{"TABLE"});
        while(rs.next()){
        	
            String tableName = rs.getString("TABLE_NAME");
           // System.out.println(rs.getString("REMARKS"));
            //System.out.println(tableName);
            list.add(tableName);
        }
        return list;
    }
 
    //输出列名、类型、注释
    public  List<Map<String, Object>> printColumnInfo(String tableName)throws Exception{
    	List<Map<String, Object>> list=new ArrayList<>();
    	Map<String, Object> map=null;
        ResultSet rs = getDatabaseMetaData().getColumns(null, "%", tableName, "%");
        while(rs.next()){
        	map=new HashMap<>();
            //列名
            String columnName = rs.getString("COLUMN_NAME");
            //类型
            String typeName = rs.getString("TYPE_NAME");
            //注释
            String remarks = rs.getString("REMARKS");
            
            //
            int columnSize = rs.getInt("COLUMN_SIZE");  //列大小    
            /**  
             * ISO规则用来确定某一列的是否可为空(等同于NULLABLE的值:[ 0:'YES'; 1:'NO'; 2:''; ]) 
             * YES -- 该列可以有空值;  
             * NO -- 该列不能为空; 
             * 空字符串--- 不知道该列是否可为空 
             */    
            String isNullAble = rs.getString("IS_NULLABLE");
            
          //  System.out.println(columnName + "--" + typeName +"--"+ columnSize+"--" + remarks+"--"+isNullAble);
            
            map.put("columnName",columnName);
            map.put("typeName",typeName);
            map.put("remarks",remarks);
            map.put("columnSize",columnSize);
            map.put("isNullAble",isNullAble);
            list.add(map);
        }
        return list;
    }
    
    public DatabaseMetaData getDatabaseMetaData(){
    	 try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = PropertiesUtils.get("jdbc.url");
	         String user =  PropertiesUtils.get("jdbc.userId");
	         String password = PropertiesUtils.get("jdbc.password");
	         Connection connection = DriverManager.getConnection(url, user, password);
	         DatabaseMetaData databaseMetaData = connection.getMetaData();
	    	
	    	return databaseMetaData;
		} catch (Exception e) {
			e.printStackTrace();
		}
         return null;
    }
    
 
    public static void main(String[] args) throws Exception{
       
      // new GetTableInfo().printTableNames();
        new GetTableInfo().printColumnInfo("sys_email");
    }

}
