package com.xzsoft.xc.ar.dao.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.ar.dao.ArInvGlAdjDao;
import com.xzsoft.xc.ar.mapper.ArInvGlAdjMapper;
import com.xzsoft.xc.ar.modal.ArInvGlAdj;
import com.xzsoft.xc.ar.modal.ArInvGlHBean;
 


/**
 * 
  * @ClassName：ArInvGlAdjDaoImpl
  * @Description：调整单Dao层接口实现类
  * @author：张龙
  * @date：2016年7月22日 上午10:05:00
 */
@Repository("arInvGlAdjDao")
public class ArInvGlAdjDaoImpl implements ArInvGlAdjDao {
	@Resource
	ArInvGlAdjMapper mapper;
	/**
	 * @Title:addArAdjInfo
	 * @Description: 添加调整单
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArInvGlAdj
	 * @return 
	 * @throws 
	 */
	@Override
	public void addArAdjInfo(ArInvGlAdj arInvGlAdj) {
		mapper.addArAdjInfo(arInvGlAdj);
		
	}
	/**
	 * @Title:addInvTrans
	 * @Description: 添加交易明细
	 * 参数格式:
	 * @param com.xzsoft.xc.apar.modal.ArInvTransBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addInvTrans(ArInvTransBean arInvTransBean) {
		mapper.addInvTrans(arInvTransBean);
		
	}
	/**
	 * @Title:updateInvGlH
	 * @Description: 更新应收单
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateInvGlH(HashMap<String, Object> map) {
		mapper.updateInvGlH(map);
		
	}


	/**
	 * @Title:isAddOrEdit
	 * @Description: 判断新增或修改
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String isAddOrEdit(String glAdjId) {
		return mapper.isAddOrEdit(glAdjId);
	}
	/**
	 * @Title:updateInvGlhadj
	 * @Description: 更新调整单
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArInvGlAdj
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateInvGlhadj(ArInvGlAdj arInvGLAdj) {
		mapper.updateInvGlhadj(arInvGLAdj);
		
	}
	/**
	 * @Title:updateInvtrans
	 * @Description: 更新交易明细
	 * 参数格式:
	 * @param  com.xzsoft.xc.apar.modal.ArInvTransBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateInvtrans(ArInvTransBean arInvTransBean) {
		mapper.updateInvtrans(arInvTransBean);
		
	}
	/**
	 * @Title:getInvAdjInfo
	 * @Description: 查询调整单的数据
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.ar.modal.ArInvGlAdj
	 * @throws 
	 */
	@Override
	public ArInvGlAdj getInvAdjInfo(String id) {
		return mapper.getInvAdjInfo(id);
	}	
}

