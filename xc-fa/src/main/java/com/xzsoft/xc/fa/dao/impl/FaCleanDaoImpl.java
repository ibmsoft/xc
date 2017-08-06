package com.xzsoft.xc.fa.dao.impl;



import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.fa.dao.FaCleanDao;
import com.xzsoft.xc.fa.mapper.FaCleanMapper;
import com.xzsoft.xc.fa.modal.FaAddition;
@Repository("faCleanDao")
public class FaCleanDaoImpl  implements FaCleanDao {
	
	@Resource
	private FaCleanMapper FaCleanMapper;



	@Override
	public void cleanFaAddition(FaAddition faAddition) {
		FaCleanMapper.cleanFaAddition(faAddition);
		
	}



	@Override
	public void deleteInterface(FaAddition faAddition) {
		FaCleanMapper.deleteInterface(faAddition);
		
	}



}
