package com.code.core.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.ACID.core.mapper.BasePojo;
import com.ACID.core.mapper.MapperService;
import com.ACID.core.mapper.SysUser;
import com.ACID.utils.page.DefaultPagerHelper;
import com.ACID.utils.page.UserUtils;
import com.code.utils.StringUtils;
import com.github.pagehelper.PageInfo;


public abstract class BaseService<M extends MapperService<T> ,T extends BasePojo> {
	
	
	@Autowired
	protected M mapper;
	
	/**
	 * 封装插入方法
	 * @param t
	 * @return
	 */
	public int save(T t){
		if(StringUtils.isNotBlank(t.getId())){
			if(t.isNewRecorde()){
				//插入
				//insert
				preInsert(t);
				mapper.insert(t);
			}else{
				//更新
				preUpdate(t);
			return 	mapper.updateByPrimaryKeySelective(t);
				
			}
		}else{
			//insert
			preInsert(t);
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
		preUpdate(t);
		return mapper.deleteBylogic(t);
	}
	
	/**
	 * 插入之前
	 * @param t
	 */
	private void preInsert(T t){
		t.setCreateDate(new Date());
		t.setUpdateDate(new Date());
		if(StringUtils.isBlank(t.getCreateBy())){
			SysUser obj=UserUtils.getUser();
			if(obj!=null){
				t.setCreateBy(obj.getId());
				t.setUpdateBy(obj.getId());
			}
		}
	}
	/**
	 * 更新之前
	 */
	private void preUpdate(T t){
		t.setUpdateDate(new Date());
		SysUser obj=UserUtils.getUser();
		if(obj!=null){
			t.setUpdateBy(obj.getId());
		}
	}
	
	/**
	 * 分页获取数据
	 * @param t
	 * @param request
	 * @return
	 */
	public PageInfo<T> findPage(T t,HttpServletRequest request){
		DefaultPagerHelper.DefStartPage(request);
		List<T> list=	mapper.findList(t);
		return new PageInfo<>(list);
	}

}
