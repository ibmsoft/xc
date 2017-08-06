package com.xzsoft.xc.fa.dao.impl;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.fa.dao.GlInterfaceDao;
import com.xzsoft.xc.fa.mapper.GlInterfaceMapper;

@Repository("glInterfaceDao")
public class GlInterfaceImpl implements GlInterfaceDao {

	private static final Logger log = Logger.getLogger(GlInterfaceImpl.class);

	@Resource
	private GlInterfaceMapper glInterfaceMapper;
	
	@Override
	public void insertGlInterfaceHead(HashMap interfaceHeadJson) {
		glInterfaceMapper.insertGlInterfaceHead(interfaceHeadJson);
	}

	@Override
	public void updateGlInterface(String iHeadId, String glInterfaceIds, String lastUpdateBy, Date lastUpdateDate) {
		glInterfaceMapper.updateGlInterface(iHeadId, glInterfaceIds, lastUpdateBy, lastUpdateDate);
	}

	@Override
	public void deleteGlInterfaceHead(String iHeadId) {
		glInterfaceMapper.deleteGlInterfaceHead(iHeadId);
	}

	@Override
	public void updateGlInterface(String iHeadId, String lastUpdateBy,
			Date lastUpdateDate) {
		glInterfaceMapper.updateGlInterfaceByIhead(iHeadId, lastUpdateBy, lastUpdateDate);
	}
	
}