package com.xzsoft.xc.po.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.po.dao.PoContractTypeDao;
import com.xzsoft.xc.po.mapper.PoContractTypeMapper;

/** 
 * @ClassName: PoContractTypeDaoImpl 
 * @Description: 合同类型Dao层实现类
 * @author weicw 
 * @date 2016年11月29日 下午4:41:17 
 * 
 * 
 */
@Repository("poContractTypeDao")
public class PoContractTypeDaoImpl implements PoContractTypeDao{
	@Resource
	PoContractTypeMapper mapper;
	/**
	 * @Title:ifExist
	 * @Description:判断新增
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public String ifExist(String id){
		return mapper.ifExist(id);
	}
	/**
	 * @Title:addContractType
	 * @Description:新增合同类型
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void addContractType(HashMap<String, Object> map) {
		mapper.addContractType(map);
	}
	/**
	 * @Title:editContractType
	 * @Description:修改合同类型
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void editContractType(HashMap<String, Object> map) {
		mapper.editContractType(map);
	}
	/**
	 * @Title:deleteContractType
	 * @Description:删除合同类型
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void deleteContractType(List<String> list) {
		mapper.deleteContractType(list);
	}

}

