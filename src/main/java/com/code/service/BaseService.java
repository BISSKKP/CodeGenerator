package com.code.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.core.pojo.BasePojo;
import com.code.core.service.MapperService;

public abstract class BaseService<M extends MapperService<T> ,T extends BasePojo> {
	
	
	@Autowired
	private M mapper;
	
	
	public T get(String id){
		
		return mapper.selectByPrimaryKey(id);
	}
	
	
	public int save(T t){
		if(t.getId()!=null&&t.getId().trim()!=""){
			if(t.isNewRecorde()){
				//插入
				//insert
				t.preInsert();
				
				mapper.insert(t);
			}else{
				//更新
				t.preUpdate();
			return 	mapper.updateByPrimaryKeySelective(t);
				
			}
		}else{
			//insert
			t.preInsert();
			t.setId(UUID.randomUUID().toString());
			return	mapper.insert(t);
		}
		return 0;
	}
	
	/**
	 * 逻辑删除
	 * @param t
	 * @return
	 */
	public int deleteByLogic(T t){
		
		
		
		return mapper.deleteBylogin(t);
	}
	
	
	

}
