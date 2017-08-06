package com.xzsoft.xc.po.mapper;

import java.util.HashMap;
import java.util.List;

/** 
 * @ClassName: PoContractTypeMapper 
 * @Description: 合同类型表Mapper
 * @author weicw 
 * @date 2016年11月29日 下午4:34:42 
 * 
 * 
 */
public interface PoContractTypeMapper {
	/**
	 * @Title:ifExist
	 * @Description:判断新增
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public String ifExist(String id);
	/**
	 * @Title:addContractType
	 * @Description:新增合同类型
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void addContractType(HashMap<String, Object> map);
	/**
	 * @Title:editContractType
	 * @Description:修改合同类型
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void editContractType(HashMap<String, Object> map);
	/**
	 * @Title:deleteContractType
	 * @Description:删除合同类型
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void deleteContractType(List<String> list);
}

