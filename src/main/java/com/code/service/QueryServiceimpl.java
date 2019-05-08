package com.code.service;

import org.springframework.stereotype.Service;

import com.code.mapper.AmStaffMapper;
import com.code.pojo.AmStaff;

@Service
public class QueryServiceimpl extends BaseService<AmStaffMapper,AmStaff>  implements QueryService{

	public void test(){
		
	}

	@Override
	public AmStaff view(String id) {

		
		return get(id);
	}
	
	
	 
	
}
