package com.xzsoft.xc.po.dao;

import java.util.HashMap;
import java.util.List;


/** 
 * @ClassName: PoContractTypeDao 
 * @Description: 合同类型表Dao层
 * @author weicw 
 * @date 2016年11月29日 下午4:39:11 
 * 
 * 
 */
public interface PoContractTypeDao {
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

