package com.ACID.utils.page;

import javax.servlet.http.HttpServletRequest;

import com.code.utils.StringUtils;
import com.github.pagehelper.PageHelper;

/**
 * 预设分页
 * @author howard
 *
 */
public class DefaultPagerHelper extends PageHelper {

	/**
	 * 默认第一页
	 */
	private static Integer defPageNum=1;
	
	/**
	 * 默认一页15条数据
	 */
	private static Integer defPageSize=5;
	
	/**
	 * 默认设置分页参数
	 * @param request
	 */
	public static void DefStartPage(HttpServletRequest request){
		Integer dpageNum=defPageNum;
		Integer dpageSize=defPageSize;
		String pageNum=request.getParameter("pageNum");
		String pageSize=request.getParameter("pageSize");
		
		try {
			if(StringUtils.isNotBlank(pageNum)&&pageNum.indexOf(".")==-1){
				dpageNum=Integer.parseInt(pageNum);
				if(dpageNum<=0){
					dpageNum=1;
				}
			}
			if(StringUtils.isNotBlank(pageSize)&&pageSize.indexOf(".")==-1){
				dpageSize=Integer.parseInt(pageSize);
				if(dpageSize<=0){
					dpageSize=15;
				}
			}
		} catch (Exception e) {
		}
		startPage(dpageNum, dpageSize);
	}
	
	
}
