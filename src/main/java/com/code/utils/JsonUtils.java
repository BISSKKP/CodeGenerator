package com.code.utils;

import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.JSONPObject;


public class JsonUtils extends ObjectMapper {
	

	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	private static final long serialVersionUID = 1L;

	// 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private static JsonUtils jsonUtils;
    
    public JsonUtils() {
		this(Include.NON_EMPTY);
	}
    
    public JsonUtils(Include include) {
		// 设置输出时包含属性的风格
		if (include != null) {
			this.setSerializationInclusion(include);
		}
		// 允许单引号、允许不带引号的字段名称
		this.enableSimple();
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 空值处理为空串
		this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
			@Override
			public void serialize(Object value, JsonGenerator jgen,
					SerializerProvider provider) throws IOException,
					JsonProcessingException {
				jgen.writeString("");
			}
        });
		// 进行HTML解码。
		this.registerModule(new SimpleModule().addSerializer(String.class, new JsonSerializer<String>(){
			@Override
			public void serialize(String value, JsonGenerator jgen,
					SerializerProvider provider) throws IOException,
					JsonProcessingException {
				//jgen.writeString(StringEscapeUtils.unescapeHtml4(value));
				//xss攻击避免
				jgen.writeString(value);
			}
        }));
		// 设置时区
		this.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));//getTimeZone("GMT+8:00")
	}

    /**
	 * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.
	 */
	public static JsonUtils getInstance() {
		if (jsonUtils == null){
			jsonUtils = new JsonUtils().enableSimple();
			jsonUtils.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
				@Override
				public void serialize(Object value, JsonGenerator jgen,
						SerializerProvider provider) throws IOException,
						JsonProcessingException {
					jgen.writeString("");
				}
	        });
		}
		return jsonUtils;
	}
	
	/**
	 * Object可以是POJO，也可以是Collection或数组。
	 * 如果对象为Null, 返回"null".
	 * 如果集合为空集合, 返回"[]".
	 */
	public String toJson(Object object) {
		try {
			return this.writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to json string error:" + object, e);
			return null;
		}
	}

	/**
	 * 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。
	 */
	public static JsonUtils nonDefaultMapper() {
		if (jsonUtils == null){
			jsonUtils = new JsonUtils(Include.NON_DEFAULT);
		}
		return jsonUtils;
	} 
	
	/**
	 * 允许单引号
	 * 允许不带引号的字段名称
	 */
	public JsonUtils enableSimple() {
		this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		return this;
	}
	

	
    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
    	try {
    		MAPPER.setSerializationInclusion(Include.NON_NULL);  
			String string = MAPPER.writeValueAsString(data);
			
    		//String string= getInstance().objectToJson(data);
    		return string;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 将json结果集转化为对象
     * 
     * @param jsonData json数据
     * @param clazz 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
        		  T t = MAPPER.readValue(jsonData, beanType);
                  return t;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
    	try {
				JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
	    		List<T> list = MAPPER.readValue(jsonData, javaType);
	    		return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
	 * 輸出JSONP格式數據.
	 */
	public String toJsonP(String functionName, Object object) {
		return toJson(new JSONPObject(functionName, object));
	}
	
	
	
	
}
