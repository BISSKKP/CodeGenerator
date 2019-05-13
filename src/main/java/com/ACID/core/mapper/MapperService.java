package com.ACID.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 查询父类
 * @author ACID
 *
 * @param <POJO>
 * @param <POJOEXAPLE>
 */
public interface MapperService<POJO> {

	/**
	 * 根据example统计数据
	 * @param example
	 * @return
	 */
	 int countByExample(BaseExample example);

	 	/**
	 	 * 根据example删除数据
	 	 * @param example
	 	 * @return
	 	 */
	    int deleteByExample(BaseExample example);

	    /**
	     *根据id删除数据
	     * @param id
	     * @return
	     */
	    int deleteByPrimaryKey(String id);
	    
	    /**
	     * 插入实体
	     * @param record
	     * @return
	     */
	    int insert(POJO record);
	    
	    /**
	     * 插入已有值
	     * @param record
	     * @return
	     */
	    int insertSelective(POJO record);

	    /**
	     * 根据example查询数据
	     * @param example
	     * @return
	     */
	    List<POJO> selectByExample(BaseExample example);

	    /**
	     * 根据主键查询数据
	     * @param id
	     * @return
	     */
	    POJO selectByPrimaryKey(String id);

	    /**
	     * 根据已有之的example更新
	     * @param record
	     * @param example
	     * @return
	     */
	    int updateByExampleSelective(@Param("record") POJO record, @Param("example") BaseExample example);

	    /**
	     * 根据example更新
	     * @param record
	     * @param example
	     * @return
	     */
	    int updateByExample(@Param("record") POJO record, @Param("example") BaseExample example);

	    /**
	     * 根据id更新一有值得属性
	     * @param record
	     * @return
	     */
	    int updateByPrimaryKeySelective(POJO record);

	    /**
	     * 根据主键更新
	     * @param record
	     * @return
	     */
	    int updateByPrimaryKey(POJO record);
	    
	    /**
	     * 逻辑删除
	     * @param record
	     * @return
	     */
	    int deleteBylogic(POJO record);
	    

	    /**
	     * 额外查询list
	     * @param record
	     * @return
	     */
	    List<POJO> findList(POJO record);
}
