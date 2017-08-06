package com.xzsoft.xc.ar.dao;

import java.util.HashMap;

import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.ar.modal.ArInvGlAdj;
import com.xzsoft.xc.ar.modal.ArInvGlHBean;
/**
 * 
  * @ClassName：ArInvGlAdjDao
  * @Description：应收单Dao
  * @author：张龙
  * @date：2016年7月22日 下午9:55:26
 */

public interface ArInvGlAdjDao {
	/**
	 * @Title:addArAdjInfo
	 * @Description: 添加调整单
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArInvGlAdj
	 * @return 
	 * @throws 
	 */
	public void addArAdjInfo(ArInvGlAdj arInvGlAdj);
	
	/**
	 * @Title:addInvTrans
	 * @Description: 添加交易明细
	 * 参数格式:
	 * @param com.xzsoft.xc.apar.modal.ArInvTransBean
	 * @return 
	 * @throws 
	 */
	public void addInvTrans(ArInvTransBean arInvTransBean);
	
	/**
	 * @Title:isAddOrEdit
	 * @Description: 判断新增或修改
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	public String isAddOrEdit(String glAdjId);
	
	/**
	 * @Title:updateInvGlH
	 * @Description: 更新应收单
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void updateInvGlH(HashMap<String, Object> map);
	
	/**
	 * @Title:updateInvGlhadj
	 * @Description: 更新调整单
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArInvGlAdj
	 * @return 
	 * @throws 
	 */
	public void updateInvGlhadj(ArInvGlAdj arInvGLAdj);
	
	/**
	 * @Title:updateInvtrans
	 * @Description: 更新交易明细
	 * 参数格式:
	 * @param  com.xzsoft.xc.apar.modal.ArInvTransBean
	 * @return 
	 * @throws 
	 */
	public void updateInvtrans(ArInvTransBean arInvTransBean);
	/**
	 * @Title:getInvAdjInfo
	 * @Description: 查询调整单的数据
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.ar.modal.ArInvGlAdj
	 * @throws 
	 */
	public ArInvGlAdj getInvAdjInfo(String id);
	
}

