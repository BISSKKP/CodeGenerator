package com.code.utils;

public class StringUtils {
	   
    /**
     * 驼峰命名时使用
     */
    private static final char SEPARATOR = '_';
	 
    /**驼峰命名
     * 
     * @param s
     * @return
     */
    public  static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
    
    
    /**
	 * 首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String toUp(String str) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if (i == 0) {
				sb.append((str.charAt(i) + "").toUpperCase());
			} else {
				sb.append(str.charAt(i));
			}
		}
		return sb.toString();
	}

	/**
	 * 首字母小写
	 * 
	 * @param str
	 * @return
	 */
	public static String tolow(String str) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if (i == 0) {
				sb.append((str.charAt(i) + "").toLowerCase());
			} else {
				sb.append(str.charAt(i));
			}
		}
		return sb.toString();
	}
    /**
     * 获取换行符
     * @return
     */
	public static String  getNewLine(){
		
		return System.getProperty("line.separator");
	}
	
	/**
	 * 禁止生成默认枚举
	 * 
	 * @param name
	 * @return
	 */
	public static boolean hasDef(String name) {
		return indexDef(PropertiesUtils.get("pojo.rootClass.columns").toString(), name);
	}

	/**
	 * value 含有 str时返回true
	 * 
	 * @param value
	 * @param str
	 * @return
	 */
	public  static boolean indexDef(String value, String str) {
		if (value.indexOf(str) >= 0) {
			return true;
		}
		return false;
	}


	public static boolean isNotBlank(String str) {
		
		return !org.springframework.util.StringUtils.isEmpty(str);
	}


	public static boolean isBlank(String str) {
		// TODO Auto-generated method stub
		return !isNotBlank(str);
	}
    
}
