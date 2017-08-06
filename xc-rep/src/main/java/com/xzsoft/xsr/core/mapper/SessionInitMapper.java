package com.xzsoft.xsr.core.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xsr.core.modal.DataCenter;

public interface SessionInitMapper {

	public List<HashMap> getSuitByUserId(String userId) throws Exception;
	
	public List<HashMap> getSuitByRoleId(String roleId) throws Exception;
	
	public DataCenter getDefauleDcBySuitId(String suitId) throws Exception;
	
	//by songyh 2015-12-14,增加默认期间及默认模板集等会话变量取数
	public List<HashMap> getPeriodInfo(HashMap params) throws Exception;
	
	public List<HashMap> getCurrencyInfo(String suitId) throws Exception;
	
	public List<HashMap> getEntityInfo(HashMap params) throws Exception;

	public List<HashMap> getModalTypeInfo(HashMap params) throws Exception;
	//
	
}
